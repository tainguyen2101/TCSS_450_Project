package edu.uw.group1app.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pusher.pushnotifications.PushNotifications;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.group1app.R;
import edu.uw.group1app.databinding.FragmentHomeBinding;
import edu.uw.group1app.model.PushyTokenViewModel;
import edu.uw.group1app.model.UserInfoViewModel;
import edu.uw.group1app.ui.signin.SignInViewModel;
import edu.uw.group1app.ui.weather.CurrentWeatherViewModel;
import edu.uw.group1app.ui.weather.ZipcodeViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private CurrentWeatherViewModel mWeatherModel;
    private ZipcodeViewModel mZipModel;


    public HomeFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherModel = new ViewModelProvider(getActivity()).get(CurrentWeatherViewModel.class);
        mZipModel = new ViewModelProvider(getActivity()).get(ZipcodeViewModel.class);
        //mZipModel.connect("98374");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserInfoViewModel model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);
        FragmentHomeBinding.bind(getView()).emailLabel.setText("Hello " + model.getEmail());



        mZipModel.addResponseObserver(getViewLifecycleOwner(), response ->{
            try {

                //mWeatherModel.connect(response.getString("Key"));
                FragmentHomeBinding.bind(getView()).textViewHomeWeatherCity.setText(
                        response.getString("LocalizedName")
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        mWeatherModel.addResponseObserver(getViewLifecycleOwner(), response ->{
            try {
                FragmentHomeBinding.bind(getView()).textViewHomeWeatherCond.setText(
                        response.getString("WeatherText"));
                JSONObject temp = response.getJSONObject("Temperature");
                JSONObject type = temp.getJSONObject("Imperial");
                FragmentHomeBinding.bind(getView()).textViewHomeWeatherTemp.setText(
                        type.getString("Value"));
                FragmentHomeBinding.bind(getView()).textViewHomeWeatherUnit.setText("F");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}