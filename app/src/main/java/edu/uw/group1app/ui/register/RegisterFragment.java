package edu.uw.group1app.ui.register;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.group1app.R;
import edu.uw.group1app.databinding.FragmentRegisterBinding;
import edu.uw.group1app.ui.utils.PasswordValidator;

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
public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    private RegisterViewModel mRegisterModel;
    private PasswordValidator mNameValidator = checkPwdLength(1);
    private PasswordValidator mUserValidator = checkPwdLength(1);
    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    private PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> pwd.equals(binding.registerPwBox.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase().or(checkPwdUpperCase()));

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegisterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.actualRegButton.setOnClickListener(this::attemptRegister);
        mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    private void attemptRegister(final View button) {
        validateFirst();
    }

    private void validateFirst() {
        mNameValidator.processResult(
               //need to add additional fields to register page
                mNameValidator.apply(binding.registerFirstNameBox.getText().toString().trim()),
                this::validateLast,
                result -> binding.registerFirstNameBox.setError("Please enter a first name."));
    }

    private void validateLast() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.registerLastNameBox.getText().toString().trim()),
                this::validateEmail,
                result -> binding.registerLastNameBox.setError("Please enter a last name."));
    }

    private void validateUser() {
        mUserValidator.processResult(mUserValidator.apply(
                binding.registerUsernameBox.getText().toString().trim()), this:: validateEmail,
                result -> binding.registerUsernameBox.setError("Please enter a username"));

    }

    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.registerEmailBox.getText().toString().trim()),
                this::validatePasswordsMatch,
                result -> binding.registerEmailBox.setError("Please enter a valid Email address."));
    }

    private void validatePasswordsMatch() {
        PasswordValidator matchValidator =
                checkClientPredicate(
                        pwd -> pwd.equals(binding.registerPwBox.getText().toString().trim()));

        mEmailValidator.processResult(
                matchValidator.apply(binding.registerPwBox.getText().toString().trim()),
                this::validatePassword,
                result -> binding.registerPwBox.setError("Passwords must match."));
    }

    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.registerPwBox.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.registerPwBox.setError("Please enter a valid Password."));
    }

    private void verifyAuthWithServer() {
        //TODO: Add personal info fields
        mRegisterModel.connect(
                binding.registerFirstNameBox.getText().toString(),
                binding.registerLastNameBox.getText().toString(),
                binding.registerEmailBox.getText().toString(),
                binding.registerPwBox.getText().toString());
//        This is an Asynchronous call. No statements after should rely on the
//        result of connect().

    }
//TODO: Add navigation functionality
//    private void navigateToLogin() {
//        RegisterFragmentDirections.ActionRegisterFragmentToLoginFragment directions =
//                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment();
//
//        directions.setEmail(binding.registerEmailBox.getText().toString());
//        directions.setPassword(binding.registerPwBox.getText().toString());
//
//        Navigation.findNavController(getView()).navigate(directions);
//
//    }

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
                    binding.registerEmailBox.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                //TODO: Add navigation functionality
                //navigateToLogin();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}