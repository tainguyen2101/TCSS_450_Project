package edu.uw.group1app.ui.password;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import edu.uw.group1app.R;
import edu.uw.group1app.databinding.FragmentPasswordChangingBinding;
import edu.uw.group1app.databinding.FragmentPasswordRecoveryBinding;

/**
 * A simple {@link Fragment} subclass. Displays options for password recovery
 * Connects to a {@Link ViewModel} to allow for connection / request to backend
 * @author D. Jared Idler
 */
public class PasswordRecoveryFragment extends Fragment {
    private FragmentPasswordRecoveryBinding binding;

    private PasswordRecoveryViewModel mPasswordModel;

    public PasswordRecoveryFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPasswordRecoveryBinding.inflate(inflater);
        mPasswordModel =  new ViewModelProvider(getActivity()).get(PasswordRecoveryViewModel.class);

        return binding.getRoot();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.buttonSendRecovery.setOnClickListener(this::sendEmail);

    }
    /**asynchronous call to verify authentication with web service*/
    private void sendEmail(final View Button) {

        mPasswordModel.connect(binding.passwordRecoveryTextbox.getText().toString());

    }


}