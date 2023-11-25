package com.example.mwcproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.mwcproject.Permission.LocationPermission;
import com.example.mwcproject.fragments.DownButtonFragment;
import com.example.mwcproject.fragments.Maps2Fragment;
import com.example.mwcproject.fragments.MapsFragment;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 1888;

    private LocationPermission localisationPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        localisationPermission = LocationPermission.getInstance(this, this);
        requestLocationPermissions();
        requestCameraPermission();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_map, Maps2Fragment.class, null)
                    .setReorderingAllowed(true)
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_buttons, DownButtonFragment.class, null)
                    .setReorderingAllowed(true)
                    .commit();
        }
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