package edu.uw.group1app.ui.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import edu.uw.group1app.R;
import edu.uw.group1app.databinding.FragmentContactRequestListBinding;
import edu.uw.group1app.model.UserInfoViewModel;

/**
 * This class generate a Fragment that contains list of friend request
 * @author Ford Nguyen
 * @version 1.0
 */
public class ContactRequestListFragment extends Fragment {

    private ContactRequestViewModel mModel;

    public ContactRequestListFragment() {
        // empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(ContactRequestViewModel.class);

        UserInfoViewModel model = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mModel.connectGet(model.getmJwt());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_request_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentContactRequestListBinding binding = FragmentContactRequestListBinding.bind(getView());

        mModel.addRequestListObserver(getViewLifecycleOwner(), requestList -> {
            binding.listRoot.setAdapter(new ContactRequestRecyclerViewAdapter(requestList, this.getContext()));
        });


    }
}