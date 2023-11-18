package com.example.mwcproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mwcproject.databinding.ActivityMainBinding;
import com.example.mwcproject.fragments.maps.DownButtons.DownButtonFragment;
import com.example.mwcproject.fragments.maps.maps.MapsFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_map, MapsFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null) // if you want to add it to the back stack
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_buttons, DownButtonFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null) // if you want to add it to the back stack
                    .commit();
        }
    }
}