package edu.uw.group1app.ui.register;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.auth0.android.jwt.JWT;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.group1app.R;

import edu.uw.group1app.databinding.FragmentEmailVerificationBinding;
import edu.uw.group1app.model.PushyTokenViewModel;
import edu.uw.group1app.model.UserInfoViewModel;

import edu.uw.group1app.ui.utils.PasswordValidator;

/**
 * A simple {@link Fragment} subclass.

 */
public class EmailVerificationFragment extends Fragment {

    public EmailVerificationFragment() {
        // Required empty public constructor
    }
    private FragmentEmailVerificationBinding binding;
    //private EmailVerificationViewModel mEmailVerificationModel;
    private EmailVerificationFragmentArgs mArgs;
    private RegisterViewModel mRegisterModel;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mEmailVerificationModel = new ViewModelProvider(getActivity())
//                .get(EmailVerificationViewModel.class);
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegisterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEmailVerificationBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//
//        mEmailVerificationModel.addResponseObserver(
//                getViewLifecycleOwner(),
//                this::observeEmailVerificationResponse);
        binding.emailVerificationButton.setOnClickListener(this::registerWithServer);
        mRegisterModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeRegResponse);
            mArgs = EmailVerificationFragmentArgs.fromBundle(getArguments());
    }



//    private void verifyEmailWithServer() {
//        mEmailVerificationModel.connect(mArgs.getEmail());
//        //This is an Asynchronous call. No statements after should rely on the
//        //result of connect().
//    }

    /**
     * Helper to abstract the navigation to the sign in fragment.
     */
    private void navigateToSignIn() {

        Navigation.findNavController(getView())
                .navigate(EmailVerificationFragmentDirections
                        .actionEmailVerificationFragmentToSignInFragment());
    }


    /**asynchronous call to verify authentication with web service*/
    private void registerWithServer(final View Button) {

        mRegisterModel.connect(
                mArgs.getFname(),
                mArgs.getLname(),
                mArgs.getEmail(),
                mArgs.getPassword());

    }
    @Override
    public void onStart() {
        super.onStart();
    }
    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeRegResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    Log.d("Error code received",
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                navigateToSignIn();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}