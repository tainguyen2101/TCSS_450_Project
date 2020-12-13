package edu.uw.group1app.ui.chat;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import edu.uw.group1app.R;

/**
 * It provides message information.
 *
 * @author Gyubeom Kim
 * @version 2.0
 */
public final class ChatMessage implements Serializable {

    /**
     * message id
     */
    private final int mMessageId;

    /**
     * message
     */
    private final String mMessage;

    /**
     * sender of message
     */
    private final String mSender;

    /***
     * time stamp of message
     */
    private final String mTimeStamp;


    /**
     * it is constructor.
     *
     * @param messageId representing message id
     * @param message representing message
     * @param sender representing sender of message
     * @param timeStamp representing timestamp of message
     */
    public ChatMessage(int messageId, String message, String sender, String timeStamp) {
        mMessageId = messageId;
        mMessage = message;
        mSender = sender;
        mTimeStamp = timeStamp;
    }

    /**
     * Static factory method to turn a properly formatted JSON String into a
     * ChatMessage object.
     *
     * @param cmAsJson the String to be parsed into a ChatMessage Object.
     * @return a ChatMessage Object with the details contained in the JSON String.
     * @throws JSONException when cmAsString cannot be parsed into a ChatMessage.
     */
    public static ChatMessage createFromJsonString(final String cmAsJson) throws JSONException {
        final JSONObject msg = new JSONObject(cmAsJson);
        return new ChatMessage(msg.getInt("messageid"),
                msg.getString("message"),
                msg.getString("email"),
                msg.getString("timestamp"));
    }

    /**
     * it returns message
     *
     * @return message representing message
     */
    public String getMessage() {
        return mMessage;
    }

    /**
     * it return sender of message
     *
     * @return sender representing sender of message
     */
    public String getSender() {
        return mSender;
    }

    /**
     * it returns timestamp of message
     *
     * @return timeStamp representing timestamp of message
     */
    public String getTimeStamp() {
        return mTimeStamp;
    }

    /**
     * it returns message id
     *
     * @return messageId representing message id
     */
    public int getMessageId() {
        return mMessageId;
    }

    /**
     * Provides equality solely based on MessageId.
     *
     * @param other the other object to check for equality
     * @return true if other message ID matches this message ID, false otherwise
     */
    @Override
    public boolean equals(@Nullable Object other) {
        boolean result = false;
        if (other instanceof ChatMessage) {
            result = mMessageId == ((ChatMessage) other).mMessageId;
        }
        return result;
    }
}
