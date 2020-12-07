package edu.uw.group1app.ui.weather;

import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.group1app.R;
import edu.uw.group1app.databinding.FragmentCurrentWeatherBinding;
import edu.uw.group1app.databinding.FragmentWeatherBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    private CurrentWeatherViewModel mModel;
    private FragmentWeatherBinding binding;
    private ZipcodeViewModel mZipModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = new ViewModelProvider(getActivity()).get(CurrentWeatherViewModel.class);
        mZipModel = new ViewModelProvider(getActivity()).get(ZipcodeViewModel.class);
        //mModel.connect();
        mZipModel.connect("78247");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        return  binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        //FragmentWeatherBinding binding = FragmentWeatherBinding.bind(getView());

        mModel.addResponseObserver(getViewLifecycleOwner(), this::observeResponse);
        mZipModel.addResponseObserver(getViewLifecycleOwner(), this::observeZipResponse);


    }

   private void observeResponse(final JSONObject response){

        try {
            binding.textViewStatus.setText(response.getString("WeatherText"));

            JSONObject temp = response.getJSONObject("Temperature");
            JSONObject type = temp.getJSONObject("Imperial");
            binding.textViewTemperature.setText(type.getString("Value"));

        } catch(JSONException e){
            Log.e("JSON Parse Error",e.getMessage());
        }

    }

    private void observeZipResponse(final JSONObject response){
        try{
            binding.textViewCity.setText(response.getString("LocalizedName"));
            mModel.connect(response.getString("Key"));
        } catch (JSONException e) {

            Log.e("JSON Parse Error",e.getMessage());
        }
    }



}