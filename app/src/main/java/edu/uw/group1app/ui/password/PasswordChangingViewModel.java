package edu.uw.group1app.ui.password;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Objects;

import edu.uw.group1app.R;

/**
 * PasswordChangingViewModel that connects to the back-end for changing a user
 * password.
 *
 * @author Gyubeom Kim
 * @version 2.0
 */
public class PasswordChangingViewModel extends AndroidViewModel {

    /**
     * response
     */
    private MutableLiveData<JSONObject> mResponse;

    /**
     * Constructor for PasswordChangingViewModel
     *
     * @param application the application
     */
    public PasswordChangingViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }
    /**
     * PasswordChangingViewModel observer.
     *
     * @param owner    life cycle owner
     * @param observer observer
     */
    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    /**
     * sending email, old password, new password as body to server
     *
     * @param email representing a user email
     * @param oldPass representing a user old password
     * @param newPass representing a user's new password
     */
    public void changePassword(final String email,
                               final String oldPass,
                               final String newPass) {
        String url = getApplication().getResources().getString(R.string.base_url) + "password/change";

        JSONObject body = new JSONObject();
        try {
            body.put("email", email);
            body.put("oldpassword", oldPass);
            body.put("newpassword", newPass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                mResponse::setValue,
                this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    /**
     * handle a failure connection to the back-end
     *
     * @param error the error.
     */
    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            try {
                mResponse.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:\"" + data +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }
}