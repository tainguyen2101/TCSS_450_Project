package edu.uw.group1app.ui.contacts;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.uw.group1app.R;
import edu.uw.group1app.databinding.FragmentContactListBinding;
import edu.uw.group1app.model.UserInfoViewModel;


/**
 * This class generate a Fragment that contains list of contacts
 * @author Ford Nguyen
 * @version 2.0
 */
public class ContactListFragment extends Fragment {

    private ContactListViewModel mModel;

    private UserInfoViewModel mInfoModel;

    private String m_Text;


    public ContactListFragment() {
        // empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(ContactListViewModel.class);

        mInfoModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mModel.connectGet(mInfoModel.getmJwt());
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

        fab.setOnClickListener(v -> {
            // TODO: Navigate to a page for user to add friend

            StringBuilder strBuilder = new StringBuilder();
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
            builder.setTitle("Who would like to be friend with?");

            // Set up the input
            final EditText input = new EditText(this.getContext());
            input.setHint("Username");
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", (dialog, which) ->
                    mModel.addFriend(mInfoModel.getmJwt(), input.getText().toString()));
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        });

        mModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            binding.listRoot.setAdapter(
                    new ContactRecyclerViewAdapter(contactList, this.getContext(),
                            getChildFragmentManager())
            );
        });
    }

}