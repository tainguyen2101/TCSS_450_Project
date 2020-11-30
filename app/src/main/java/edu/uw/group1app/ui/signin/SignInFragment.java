package edu.uw.group1app.ui.signin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.auth0.android.jwt.JWT;
import com.pusher.pushnotifications.PushNotifications;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.json.JSONException;
import org.json.JSONObject;


import edu.uw.group1app.R;
import edu.uw.group1app.databinding.FragmentSignInBinding;
import edu.uw.group1app.model.PushyTokenViewModel;
import edu.uw.group1app.model.UserInfoViewModel;
import edu.uw.group1app.ui.utils.PasswordValidator;

import static edu.uw.group1app.ui.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.group1app.ui.utils.PasswordValidator.checkPwdLength;
import static edu.uw.group1app.ui.utils.PasswordValidator.checkPwdSpecialChar;

/**
 * A simple {@link Fragment} subclass.
 * @author Ivan
 */
public class SignInFragment extends Fragment {

    private FragmentSignInBinding binding;
    private SignInViewModel mSignInModel;
    private PushyTokenViewModel mPushyTokenViewModel;
    private UserInfoViewModel mUserViewModel;


    private PasswordValidator mEmailValidator = PasswordValidator.checkPwdLength(2)
            .and(PasswordValidator.checkExcludeWhiteSpace())
            .and(PasswordValidator.checkPwdSpecialChar("@"));

    private PasswordValidator mPassWordValidator = PasswordValidator.checkPwdLength(1)
            .and(PasswordValidator.checkExcludeWhiteSpace());

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSignInModel = new ViewModelProvider(getActivity())
                .get(SignInViewModel.class);
        mPushyTokenViewModel = new ViewModelProvider(getActivity())
                .get(PushyTokenViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonRegister.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        SignInFragmentDirections.actionSignInFragmentToRegisterFragment()
                ));

        binding.buttonSignin.setOnClickListener(this::attemptSignIn);

        mSignInModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeSignInResponse);



        SignInFragmentArgs args = SignInFragmentArgs.fromBundle(getArguments());
        binding.editTextEmail.setText(args.getEmail().equals("default") ? "test12345@uw.edu" : args.getEmail());
        binding.editTextPassword.setText(args.getPassword().equals("default") ? "Test1234@" : args.getPassword());

        //don't allow sign in until pushy token retrieved
        mPushyTokenViewModel.addTokenObserver(getViewLifecycleOwner(), token ->
                binding.buttonSignin.setEnabled(!token.isEmpty()));

        mPushyTokenViewModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observePushyPutResponse);
    }

    private void attemptSignIn(final View button) {
        validateEmail();
    }

    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editTextEmail.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editTextEmail.setError("Please enter a valid Email address."));
    }

    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editTextPassword.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.editTextPassword.setError("Please enter a valid Password."));
    }

    private void verifyAuthWithServer() {
        mSignInModel.connect(
                binding.editTextEmail.getText().toString(),
                binding.editTextPassword.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }

    /**
     * Helper to abstract the navigation to the Activity past Authentication.
     * @param email users email
     * @param jwt the JSON Web Token supplied by the server
     */
    private void navigateToSuccess(final String email, final String jwt, final int memberid, final String username) {
        if (binding.switchSignin.isChecked()) {
            SharedPreferences prefs =
                    getActivity().getSharedPreferences(
                            getString(R.string.keys_shared_prefs),
                            Context.MODE_PRIVATE);
            //Store the credentials in SharedPrefs
            prefs.edit().putString(getString(R.string.keys_prefs_jwt), jwt).apply();
            prefs.edit().putString(getString(R.string.keys_prefs_email), email).apply();
            prefs.edit().putInt(getString(R.string.keys_prefs_memberid), memberid).apply();
        }

        Navigation.findNavController(getView())
                .navigate(SignInFragmentDirections
                        .actionSignInFragmentToMainActivity(email, jwt, memberid, username));
        //Remove THIS activity from the Task list. Pops off the backstack
        getActivity().finish();
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeSignInResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.editTextEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    mUserViewModel = new ViewModelProvider(getActivity(),
                            new UserInfoViewModel.UserInfoViewModelFactory(
                                    binding.editTextEmail.getText().toString(),
                                    response.getString("token"),
                                    response.getInt("memberid"),
                                    response.getString("username")
                            )).get(UserInfoViewModel.class);
                    sendPushyToken();
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }

    }

    /**
     * Helper to abstract the request to send the pushy token to the web service
     */
    private void sendPushyToken() {
        mPushyTokenViewModel.sendTokenToWebservice(mUserViewModel.getmJwt());
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to PushyTokenViewModel.
     *
     * @param response the Response from the server
     */
    private void observePushyPutResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                //this error cannot be fixed by the user changing credentials...
                binding.editTextEmail.setError(
                        "Error Authenticating on Push Token. Please contact support");
            } else {
                navigateToSuccess(
                        binding.editTextEmail.getText().toString(),
                        mUserViewModel.getmJwt(),
                        mUserViewModel.getmMeberId(),
                        mUserViewModel.getmUserName()
                );
            }
        }
    }
    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);

        if (prefs.contains(getString(R.string.keys_prefs_jwt))) {
            String token = prefs.getString(getString(R.string.keys_prefs_jwt), "");
            JWT jwt = new JWT(token);
            // Check to see if the web token is still valid or not. To make a JWT expire after a
            // longer or shorter time period, change the expiration time when the JWT is
            // created on the web service.
            if(!jwt.isExpired(0)) {
                String email = jwt.getClaim("email").asString();
                int memberid = jwt.getClaim("memberid").asInt();
                String username = jwt.getClaim("username").asString();
                navigateToSuccess(email, token, memberid, username);
                return;
            }
        }
    }
}

