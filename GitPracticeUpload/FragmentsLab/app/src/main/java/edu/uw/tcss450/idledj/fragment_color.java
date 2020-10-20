package edu.uw.tcss450.idledj;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.idledj.databinding.FragmentColorBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_color extends Fragment {
    private FragmentColorBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentColorBinding.inflate(getLayoutInflater());
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_color2, container, false);
    }
}