package edu.uw.group1app.ui.contacts;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.group1app.R;

public class ContactRequestRecyclerViewAdapter extends
        RecyclerView.Adapter<ContactRequestRecyclerViewAdapter.RequestViewHolder> {

    private List<FriendRequest> mFriendRequest;

    public ContactRequestRecyclerViewAdapter(List<FriendRequest> requests) {
        this.mFriendRequest = requests;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.fragment_contact_request_card,
                parent, false);
        ContactRequestRecyclerViewAdapter.RequestViewHolder viewHolder = new
                ContactRequestRecyclerViewAdapter.RequestViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        holder.setRequest(mFriendRequest.get(position));
    }

    @Override
    public int getItemCount() {
        return mFriendRequest.size();
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {

        private TextView usernameTextView;
        private ImageButton acceptImageButton;
        private final View mView;
        private FriendRequest mRequest;

        public RequestViewHolder(View v) {
            super(v);
            mView = v;
            usernameTextView = v.findViewById(R.id.contact_username_request);
            acceptImageButton = v.findViewById(R.id.contact_request_accept_button);
        }

        private void setRequest(final FriendRequest request) {
            mRequest = request;
            usernameTextView.setText(request.getUsername());

            //Accept button on click listener
            acceptImageButton.setOnClickListener(v -> {
                // TODO: Accept request send a signal to back-end to change verified to 1


                // Notify change to data set
                //notifyDataSetChanged();
            });
        }
    }
}
