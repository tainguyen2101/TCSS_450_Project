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

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.group1app.R;
import edu.uw.group1app.databinding.FragmentCurrentWeatherBinding;
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
    private Context mContext;
    private ZipcodeDialog zipBinding;
    private LocationViewModel mLocationModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = new ViewModelProvider(getActivity()).get(CurrentWeatherViewModel.class);
        mZipModel = new ViewModelProvider(getActivity()).get(ZipcodeViewModel.class);
        mGeoModel = new ViewModelProvider(getActivity()).get(GeopositionViewModel.class);
        mLocationModel = new ViewModelProvider(getActivity()).get(LocationViewModel.class);
        //mGeoModel.connect("40.73","-74.00");

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

   private void observeGeoResponse(JSONObject response) {

        try{
            binding.textViewCity.setText(response.getString("LocalizedName"));
            mModel.connect(response.getString("Key"));
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

    //The current conditions are acquired from this method by the mModel.connect method
    private void observeZipResponse(final JSONObject response){
        try{
            binding.textViewCity.setText(response.getString("LocalizedName"));
            mModel.connect(response.getString("Key"));
        } catch (JSONException e) {

            Log.e("JSON Parse Error",e.getMessage());
        }
    }
}