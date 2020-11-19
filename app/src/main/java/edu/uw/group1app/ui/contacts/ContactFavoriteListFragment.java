package edu.uw.group1app.ui.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.uw.group1app.R;
import edu.uw.group1app.databinding.FragmentContactFavoriteListBinding;
import edu.uw.group1app.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFavoriteListFragment extends Fragment {

    private ContactListViewModel mModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_favorite_list, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(ContactListViewModel.class);

        UserInfoViewModel model = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mModel.connectGetFavorite(model.getmJwt());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentContactFavoriteListBinding binding = FragmentContactFavoriteListBinding.bind(getView());

        mModel.addContactFavoriteListObserver(getViewLifecycleOwner(), contactList -> {
            binding.listRoot.setAdapter(
                    new ContactFavoriteRecyclerViewAdapter(contactList, this.getContext(),
                            getChildFragmentManager())
            );
        });
    }
}