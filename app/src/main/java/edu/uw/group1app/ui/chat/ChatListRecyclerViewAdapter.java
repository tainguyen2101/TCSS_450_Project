package edu.uw.group1app.ui.chat;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.List;

import edu.uw.group1app.R;
import edu.uw.group1app.ui.contacts.ContactRecyclerViewAdapter;


public class ChatListRecyclerViewAdapter extends RecyclerView.Adapter<ChatListRecyclerViewAdapter.ChatListViewHolder> {

    private List<ChatRoom> mChatRooms;
    private Context mContext;
    private final FragmentManager mFragMan;

    /**
     * Constructor that builds the recycler view adapter from
     */
    public ChatListRecyclerViewAdapter(List<ChatRoom> chatrooms,  Context context, FragmentManager fm) {
        this.mChatRooms = chatrooms;
        this.mContext = context;
        this.mFragMan = fm;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View chatListView = inflater.inflate(R.layout.fragment_chat_list_card, parent, false);
        ChatListViewHolder viewHolder = new ChatListViewHolder(chatListView);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        try {
            holder.setChatRoom(mChatRooms.get(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mChatRooms.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public class ChatListViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private ImageButton moreButtonView;
        private ChatRoom mChatRoom;
        private final View mView;

        public ChatListViewHolder(View v) {
            super(v);
            mView = v;
            nameTextView = v.findViewById(R.id.chatRoom_Title);
            moreButtonView = v.findViewById(R.id.button_add_chat);
        }

        void setChatRoom(final ChatRoom chatRoom) throws JSONException {
            mChatRoom = chatRoom;
            nameTextView.setText(chatRoom.getmChatRoomName() + " " + chatRoom.getmChatId());
        }
    }
}
