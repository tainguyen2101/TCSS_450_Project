package edu.uw.tcss450.idledj;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import edu.uw.tcss450.idledj.databinding.ActivityMainBinding;
import edu.uw.tcss450.idledj.databinding.FragmentFirstBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}