package edu.uw.group1app.ui.contacts.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.uw.group1app.R;
import edu.uw.group1app.databinding.FragmentSearchBinding;
import edu.uw.group1app.model.UserInfoViewModel;
import edu.uw.group1app.ui.contacts.all.Contact;
import edu.uw.group1app.ui.contacts.all.ContactListViewModel;

/**
 * Search Fragment.
 * @author Ford Nguyen
 * @version 1.0
 */
public class SearchFragment extends Fragment {
    private ContactListViewModel mModel;
    private UserInfoViewModel mUser;
    private List<Contact> list = new ArrayList<>();
    private SearchRecyclerViewAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = new ViewModelProvider(getActivity()).get(ContactListViewModel.class);
        mUser = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        mModel.connectGetAll(mUser.getmJwt());
        list = mModel.getList();
        adapter = new SearchRecyclerViewAdapter(list);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentSearchBinding binding = FragmentSearchBinding.bind(getView());
        mModel.addContactListObserver(getViewLifecycleOwner(), contactList ->
                binding.listRoot.setAdapter(adapter)
        );
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

}