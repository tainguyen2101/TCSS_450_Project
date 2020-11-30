package edu.uw.group1app.ui.contacts.all;

import android.content.DialogInterface;
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
import com.pusher.pushnotifications.PushNotifications;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import edu.uw.group1app.R;
import edu.uw.group1app.databinding.FragmentContactListBinding;
import edu.uw.group1app.model.UserInfoViewModel;


/**
 * This class generate a Fragment that contains list of contacts
 * @author Ford Nguyen
 * @version 3.0
 */
public class ContactListFragment extends Fragment {

    private ContactListViewModel mModel;
    private UserInfoViewModel mInfoModel;
    private EditText mUserInput;
    private AlertDialog mDialog;



    public ContactListFragment() {
        // empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = new ViewModelProvider(getActivity()).get(ContactListViewModel.class);

        mInfoModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);


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


        fab.setOnClickListener(v -> {

            // Set up the input
            mUserInput = new EditText(this.getContext());
            mUserInput.setHint("Username");

            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
            builder.setTitle("Who would like to be friend with?");

            // Specify the type of input expected
            mUserInput.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(mUserInput);
            builder.setPositiveButton("OK", (dialog, which) -> {
                // DO NOTHING
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> {
                // DO NOTHING
            });
            mDialog = builder.create();
            mDialog.show();

            Button okButton = mDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            okButton.setOnClickListener(v1 -> {
                mModel.addFriend(mInfoModel.getmJwt(), mUserInput.getText().toString());
                mModel.addResponseObserver(
                        getViewLifecycleOwner(),
                        this::observeAddUserResponse);
            });



            Button cancelButton = mDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            cancelButton.setOnClickListener(v12 -> {
                mDialog.dismiss();
            });

            // clear error when dismiss dialog
            mUserInput.setError(null);
        });
        mModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            binding.listRoot.setAdapter(
                    new ContactRecyclerViewAdapter(contactList, this.getContext(),
                            getChildFragmentManager(), mInfoModel, mModel));
        });
    }


    /**
     * An observer on the HTTP Response from the web server.
     *
     * @param response the Response from the server
     */
    private void observeAddUserResponse(JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    mUserInput.setError("Error Adding: " +
                            response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                mDialog.cancel();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

}