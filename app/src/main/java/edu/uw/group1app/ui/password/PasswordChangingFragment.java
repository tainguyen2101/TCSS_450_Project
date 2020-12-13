package edu.uw.group1app.ui.password;

import android.app.AlertDialog;
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
 * this class provides a function that a user can change his/her password
 *
 * @author Gyubeom Kim
 * @version 2.0
 */

public class PasswordChangingFragment extends Fragment {

    /**
     * binding for the class
     */
    private FragmentPasswordChangingBinding binding;

    /**
     * view model for the class
     */
    private PasswordChangingViewModel mPasswordModel;

    /**
     * a user's email
     */
    private String mEmail;

    /**
     * builder for creating dialog
     */
    private AlertDialog.Builder builder;

    /**
     * map for saving responses
     */
    private Map<Boolean, String> map;

    // a user should use the email that s/he logged in
    private PasswordValidator mEmailValidator =
            checkClientPredicate(email -> email.equals(binding.textChangeEmail.getText().toString()));

    // new password should not same as old one
    // more than 7 characters
    // at least 1 special character
    // at least 1 digit
    // at least 1 upper and lower case letter
    private PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> !pwd.equals(binding.textChangeOldPass.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase().or(checkPwdUpperCase()));

    /**
     * Required empty public constructor
     */
    public PasswordChangingFragment() {

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
        map = new HashMap<>();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PasswordChangingFragmentArgs args = PasswordChangingFragmentArgs.fromBundle(getArguments());

        mEmail = args.getEmail();

        //the email is filled with the email you typed
        binding.textChangeEmail.setText(mEmail);

        //the cancel button
        binding.buttonCancel.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        PasswordChangingFragmentDirections.actionNavigationPasswordChageToNavigationUserSetting()));

        //send button
        binding.buttonSend.setOnClickListener(this::attemptChangePassword);

        //add response observer
        mPasswordModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeChangingPasswordResponse
        );
    }

    /**
     * it validates all the restriction for email and password.
     * Once, a user types correct inputs. it it will send request to
     * the server and changes the password.
     *
     * @param button representing button for send
     */
    private void attemptChangePassword(final View button) {
        validateEmail();
    }

    /*
     * a user should use the email that s/he logged in
     */
    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(mEmail.trim()),
                this::validatePassword,
                result -> binding.textChangeEmail.setError("Please enter your own email."));
    }

    /**
     *  new password should not same as old one
     *  more than 7 characters
     *  at least 1 special character
     *  at least 1 digit
     *  at least 1 upper and lower case lette
     */
    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.textChageNewPass.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.textChageNewPass.setError("1) Should not be same as old password\n" +
                        "2) More Than 7 characters\n" +
                        "3) At least one lowercase letter\n" +
                        "4) At least one number digit\n" +
                        "5) At least one special character\n" +
                        "6) At least one uppercase letter"));
    }

    /**
     * getting respons from server
     *
     * @param response representing the response for the server side
     */
    private void observeChangingPasswordResponse(final JSONObject response) {
        if (response.length() > 0) {
            try {
                //openDialog(response.getBoolean("success"), response.getString("message"));

                if(!map.containsKey(response.getBoolean("success"))) {
                    map.put(response.getBoolean("success"), response.getString("message"));
                    openDialog(response.getBoolean("success"), map.get(response.getBoolean("success")));
                } else {
                    map.clear();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }

    }

    private void verifyAuthWithServer() {
        mPasswordModel.changePassword(
                binding.textChangeEmail.getText().toString(),
                binding.textChangeOldPass.getText().toString(),
                binding.textChageNewPass.getText().toString());
    }

    /***
     * it creates a dialog corresponding to the status of password changing
     *
     * @param isSuccess representing whether the password is changed or not
     * @param theMessage representing message tha tells whether the password is changed or not
     */
    private void openDialog(boolean isSuccess, String theMessage) {
        builder = new AlertDialog.Builder(getActivity());
        String theTitle = "";
        if(isSuccess) {
            theTitle = "Success!";
        } else {
            theTitle = "Failed!";
        }
        builder.setTitle(theTitle);
        builder.setMessage(theMessage);
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.create();
        builder.show();
    }
}