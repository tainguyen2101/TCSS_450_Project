package edu.uw.group1app.ui.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import edu.uw.group1app.R;
import edu.uw.group1app.databinding.FragmentPersonListBinding;
import edu.uw.group1app.model.UserInfoViewModel;


/**
 * This class generate a Fragment that contains list of contacts
 * For now it is use for Recents, All, Favorite
 * @author Ford Nguyen
 * @version 1.0
 */
public class ContactListFragment extends Fragment {

    private ContactListViewModel mModel;

    public ContactListFragment() {
        // empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(ContactListViewModel.class);

        UserInfoViewModel model = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        Log.i("CONTACT", model.getJwt());
        mModel.connectGet(model.getJwt());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_person_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentPersonListBinding binding = FragmentPersonListBinding.bind(getView());

        mModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            binding.listRoot.setAdapter(
                    new ContactRecyclerViewAdapter(contactList)
            );
        });

    }
}