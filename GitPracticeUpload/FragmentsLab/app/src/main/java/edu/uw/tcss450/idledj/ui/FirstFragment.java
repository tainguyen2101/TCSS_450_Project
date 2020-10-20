package edu.uw.tcss450.idledj.ui;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.idledj.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment implements View.OnClickListener{

    private edu.uw.tcss450.idledj.databinding.FragmentFirstBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_first, container, false);
        binding = edu.uw.tcss450.idledj.databinding.FragmentFirstBinding
                            .inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle
            savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //add this Fragment Object as the OnClickListener to the Red button
        binding.buttonRed.setOnClickListener(this);
        //Use a Lamda expression to add the OnClickListener
        binding.buttonGreen.setOnClickListener(button ->
                processColor(Color.GREEN));
        //Use a method reference to add the OnClickListener
        binding.buttonBlue.setOnClickListener(this::handleBlue);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void processColor(int color) {
        Log.d("ACTIVITY", "Red: " + Color.red(color) +
                " Green: " + Color.green(color) + " Blue: " + Color.blue(color));
        //The following object represents the action from first to color.
        FirstFragmentDirections.ActionFirstFragmentToColorFragment directions =
                FirstFragmentDirections.actionFirstFragmentToColorFragment(color);
        //Use the navigate method to perform the navigation.
        Navigation.findNavController(getView()).navigate(directions);

    }

    @Override
    public void onClick(View view) {
        if( view == binding.buttonRed) {
            processColor(Color.RED);
        }
    }

    private void handleBlue(View v) {
        if (v == binding.buttonBlue) {
            processColor(Color.BLUE);
        }
    }
}