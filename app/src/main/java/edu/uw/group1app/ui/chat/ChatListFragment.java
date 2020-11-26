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
 * A simple {@link Fragment} subclass.
 */
public class ChatListFragment extends Fragment {

    private ChatListViewModel mChatListModel;
    private UserInfoViewModel mUserInfoViewmodel;
    private FragmentChatListBinding binding;
    private ChatListRecyclerViewAdapter chatListRecyclerViewAdapter;

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
        binding.buttonAddChat.setOnClickListener(button -> createChatRoom());
        chatListRecyclerViewAdapter = new ChatListRecyclerViewAdapter(new ArrayList<>(), this);
        binding.listChatRoot.setAdapter(chatListRecyclerViewAdapter);
        mChatListModel.addChatListObserver(getViewLifecycleOwner(), chatRoomList -> {
            chatListRecyclerViewAdapter.setChatRooms(chatRoomList);
        });
    }

    private void createChatRoom(){
        String title = binding.textChatTitle.getText().toString().trim();
        if(title.length() < 2){
            binding.textChatTitle.setError("Please enter a valid chat room name");
        }else{
            mChatListModel.addChat(mUserInfoViewmodel.getmJwt(), title);
            //mChatListModel.putMembers(mUserInfoViewmodel.getmJwt(), 11);
        }
    }

    public void deleteChat(final int chatId) {
        mChatListModel.deleteChat(chatId);
    }
}