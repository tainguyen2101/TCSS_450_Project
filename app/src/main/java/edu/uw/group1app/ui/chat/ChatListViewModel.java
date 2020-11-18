package edu.uw.group1app.ui.chat;

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
import java.util.function.IntFunction;

import edu.uw.group1app.R;
import edu.uw.group1app.model.UserInfoViewModel;

public class ChatListViewModel extends AndroidViewModel {
    private MutableLiveData<List<ChatRoom>> mChatRoomList;
    private UserInfoViewModel uiv;

    public ChatListViewModel(@NonNull Application application) {
        super(application);
        mChatRoomList = new MutableLiveData<>();
        mChatRoomList.setValue(new ArrayList<>());
    }

    public void addChatListObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super List<ChatRoom>> observer) {
        mChatRoomList.observe(owner, observer);
    }

    /**
     * Method to handle volley errors.
     *
     * @param error VolleyError object.
     */
    private void handleError(final VolleyError error) {
        if (error != null && error.getMessage() != null) {
            Log.e("CONNECTION ERROR", error.getMessage());
            throw new IllegalStateException(error.getMessage());
        }
    }

    /**
     * Method to interpret given JSONObject. for the delete method
     *
     * @param result Given JSONObject object.
     */
    private void handleDeleteResult(final JSONObject result) {
        try {
            Log.d("ChatListViewModel DELETE", "Result for delete attempt: " + result.getString("success"));
        } catch (JSONException e) {
            throw new IllegalStateException("Unexpected response in ChatListViewModel: " + result);
        }
    }


    /**
     * Method to interpret given JSONObject.
     *
     * @param result Given JSONObject object.
     */
    private void handleResult(final JSONObject result) {
        if (!result.has("rows")) {
            throw new IllegalStateException("Unexpected response in ChatListViewModel: " + result);
        }
        try {
            JSONArray rows = result.getJSONArray("rows");
            ArrayList<ChatRoom> listOfChatRooms = new ArrayList<>();
            for (int i = 0; i < rows.length(); i++) {
                JSONObject row = rows.getJSONObject(i);
                int chatId = row.getInt("chatid");
                ChatRoom cr = new ChatRoom(getApplication(), uiv, chatId);
                listOfChatRooms.add(cr);

            }
            mChatRoomList.setValue(listOfChatRooms);

        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Method to connect to webservice and get chat data. Retrieves the list of chatIDs the user is a part of.
     */
    public void connectGet() {
        if (uiv == null) {
            throw new IllegalArgumentException("No UserInfoViewModel is assigned");
        }

        String url = getApplication().getResources().getString(R.string.base_url) +
                "chatData?memberId=" + uiv.getmMeberId();

        Request request = new JsonObjectRequest(Request.Method.GET, url, null,
                this::handleResult, this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", uiv.getmJwt());
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10_000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }

    public void connectDeleteChat(final int chatId) {
        Log.d("ChatListViewModel DELETE", "Request to delete chat: " + chatId + " for email: " + uiv.getEmail());
        String url = getApplication().getResources().getString(R.string.base_url) + "chats"
                + "?chatId=" + chatId
                + "&email=" + uiv.getEmail();

        Request request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                this::handleDeleteResult, this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", uiv.getmJwt());
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10_000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }

    private void handleAddChatResponse(final JSONObject response, ArrayList<String> usernames) {
        if (!response.has("success")) {
            throw new IllegalStateException("Unexpected response in ChatListViewModel: " + response);
        }
        try {
            ArrayList<ChatRoom> list = new ArrayList<>();
            if (mChatRoomList.getValue() != null){
                list.addAll(mChatRoomList.getValue());
            }
            int chatID = response.getInt("chatID");

            ChatRoom cr = new ChatRoom(getApplication(), uiv, chatID);
            list.add(cr);

            for (int i = 0; i < usernames.size(); i++) {
                connectAddMemberInChatPut(chatID, usernames.get(i));
            }
            mChatRoomList.setValue(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void connectAddChat(String nameOfChat, ArrayList<String> usernames) {
        if (uiv == null) {
            throw new IllegalArgumentException("No UserInfoViewModel is assigned");
        }
        String url = getApplication().getResources().getString(R.string.base_url) +
                "chats";

        JSONObject body = new JSONObject();
        try {
            body.put("name", nameOfChat);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new JsonObjectRequest(Request.Method.POST, url, body,
                response -> handleAddChatResponse(response, usernames), this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", uiv.getmJwt());

                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10_000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }


    public void connectAddMemberInChatPut(int chatID, String userName) {
        if (uiv == null) {
            throw new IllegalArgumentException("No UserInfoViewModel is assigned");
        }
        String url = getApplication().getResources().getString(R.string.base_url) +
                "chats?chatId=" + chatID + "&username=" + userName;

        Request request = new JsonObjectRequest(Request.Method.PUT, url, null,
                null, this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", uiv.getmJwt());

                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10_000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }
    public void setUserInfoViewModel(UserInfoViewModel vm) {
        uiv = vm;
    }
}
