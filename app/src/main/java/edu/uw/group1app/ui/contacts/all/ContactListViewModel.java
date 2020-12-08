package edu.uw.group1app.ui.contacts.all;

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
import com.pusher.pushnotifications.BeamsCallback;
import com.pusher.pushnotifications.PushNotifications;
import com.pusher.pushnotifications.PusherCallbackError;
import com.pusher.pushnotifications.auth.AuthData;
import com.pusher.pushnotifications.auth.AuthDataGetter;
import com.pusher.pushnotifications.auth.BeamsTokenProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.group1app.io.RequestQueueSingleton;

/**
 * Contact List View Model that connect to the back-end to pull user contacts from server
 * if verified on the back-end contacts table is 1
 * user has accept the friend request hence should be in the contact list
 *
 * @author Ford Nguyen
 * @version 1.0
 */
public class ContactListViewModel extends AndroidViewModel {

    private MutableLiveData<List<Contact>> mContactList;
    private MutableLiveData<List<Contact>> mFavoriteList;
    private final MutableLiveData<JSONObject> mResponse;


    /**
     * Constructor for Contact List View Model
     *
     * @param application the application
     */
    public ContactListViewModel(@NonNull Application application) {
        super(application);
        mContactList = new MutableLiveData<>(new ArrayList<>());
        mFavoriteList = new MutableLiveData<>(new ArrayList<>());
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    /**
     * contact list view model observer.
     *
     * @param owner    life cycle owner
     * @param observer observer
     */
    public void addContactListObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<Contact>> observer) {
        mContactList.observe(owner, observer);
    }


    /**
     * webservice response observer.
     *
     * @param owner    life cycle owner
     * @param observer observer
     */
    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    public void connectPusher(final String jwt, final String email) {
        BeamsTokenProvider tokenProvider = new BeamsTokenProvider(
                "https://mobileapp-group-backend.herokuapp.com/pusher",
                new AuthDataGetter() {
                    @Override
                    public AuthData getAuthData() {
                        // Headers and URL query params your auth endpoint needs to
                        // request a Beams Token for a given user
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("Authorization", jwt);

                        HashMap<String, String> queryParams = new HashMap<>();
                        return new AuthData(
                                headers,
                                queryParams
                        );
                    }
                }
        );

        PushNotifications.setUserId(email, tokenProvider,
                new BeamsCallback<Void, PusherCallbackError>(){
                    @Override
                    public void onSuccess(Void... values) {
                        Log.i("PusherBeams", "Successfully authenticated with Pusher Beams");
                    }

                    @Override
                    public void onFailure(PusherCallbackError error) {
                        Log.i("PusherBeams", "Pusher Beams authentication failed: "
                                + error.getMessage());
                    }
                });
    }

    /**
     * connect to the webservice and get contact list
     *
     * @param jwt authorization token
     */
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
     * connect to the webservice and get favorite list
     *
     * @param jwt authorization token
     */
    public void connectGetFavorite(String jwt) {
        String url = "https://mobileapp-group-backend.herokuapp.com/contact/favorite";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, // no body
                this::handleSuccessFavorite,
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
     * connect to the webservice and request for a contact deletion
     *
     * @param jwt      JWT authorization token
     * @param memberID to be deleted
     */
    public void deleteContact(String jwt, final int memberID) {
        String url = "https://mobileapp-group-backend.herokuapp.com/contact/contact/" + memberID;
        Request request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                mResponse::setValue,
                this::handleError) {
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
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }

    /**
     * add a contact to favorite tab
     *
     * @param jwt      JWT Authorization Token
     * @param memberID to be favorite
     */
    public void addFavorite(final String jwt, final int memberID) {
        String url = "https://mobileapp-group-backend.herokuapp.com/contact/favorite/" + memberID;


        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                mResponse::setValue,
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
     * Remove a contact from the favorite tab
     *
     * @param jwt      JWT Authorization Token
     * @param memberID to be un-favorite
     */
    public void unFavorite(final String jwt, final int memberID) {
        String url = "https://mobileapp-group-backend.herokuapp.com/contact/favorite/delete/"
                + memberID;

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                mResponse::setValue,
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
     * Aceept friend request
     *
     * @param jwt      JWT
     * @param memberID to accept
     */
    public void acceptRequest(final String jwt, final int memberID) {
        String url = "https://mobileapp-group-backend.herokuapp.com/contact/request/" + memberID;

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                mResponse::setValue,
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

    public void declineRequest(final String jwt, final String username) {
        String url = "https://mobileapp-group-backend.herokuapp.com/contact/decline";

        JSONObject body = new JSONObject();
        try {
            body.put("userName", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                mResponse::setValue,
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
     * send a friend request to username if match.
     *
     * @param jwt      JWT Authorization Token.
     * @param username the username.
     */
    public void addFriend(final String jwt, final String username) {

        String url = "https://mobileapp-group-backend.herokuapp.com/contact/add";

        JSONObject body = new JSONObject();
        try {
            body.put("userName", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                body, //push token found in the JSONObject body
                mResponse::setValue,
                this::handleAddError)
        {
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
     *
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

    /**
     * handle a success connection to the back-end
     *
     * @param result result
     */
    private void handleSuccessFavorite(final JSONObject result) {
        ArrayList<Contact> temp = new ArrayList<>();
        try {
            JSONArray contacts = result.getJSONArray("contacts");
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject contact = contacts.getJSONObject(i);
                int favorite = contact.getInt("favorite");
                if (favorite == 1) {
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
        mFavoriteList.setValue(temp);
    }

    public void putContactMembers(final String jwt, int chatID, int memberID) throws JSONException {
        String url = "https://mobileapp-group-backend.herokuapp.com/addcontactmember/" + chatID  + "/" + memberID;
        System.out.println("Adding Contact To Chat, Member ID: ");
        JSONObject body = new JSONObject();
        try {
            body.put("memberid", memberID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(body.toString());

        Request request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                body, //push token found in the JSONObject body
                mResponse::setValue,
                this::handleAddError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }
    /**
     * handle a failure connection to the back-end
     * @param error the error.
     */
    private void handleAddError(final VolleyError error) {
        Log.e("CONNECTION ERROR", "No Chat Info");
    }

    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        } else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            try {
                mResponse.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:" + data +
                        "}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }
}