package edu.uw.group1app.ui.chat;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.List;

import edu.uw.group1app.R;
import edu.uw.group1app.databinding.FragmentChatListCardBinding;


public class ChatListRecyclerViewAdapter extends RecyclerView.Adapter<ChatListRecyclerViewAdapter.ChatListViewHolder> {

    private List<ChatRoom> mChatRooms;
    private final ChatListFragment mParent;
    //private final FragmentManager mFragMan;
    //private final ChatListFragment mParent;

    /**
     * Constructor that builds the recycler view adapter from
     */
    public ChatListRecyclerViewAdapter(List<ChatRoom> chats, ChatListFragment parent) {
        this.mChatRooms = chats;
        this.mParent = parent;
        //this.mFragMan = fm;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatListViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.fragment_chat_list_card, parent, false));
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
        private ChatRoom mChatRoom;
        private final View mView;
        private FragmentChatListCardBinding binding;

        public ChatListViewHolder(View v) {
            super(v);
            mView = v;
            nameTextView = v.findViewById(R.id.text_chatRoom_title);
            binding = FragmentChatListCardBinding.bind(mView);
        }

        void setChatRoom(final ChatRoom chatRoom) throws JSONException {
            mChatRoom = chatRoom;
            nameTextView.setText(mChatRoom.getmChatRoomName() + " " + mChatRoom.getmChatId());
            binding.buttonDelete.setOnClickListener(button -> deletChat());
            binding.buttonEnter.setOnClickListener(view ->
                    Navigation.findNavController(mView)
                            .navigate(ChatListFragmentDirections
                                    .actionNavigationChatToChatFragment(mChatRoom.getmChatId(), mChatRoom.getmChatRoomName())));
        }

        void deletChat() {
            mChatRooms.remove(mChatRoom);
            mParent.deleteChat(mChatRoom.getmChatId());
            notifyDataSetChanged();
        }
    }

    public void setChatRooms(List<ChatRoom> rooms){
        mChatRooms = rooms;
        notifyDataSetChanged();
    }
}
