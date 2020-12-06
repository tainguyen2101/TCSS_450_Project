package edu.uw.group1app.ui.password;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.group1app.databinding.FragmentPasswordChangingBinding;
import edu.uw.group1app.ui.utils.PasswordValidator;

import static edu.uw.group1app.ui.utils.PasswordValidator.checkClientPredicate;
import static edu.uw.group1app.ui.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.group1app.ui.utils.PasswordValidator.checkPwdDigit;
import static edu.uw.group1app.ui.utils.PasswordValidator.checkPwdLength;
import static edu.uw.group1app.ui.utils.PasswordValidator.checkPwdLowerCase;
import static edu.uw.group1app.ui.utils.PasswordValidator.checkPwdSpecialChar;
import static edu.uw.group1app.ui.utils.PasswordValidator.checkPwdUpperCase;

/**
 * A simple {@link Fragment} subclass.
 */
public class PasswordChangingFragment extends Fragment {

    private FragmentPasswordChangingBinding binding;

    private PasswordChangingViewModel mPasswordModel;

    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));
    private PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> !pwd.equals(binding.textChageConfirmPass.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase().or(checkPwdUpperCase()));

    public PasswordChangingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPasswordChangingBinding.inflate(inflater);
        mPasswordModel =  new ViewModelProvider(getActivity()).get(PasswordChangingViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonCancel.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        PasswordChangingFragmentDirections.actionNavigationPasswordChageToNavigationUserSetting()));

        binding.buttonSend.setOnClickListener(button ->
                                                verifyAuthWithServer());
        /*mPasswordModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);*/

    }

    private void attemptRegister(final View button) {
        validateEmail();
    }

    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.textChangeEmail.getText().toString().trim()),
                this::validatePassword,
                result -> binding.textChangeEmail.setError("Please enter a valid Email address."));
    }

    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.textChangeNewPass.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.textChangeNewPass.setError("Please enter a valid Password."));
    }


    private void verifyAuthWithServer() {
        mPasswordModel.connect(
                binding.textChangeEmail.getText().toString(),
                binding.textChangeNewPass.getText().toString(),
                binding.textChageConfirmPass.getText().toString());
    }

    /*private void navigateToLogin() {
        PasswordFindingDirections.ActionPasswordFindingToSignInFragment directions =
                PasswordFindingDirections.actionPasswordFindingToSignInFragment();

        directions.setEmail(binding.textChangeEmail.getText().toString());
        directions.setPassword(binding.textChangeNewPass.getText().toString());

        Navigation.findNavController(getView()).navigate(directions);

    }*/

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.textChangeEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                //navigateToLogin();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}