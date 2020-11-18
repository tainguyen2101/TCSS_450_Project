package edu.uw.group1app.ui.contacts;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.group1app.MainActivity;
import edu.uw.group1app.R;

/**
 * Contact List Fragment Recycle View Adapter
 * @author Ford Nguyen
 * @version 1.0
 */
public class ContactRecyclerViewAdapter extends
        RecyclerView.Adapter<ContactRecyclerViewAdapter.ContactViewHolder> {

    private List<Contact> mContacts;

    private Context mContext;

    private final FragmentManager mFragMan;

    public ContactRecyclerViewAdapter(List<Contact> contacts, Context context, FragmentManager fm) {
        this.mContacts = contacts;
        mContext = context;
        this.mFragMan = fm;
    }

    @NonNull
    @Override
    public ContactRecyclerViewAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.contact_item, parent, false);
        ContactViewHolder viewHolder = new ContactViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.setContact(mContacts.get(position));

    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView usernameTextView;
        public ImageButton moreButtonView;
        public Contact mContact;
        public final View mView;

        public ContactViewHolder(View v) {
            super(v);
            mView = v;
            nameTextView = v.findViewById(R.id.contact_name);
            usernameTextView = v.findViewById(R.id.contact_username);
            moreButtonView = v.findViewById(R.id.contact_more_button);
        }

        private void deleteDialog() {
            DeleteContactDialog dialog = new DeleteContactDialog(mContact.getMemberID(), mFragMan,
                    this);
            dialog.show(mFragMan, "yes/no?");
        }

        /**
         * Sets the contact.
         *
         * @param contact the contact
         */
        void setContact(final Contact contact) {
            mContact = contact;
            nameTextView.setText(contact.getFirstName() + " " + contact.getLastName());
            usernameTextView.setText(contact.getUsername());
            moreButtonView.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.getMenuInflater().inflate(R.menu.pop_up_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.favorite_pop_menu:
                            return true;
                        case R.id.delete_pop_menu:
                            deleteDialog();
                            return true;
                        default:
                            return false;
                    }
                });
                popupMenu.show();
            });
        }

        public void deleteContact(){
            mContacts.remove(mContact);
            notifyDataSetChanged();
        }

    }
}
