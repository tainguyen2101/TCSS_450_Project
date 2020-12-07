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

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.group1app.R;
import edu.uw.group1app.databinding.FragmentCurrentWeatherBinding;
import edu.uw.group1app.databinding.FragmentWeatherBinding;
import edu.uw.group1app.databinding.ZipcodeDialogBinding;


/**
 * A simple {@link Fragment} subclass that holds all the information for weather.
 * @author Ivan Mendez
 */
public class WeatherFragment extends Fragment {

    private CurrentWeatherViewModel mModel;
    private FragmentWeatherBinding binding;
    private ZipcodeViewModel mZipModel;
    private Context mContext;
    private ZipcodeDialogBinding zipBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = new ViewModelProvider(getActivity()).get(CurrentWeatherViewModel.class);
        mZipModel = new ViewModelProvider(getActivity()).get(ZipcodeViewModel.class);
        //mZipModel.connect("78247");

    }

    private void findZipcode(String zipcode){
        mZipModel.connect(zipcode);
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

        binding.imageButtonChangeLocation.setOnClickListener(v -> {
            PopupMenu menu = new PopupMenu(getActivity().getApplicationContext(),v);
            menu.getMenuInflater().inflate(R.menu.weather_change_location_menu, menu.getMenu());
            menu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.item_current_location:
                        Log.d("SUBMENU","current location placeholder");
                        return true;
                    case R.id.item_zipcode:
                        searchByZipCode();
                        return true;
                    case R.id.item_map_location:
                            Log.d("SUBMENU","map location placeholder");
                    return true;

                    default:
                        return false;
                }
            });
            menu.show();
        });


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
        } catch (JSONException e) {

            Log.e("JSON Parse Error",e.getMessage());
        }
    }



}