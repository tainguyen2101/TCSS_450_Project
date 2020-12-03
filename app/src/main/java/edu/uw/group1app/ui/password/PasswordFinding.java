package edu.uw.group1app.ui.password;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.group1app.R;
import edu.uw.group1app.databinding.FragmentPasswordFindingBinding;
import edu.uw.group1app.ui.utils.PasswordValidator;

/**
 * A simple {@link Fragment} subclass.
 */
public class PasswordFinding extends Fragment {

    private FragmentPasswordFindingBinding binding;

    private PasswordValidator mEmailValidator =  PasswordValidator.checkPwdLength(2)
            .and(PasswordValidator.checkExcludeWhiteSpace())
            .and(PasswordValidator.checkPwdSpecialChar("@"));

    public PasswordFinding() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPasswordFindingBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonCancel.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        PasswordFindingDirections.actionPasswordFindingToSignInFragment()));

        //binding.buttonSend.setOnClickListener();

    }

}