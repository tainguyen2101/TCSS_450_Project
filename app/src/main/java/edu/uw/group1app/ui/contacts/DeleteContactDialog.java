package edu.uw.group1app.ui.contacts;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uw.group1app.R;
import edu.uw.group1app.databinding.FragmentContactListBinding;
import edu.uw.group1app.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteContactDialog extends DialogFragment {

    private UserInfoViewModel mUserModel;
    private ContactListViewModel mContactModel;
    private final int mMemberID;
    private final FragmentManager mFragMan;
    private final ContactRecyclerViewAdapter.ContactViewHolder mUpdater;

    /**
     * DElete contact dialog contructor given a in and a Fragment manger
     * @param memberID the int representing member id
     * @param fm the fragment manager
     */
    public DeleteContactDialog(int memberID, FragmentManager fm,
                               ContactRecyclerViewAdapter.ContactViewHolder updater) {
        this.mMemberID = memberID;
        this.mFragMan = fm;
        mUpdater = updater;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserModel = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

        mContactModel = new ViewModelProvider(getActivity())
                .get(ContactListViewModel.class);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentContactListBinding binding = FragmentContactListBinding.bind(getView());

        mContactModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            binding.listRoot.setAdapter(
                    new ContactRecyclerViewAdapter(contactList, this.getContext(),
                            getChildFragmentManager()));
        });
    }

    /**
     * Created the dialog window
     * @param savedInstanceState the saved instance state.
     * @return the Dialog.
     */
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_delete_contact_dialog, null);
        builder.setView(view)
                .setNegativeButton("Yes", (dialogInterface, i) -> {
                    mContactModel.deleteContact(mUserModel.getmJwt(), mMemberID);
                    mUpdater.deleteContact();
                })
                .setPositiveButton("Cancel", (dialogInterface, i) -> {
                });
        return builder.create();
    }
}