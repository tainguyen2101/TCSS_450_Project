package edu.uw.group1app.ui.contacts.all;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import edu.uw.group1app.R;
import edu.uw.group1app.model.UserInfoViewModel;

/**
 * Contact Detail Dialog to show user more detail of a contact when they click on a contact card
 * @author Ford Nguyen
 * @version 1.0
 */

public class ContactDetailDialog extends DialogFragment {

    private final Contact mContact;

    private final ContactListViewModel mContactModel;

    private final UserInfoViewModel mUserModel;

    private final ContactRecyclerViewAdapter.ContactViewHolder mUpdater;

    public ContactDetailDialog(Contact contact, ContactListViewModel contactModel,
                               UserInfoViewModel infoModel,
                               ContactRecyclerViewAdapter.ContactViewHolder updater) {
        this.mContact = contact;
        this.mContactModel = contactModel;
        this.mUserModel = infoModel;
        mUpdater = updater;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.contact_detail, null);

        TextView nameText = view.findViewById(R.id.contact_detail_name);
        nameText.setText(mContact.getFirstName() + " " + mContact.getLastName());

        TextView usernameText = view.findViewById(R.id.contact_detail_username);
        usernameText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_contacts_all_24,
                0, 0, 0);
        usernameText.setText(mContact.getUsername());

        TextView emailText = view.findViewById(R.id.contact_detail_email);
        emailText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_email_24,
                0, 0, 0);
        emailText.setText(mContact.getEmail());

        Button deleteButton = view.findViewById(R.id.contact_detail_delete_button);
        deleteButton.setOnClickListener(v -> {
            mContactModel.deleteContact(mUserModel.getmJwt(), mContact.getMemberID());
            mUpdater.deleteContact();
            dismiss();
        });


        builder.setView(view);
        return builder.create();
    }
}
