package edu.uw.group1app.ui.weather;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.uw.group1app.R;
import edu.uw.group1app.databinding.FragmentWeatherBinding;
import edu.uw.group1app.databinding.ZipcodeDialogBinding;

/** Creates a pop up window asking user for a zip code.
 * @author Ivan Mendez
 */
public class ZipcodeDialog extends DialogFragment {

    private ZipcodeDialogBinding binding;
    private ZipcodeViewModel mZipModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mZipModel = new ViewModelProvider(getActivity()).get(ZipcodeViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mZipModel.connect(binding.editTextEnterZip.getText().toString());
                Log.d("BUTTON","clicked");
                dismiss();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = ZipcodeDialogBinding.inflate(inflater, container, false);

        return  binding.getRoot();
    }
}
