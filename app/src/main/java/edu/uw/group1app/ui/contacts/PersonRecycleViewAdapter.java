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
 * Person List Fragment Recycle View Adapter
 * @author Ford Nguyen
 * @version 1.0
 */
public class PersonRecycleViewAdapter extends RecyclerView.Adapter<PersonRecycleViewAdapter.PersonViewHolder> {

    private List<Person> mContact;

    public PersonRecycleViewAdapter(List<Person> contacts) {
        this.mContact = contacts;
    }

    @NonNull
    @Override
    public PersonRecycleViewAdapter.PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.person_item, parent, false);
        PersonViewHolder viewHolder = new PersonViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        Person person = mContact.get(position);

        TextView nameTextView = holder.nameTextView;
        TextView phoneTextView = holder.phoneTextView;

        nameTextView.setText(person.getName());
        phoneTextView.setText(person.getNumber());
    }

    @Override
    public int getItemCount() {
        return mContact.size();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView phoneTextView;

        public PersonViewHolder(View v) {
            super(v);
            nameTextView = v.findViewById(R.id.contact_name);
            phoneTextView = v.findViewById(R.id.contact_number);
            phoneTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_contacts_recents_24,
                    0,0,0);
        }
    }
}
