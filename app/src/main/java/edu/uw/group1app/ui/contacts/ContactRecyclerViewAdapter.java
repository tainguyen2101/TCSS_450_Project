package edu.uw.group1app.ui.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.group1app.R;

/**
 * Contact List Fragment Recycle View Adapter
 * @author Ford Nguyen
 * @version 1.0
 */
public class ContactRecyclerViewAdapter extends
        RecyclerView.Adapter<ContactRecyclerViewAdapter.ContactViewHolder> {

    private List<Contact> mContact;

    public ContactRecyclerViewAdapter(List<Contact> contacts) {
        this.mContact = contacts;
    }

    @NonNull
    @Override
    public ContactRecyclerViewAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.person_item, parent, false);
        ContactViewHolder viewHolder = new ContactViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact person = mContact.get(position);

        TextView nameTextView = holder.nameTextView;
        TextView usernameTextView = holder.usernameTextView;

        nameTextView.setText(person.getFirstName() + " " + person.getLastName());
        usernameTextView.setText(person.getUsername());
    }

    @Override
    public int getItemCount() {
        return mContact.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView usernameTextView;

        public ContactViewHolder(View v) {
            super(v);
            nameTextView = v.findViewById(R.id.contact_name);
            usernameTextView = v.findViewById(R.id.contact_username);
        }

    }
}
