package edu.uw.group1app.ui.contacts.favorite;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.group1app.R;
import edu.uw.group1app.model.UserInfoViewModel;
import edu.uw.group1app.ui.contacts.all.Contact;
import edu.uw.group1app.ui.contacts.all.ContactListViewModel;

/**
 * Contact List Fragment Recycle View Adapter
 * @author Ford Nguyen
 * @version 1.0
 */
public class ContactFavoriteRecyclerViewAdapter extends
        RecyclerView.Adapter<ContactFavoriteRecyclerViewAdapter.ContactViewHolder> {

    private List<Contact> mContacts;
    private Context mContext;
    private final FragmentManager mFragMan;
    private UserInfoViewModel mUserModel;
    private ContactListViewModel mContactModel;

    public ContactFavoriteRecyclerViewAdapter(List<Contact> contacts, Context context,
                                              FragmentManager fm, UserInfoViewModel userModel,
                                              ContactListViewModel viewModel) {
        this.mContacts = contacts;
        this.mContext = context;
        this.mFragMan = fm;
        this.mUserModel = userModel;
        this.mContactModel = viewModel;
    }

    @NonNull
    @Override
    public ContactFavoriteRecyclerViewAdapter.ContactViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.favorite_item, parent, false);
        ContactViewHolder viewHolder = new ContactViewHolder(contactView);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.setContact(mContacts.get(position));

    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView usernameTextView;
        private ImageButton deleteButton;
        private Contact mContact;
        private final View mView;

        public ContactViewHolder(View v) {
            super(v);
            mView = v;
            nameTextView = v.findViewById(R.id.favorite_name);
            usernameTextView = v.findViewById(R.id.favorite_username);
            deleteButton = v.findViewById(R.id.favorite_delete_button);
        }



        /**
         * Sets the contact name and username
         * Sets the More Button Popup Menu with its behavior
         * @param contact the contact
         */
        @RequiresApi(api = Build.VERSION_CODES.Q)
        private void setContact(final Contact contact) {
            mContact = contact;

            nameTextView.setText(contact.getFirstName() + " " + contact.getLastName());
            usernameTextView.setText(contact.getUsername());

            deleteButton.setOnClickListener(v -> {
                mContactModel.unFavorite(mUserModel.getmJwt(), mContact.getMemberID());
                mContacts.remove(mContact);
                notifyDataSetChanged();
            });
        }


    }
}
