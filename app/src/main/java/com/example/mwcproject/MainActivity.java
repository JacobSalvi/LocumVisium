package com.example.mwcproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.example.mwcproject.Permission.LocationPermission;
import com.example.mwcproject.fragments.NavBarFragment.NavBarFragment;
import com.example.mwcproject.fragments.Map.MapsFragment;
import com.example.mwcproject.fragments.SplashFragment;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 1888;

    private LocationPermission localisationPermission;


    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        localisationPermission = LocationPermission.getInstance(this, this);
        requestLocationPermissions();
        requestCameraPermission();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_splash_screen, SplashFragment.getInstance())
                    .commit();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_map, MapsFragment.class, null)
                    .setReorderingAllowed(true)
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_buttons, NavBarFragment.class, null, "NavBarFragment")
                    .setReorderingAllowed(true)
                    .commit();
        }


        // Force Dark Theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);



    }

    private void requestLocationPermissions() {
       localisationPermission.queryForPermission();
    }
    private void requestCameraPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        localisationPermission.checkForEnabledPermission(requestCode, grantResults);
    }



}