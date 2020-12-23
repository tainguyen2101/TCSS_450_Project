package edu.uw.group1app.ui.contacts.all;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.group1app.R;
import edu.uw.group1app.databinding.FragmentContactListBinding;
import edu.uw.group1app.model.UserInfoViewModel;
import edu.uw.group1app.ui.chat.ChatFragment;


/**
 * This class generate a Fragment that contains list of contacts
 * @author Ford Nguyen
 * @version 3.0
 */
public class ContactListFragment extends Fragment  {

    private ContactListViewModel mModel;
    private UserInfoViewModel mInfoModel;
    private int mChatID;
    private boolean mThroughChat;



    public ContactListFragment() {
        // empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = new ViewModelProvider(getActivity()).get(ContactListViewModel.class);

        mInfoModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        /*Chat id*/
        if(getArguments() != null) {
            ContactListFragmentArgs args = ContactListFragmentArgs.fromBundle(getArguments());
            mChatID = args.getChatid();
            mThroughChat = args.getThroughChat();
        }

        mModel.connectGet(mInfoModel.getmJwt());

        mModel.connectPusher(mInfoModel.getmJwt(), mInfoModel.getEmail());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentContactListBinding binding = FragmentContactListBinding.bind(getView());

        FloatingActionButton fab = view.findViewById(R.id.contact_add_float_button);

        mModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
                binding.listRoot.setAdapter(
                new ContactRecyclerViewAdapter(contactList, this.getContext(),
                        getChildFragmentManager(), mInfoModel, mModel, mChatID, mThroughChat));
                fab.setOnClickListener(v -> {
                    ContactAddDialog dialog = new ContactAddDialog(mInfoModel, mModel);
                    dialog.show(getChildFragmentManager(), "add");
                });

        });
    }
}