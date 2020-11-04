package edu.uw.group1app.ui.signin;

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

import edu.uw.group1app.R;
import edu.uw.group1app.databinding.FragmentSignInBinding;

/**
 * A simple {@link Fragment} subclass.

 */
public class SignInFragment extends Fragment implements View.OnClickListener {



    private FragmentSignInBinding binding;
    private SignInViewModel mSignInModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSignInModel = new ViewModelProvider(getActivity())
                .get(SignInViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentSignInBinding.inflate(inflater, container, false);
        return binding.getRoot();



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);


        binding.buttonRegister1.setOnClickListener(this);
        binding.buttonSignin1.setOnClickListener(this);
        mSignInModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeResponse);

        SignInFragmentArgs args = SignInFragmentArgs.fromBundle(getArguments());
        binding.editTextEmail.setText(args.getEmail().equals("default") ? "" : args.getEmail());
        binding.editTextPassword.setText(args.getPassword().equals("default") ? "" : args.getPassword());



    }


    @Override
    public void onClick(View v) {

        String email = binding.editTextEmail.getText().toString();
        String password = binding.editTextPassword.getText().toString();


        if (v == binding.buttonSignin1) {

            if (email.isEmpty()){
                binding.editTextEmail.setError("Email was left blank");
            }

            if (password.isEmpty()){
                binding.editTextPassword.setError("Password was left blank");
            }


            if(!email.isEmpty() && !email.contains("@")){
                binding.editTextEmail.setError("Email needs a '@'");
            }

            if(!email.isEmpty()&& !password.isEmpty() && email.contains("@")){
                verifyAuthWithServer();

            }


        }

        if(v == binding.buttonRegister1){
            Navigation.findNavController(getView()).
                    navigate(R.id.action_signInFragment_to_registerFragment);
        }
    }



    private void signIn(String email){
        Log.d("SignInFragment", "SignIn()");
        FragmentSignInBinding binding = FragmentSignInBinding.bind(getView());

        Navigation.findNavController(getView()).navigate(
                SignInFragmentDirections
                        .actionSignInFragmentToMainActivity(
                                binding.editTextEmail.getText().toString(),""
                        ));

        getActivity().finish();

    }

    private void navigateToSuccess(final String email, final String jwt) {
        Navigation.findNavController(getView())
                .navigate(SignInFragmentDirections
                        .actionSignInFragmentToMainActivity(email, jwt));

        getActivity().finish();
    }

    private void verifyAuthWithServer() {
        mSignInModel.connect(
                binding.editTextEmail.getText().toString(),
                binding.editTextPassword.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().


    }

    private void observeResponse(final JSONObject response) {
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
                    navigateToSuccess(
                            binding.editTextEmail.getText().toString(),
                            response.getString("token")
                    );
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }


    }
}