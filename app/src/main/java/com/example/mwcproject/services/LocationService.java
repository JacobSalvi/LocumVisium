package com.example.mwcproject.services;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import androidx.annotation.NonNull;
import com.example.mwcproject.Permission.AbstractPermission;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service implements AbstractPermission.PermissionListener {
    private final IBinder binder = new LocalBinder();
    private FusedLocationProviderClient fusedLocationClient;
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

    public void setLocalisationUpdateListener(LocationUpdateListener listener) {
        this.locationUpdateListener = listener;
    }

    public void clearLocalisationUpdateListern() {
        locationUpdateListener = null;
    }

    private void setupLocationListener() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000); // Update interval
        locationRequest.setFastestInterval(1000); // Fastest update interval
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location currentLocation = locationResult.getLastLocation();
                for (Location location : locationResult.getLocations()) {
                    if (location.hasAccuracy()) {
                        float accuracy = location.getAccuracy();
                        if (accuracy < currentLocation.getAccuracy()) {
                            currentLocation = location;
                        }
                    }
                }

                if (currentLocation != null) {
                    if (locationUpdateListener != null) {
                        locationUpdateListener.onLocationChanged(currentLocation);
                    }
                }
            }
        };

        startLocationUpdates(locationRequest);
    }


    @SuppressLint("MissingPermission")
    private void startLocationUpdates(LocationRequest locationRequest) {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }


    @Override
    public void onPermissionChange() {
       setupLocationListener();
    }

}

