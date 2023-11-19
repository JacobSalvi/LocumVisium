package com.example.mwcproject.services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service {
    private final IBinder binder = new LocalBinder();
    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;
    private LocationUpdateListener locationUpdateListener;
    private LocationCallback locationCallback;

    public class LocalBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setupLocationListener();
    }

    public interface LocationUpdateListener {
        void onLocationChanged(Location location);
    }

    public void setLocationUpdateListener(LocationUpdateListener listener) {
        this.locationUpdateListener = listener;
    }

    private void setupLocationListener() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000); // Update interval
        locationRequest.setFastestInterval(5000); // Fastest update interval
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    if (location.hasAccuracy()) {
                        float accuracy = location.getAccuracy();
                        if (currentLocation == null || accuracy < currentLocation.getAccuracy()) {
                            currentLocation = location;
                            if (locationUpdateListener != null) {
                                locationUpdateListener.onLocationChanged(location);
                            }
                        }
                    }
                }
            }
        };

        startLocationUpdates(locationRequest);
    }

    private void startLocationUpdates(LocationRequest locationRequest) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }



}

