package edu.uw.group1app.ui.weather;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.uw.group1app.R;

import edu.uw.group1app.databinding.FragmentWeatherBinding;


/**
 * A simple {@link Fragment} subclass that holds all the information for weather.
 * **NOTE**: The zip code view model takes in a zip code to acquire a location key
 *           via zip code and the city. That location key then is passed
 *           to the current view model to acquire the current weather conditions.
 *           The geoposition view model takes lat/lon coord to acquire a location key
 *           via geoposition and the city.That location key then is passed
 *  *        to the current view model to acquire the current weather conditions.
 *           Thank you Accuweather.
 * @author Ivan Mendez
 */
public class WeatherFragment extends Fragment {

    private CurrentWeatherViewModel mModel;
    private FragmentWeatherBinding binding;
    private ZipcodeViewModel mZipModel;
    private GeopositionViewModel mGeoModel;
    private ZipcodeDialog zipBinding;
    private LocationViewModel mLocationModel;
    private TwelveHourHomeViewModel mTwelveModel;
    private FiveDayHomeViewModel mFiveModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = new ViewModelProvider(getActivity()).get(CurrentWeatherViewModel.class);
        mZipModel = new ViewModelProvider(getActivity()).get(ZipcodeViewModel.class);
        mGeoModel = new ViewModelProvider(getActivity()).get(GeopositionViewModel.class);
        mLocationModel = new ViewModelProvider(getActivity()).get(LocationViewModel.class);
        mTwelveModel = new ViewModelProvider(getActivity()).get(TwelveHourHomeViewModel.class);
        mFiveModel = new ViewModelProvider(getActivity()).get(FiveDayHomeViewModel.class);
        //mGeoModel.connect("40.73","-74.00");
        mZipModel.connect("98374");
        mFiveModel.connect("41531_PC");


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

        mModel.addResponseObserver(getViewLifecycleOwner(), this::observeResponse);
        mZipModel.addResponseObserver(getViewLifecycleOwner(), this::observeZipResponse);
        mGeoModel.addResponseObserver(getViewLifecycleOwner(),this::observeGeoResponse);
        mTwelveModel.addResponseObserver(getViewLifecycleOwner(),this::observeTwelveHomeResponse);
        mFiveModel.addResponseObserver(getViewLifecycleOwner(),this::observeFiveHomeResponse);
        binding.layoutTwelvehourhome.setOnClickListener(v->{
            Navigation.findNavController(getView()).navigate(
                    R.id.action_navigation_weather_to_twelveHourListFragment);
        });
        binding.layoutFivedayhome.setOnClickListener(d->{
            Navigation.findNavController(getView()).navigate(
                    R.id.action_navigation_weather_to_fiveDayListFragment);
        });

        binding.imageButtonChangeLocation.setOnClickListener(v -> {
            PopupMenu menu = new PopupMenu(getActivity().getApplicationContext(),v);
            menu.getMenuInflater().inflate(R.menu.weather_change_location_menu, menu.getMenu());
           menu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.item_current_location:
                        mLocationModel.addLocationObserver(getViewLifecycleOwner(), location -> {

                            String lat = String.valueOf(location.getLatitude());
                            String lng = String.valueOf(location.getLongitude());
                            mGeoModel.connect(lat, lng);

                            Log.d("SUBMENU","" + lat + " " + lng);

                        });

                        return true;
                    case R.id.item_zipcode:
                        searchByZipCode();
                        return true;
                    case R.id.item_map_location:
                        Navigation.findNavController(getView()).navigate(
                                R.id.action_navigation_weather_to_locationFragment);
                            Log.d("SUBMENU","map location placeholder");
                    return true;

                    default:
                        return false;
                }
            });
            menu.show();
        });


    }

    private void observeFiveHomeResponse(JSONObject response) {
        Log.d("Five Response", response.toString());

        try{
            JSONArray array = response.getJSONArray("DailyForecasts");

            JSONObject data = array.getJSONObject(0);
            //TODO:
            binding.textViewDay1.setText("DAY1");
            JSONObject temp = data.getJSONObject("Temperature");
            JSONObject max = temp.getJSONObject("Maximum");
            binding.textViewDayHiTemp1.setText(max.getString("Value"));
            JSONObject min = temp.getJSONObject("Minimum");
            binding.textViewDayLoTemp1.setText(min.getString("Value"));

            JSONObject data2 = array.getJSONObject(1);
            //TODO:
            binding.textViewDay2.setText("DAY2");
            JSONObject temp2 = data2.getJSONObject("Temperature");
            JSONObject max2 = temp2.getJSONObject("Maximum");
            binding.textViewDayHiTemp2.setText(max2.getString("Value"));
            JSONObject min2 = temp2.getJSONObject("Minimum");
            binding.textViewDayLoTemp2.setText(min2.getString("Value"));

            JSONObject data3 = array.getJSONObject(2);
            //TODO:
            binding.textViewDay3.setText("DAY3");
            JSONObject temp3 = data3.getJSONObject("Temperature");
            JSONObject max3 = temp3.getJSONObject("Maximum");
            binding.textViewDayHiTemp3.setText(max3.getString("Value"));
            JSONObject min3 = temp3.getJSONObject("Minimum");
            binding.textViewDayLoTemp3.setText(min3.getString("Value"));

            JSONObject data4 = array.getJSONObject(3);
            //TODO:
            binding.textViewDay4.setText("DAY4");
            JSONObject temp4 = data4.getJSONObject("Temperature");
            JSONObject max4 = temp4.getJSONObject("Maximum");
            binding.textViewDayHiTemp4.setText(max4.getString("Value"));
            JSONObject min4 = temp4.getJSONObject("Minimum");
            binding.textViewDayLoTemp4.setText(min4.getString("Value"));

        } catch (JSONException e){
            Log.e("JSON Parse Error on 5", e.getMessage());
        }
    }

    private void observeTwelveHomeResponse(JSONObject response) {
        //TODO:
    }

    private void observeGeoResponse(JSONObject response) {

        try{
            binding.textViewCity.setText(response.getString("LocalizedName"));
            mModel.connect(response.getString("Key"));
            mFiveModel.connect(response.getString("Key"));
        }catch(JSONException e){
            Log.e("JSON Parse Error",e.getMessage());

        }
    }

    private void searchByZipCode() {
        ZipcodeDialog dialog = new ZipcodeDialog();
        dialog.show(getChildFragmentManager(),"hello");
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
            mFiveModel.connect(response.getString("Key"));
        } catch (JSONException e) {

            Log.e("JSON Parse Error",e.getMessage());
        }
    }
}