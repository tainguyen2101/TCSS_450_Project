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
 * A simple {@link Fragment} subclass.
 */
public class PasswordChangingFragment extends Fragment {

    private FragmentPasswordChangingBinding binding;

    private PasswordChangingViewModel mPasswordModel;

    private String mEmail;

    private AlertDialog.Builder builder;

    private Map<Boolean, String> map;

    private PasswordValidator mEmailValidator =
            checkClientPredicate(email -> email.equals(binding.textChangeEmail.getText().toString()));

    private PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> !pwd.equals(binding.textChangeOldPass.getText().toString()))
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
        map = new HashMap<>();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PasswordChangingFragmentArgs args = PasswordChangingFragmentArgs.fromBundle(getArguments());

        mEmail = args.getEmail();

        binding.textChangeEmail.setText(mEmail);

        binding.buttonCancel.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        PasswordChangingFragmentDirections.actionNavigationPasswordChageToNavigationUserSetting()));

        binding.buttonSend.setOnClickListener(this::attemptChangePassword);

        mPasswordModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeChangingPasswordResponse
        );
    }

    private void attemptChangePassword(final View button) {
        validateEmail();
    }

    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(mEmail.trim()),
                this::validatePassword,
                result -> binding.textChangeEmail.setError("Please enter your own email."));
    }

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
            try {
                System.out.println(response.getBoolean("success"));
                System.out.println(response.getString("message"));
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