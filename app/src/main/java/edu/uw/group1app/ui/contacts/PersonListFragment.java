package edu.uw.group1app.ui.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

import edu.uw.group1app.R;
import edu.uw.group1app.databinding.FragmentPersonListBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonListFragment extends Fragment {

    private ArrayList<Person> mDummyContact;

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
        mDummyContact = Person.createContactList(5);
        binding.listRoot.setAdapter(new PersonRecycleViewAdapter(mDummyContact));
    }
}