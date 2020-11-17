package edu.uw.group1app.ui.contacts;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactListViewModel extends AndroidViewModel {

    private MutableLiveData<List<Contact>> mContactList;
    private final MutableLiveData<JSONObject> mResponse;


    /**
     * Constructor for Contact List View Model
     * @param application the application
     */
    public ContactListViewModel(@NonNull Application application) {
        super(application);
        mContactList = new MutableLiveData<>(new ArrayList<>());
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    /**
     * contact list view model observer.
     * @param owner life cycle owner
     * @param observer observer
     */
    public void addContactListObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<Contact>> observer) {
        mContactList.observe(owner, observer);
    }

    public void connectGet(String jwt) {
        String url = "https://mobileapp-group-backend.herokuapp.com/contact";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, // no body
                this::handleSuccess,
                this::handleError
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }


    /**
     * handle a success connection to the back-end
     * @param result result
     */
    private void handleSuccess(final JSONObject result) {
        ArrayList<Contact> temp = new ArrayList<>();
        try {
            JSONArray contacts = result.getJSONArray("contacts");
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject contact = contacts.getJSONObject(i);
                int verified = contact.getInt("verified");
                if (verified == 1) {
                    String email = contact.getString("email");
                    String firstName = contact.getString("firstName");
                    String lastName = contact.getString("lastName");
                    String username = contact.getString("userName");
                    int memberID = contact.getInt("memberId");

                    Contact entry = new Contact(email, firstName, lastName, username, memberID);
                    temp.add(entry);
                }
            }
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ContactViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
        mContactList.setValue(temp);
    }

    private void handleError(final VolleyError error) {
        Log.e("CONNECTION ERROR", "No contacts");
    }
}
