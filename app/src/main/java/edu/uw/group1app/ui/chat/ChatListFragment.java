package edu.uw.group1app.ui.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.uw.group1app.R;
import edu.uw.group1app.databinding.FragmentChatListBinding;
import edu.uw.group1app.model.UserInfoViewModel;

/**
 * this class provides a function that a user can chat
 * , add chats room, deletes chat room, and navigates to the contact
 *
 * @author Gyubeom Kim
 * @version 2.0
 */
public class ChatListFragment extends Fragment {

    /**
     * Chat list view model
     */
    private ChatListViewModel mChatListModel;

    /**
     *
     */
    private UserInfoViewModel mUserInfoViewmodel;

    /**
     * user info view model containing user information
     */
    private FragmentChatListBinding binding;

    /**
     * ChatListRecyclerViewAdapter
     */
    private ChatListRecyclerViewAdapter chatListRecyclerViewAdapter;

    /**
     * this is constructor.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChatListModel = new ViewModelProvider(getActivity()).get(ChatListViewModel.class);
        mUserInfoViewmodel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mChatListModel.connectGet(mUserInfoViewmodel.getmJwt());
        mChatListModel.setUserInfoViewModel(mUserInfoViewmodel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentChatListBinding.bind(getView());

        //add chat room
        binding.buttonAddChat.setOnClickListener(button -> createChatRoom());

        //refresh chat room
        binding.buttonRefresh.setOnClickListener(button -> mChatListModel.connectGet(mUserInfoViewmodel.getmJwt()));
        chatListRecyclerViewAdapter = new ChatListRecyclerViewAdapter(new ArrayList<>(), this);

        //add chat room to the list
        binding.listChatRoot.setAdapter(chatListRecyclerViewAdapter);
        mChatListModel.addChatListObserver(getViewLifecycleOwner(), chatRoomList -> {
            chatListRecyclerViewAdapter.setChatRooms(chatRoomList);
        });
    }

    /**
     * creating chat room with title that user typed
     */
    private void createChatRoom() {
        String title = binding.textChatTitle.getText().toString().trim();
        if(title.length() < 2){
            binding.textChatTitle.setError("Please enter a valid chat room name");
        }else{
            mChatListModel.addChat(mUserInfoViewmodel.getmJwt(), title);
        }
    }

    /**
     * delete chat corresponding to the chat id
     *
     * @param chatId representing chat id
     */
    public void deleteChat(final int chatId) {
        mChatListModel.deleteChat(chatId);
    }
}