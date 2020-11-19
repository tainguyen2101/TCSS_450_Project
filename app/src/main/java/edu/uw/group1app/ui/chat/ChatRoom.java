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

public class ChatRoom implements Serializable {

    private int mChatId;
    private String mChatRoomName;

    public ChatRoom(int chatid, String chatRoomName) {
        mChatId = chatid;
        mChatRoomName = chatRoomName;
    }

    public String getmChatRoomName() {
        return mChatRoomName;
    }

    public int getmChatId() {
        return mChatId;
    }
}