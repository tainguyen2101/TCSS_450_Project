package edu.uw.group1app.ui.userSetting;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import edu.uw.group1app.R;
import edu.uw.group1app.databinding.FragmentUserSettingBinding;
import edu.uw.group1app.model.UserInfoViewModel;
import edu.uw.group1app.ui.signin.SignInFragmentDirections;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserSettingFragment extends Fragment {

    private FragmentUserSettingBinding binding;
    private UserInfoViewModel mUserViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserSettingBinding.inflate(inflater);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserViewModel = provider.get(UserInfoViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.textViewUsername.setText(mUserViewModel.getmUserName());
        binding.textViewUserEmail.setText(mUserViewModel.getEmail());
        binding.buttonChangePass.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        UserSettingFragmentDirections.actionNavigationUserSettingToNavigationPasswordChage(mUserViewModel.getEmail())
                ));

        binding.switchDarkMode.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    if (isChecked) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                });
    }
}