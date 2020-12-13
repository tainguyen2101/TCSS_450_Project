package edu.uw.group1app.ui.chat;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uw.group1app.R;
import edu.uw.group1app.model.UserInfoViewModel;

/**
 * This class save the information for each chatroom
 *
 * @author Gyubeom Kim
 * @version 2.0
 */
public class ChatRoom implements Serializable {

    /**
     * chat id
     */
    private int mChatId;

    /**
     * chat title
     */
    private String mChatRoomName;

    /**
     * this is constructor for chatroom
     *
     * @param chatid representing chat id
     * @param chatRoomName representing chat title
     */
    public ChatRoom(int chatid, String chatRoomName) {
        mChatId = chatid;
        mChatRoomName = chatRoomName;
    }

    /**
     * it returns chat title
     *
     * @return chat title
     */
    public String getmChatRoomName() {
        return mChatRoomName;
    }


    /**
     * it returns chat id
     *
     * @return chatid
     */
    public int getmChatId() {
        return mChatId;
    }
}