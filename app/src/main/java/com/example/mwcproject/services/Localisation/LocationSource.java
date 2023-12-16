package com.example.mwcproject.services.Localisation;

import android.location.Location;

import androidx.annotation.NonNull;

public class LocationSource implements com.google.android.gms.maps.LocationSource, LocationService.LocationUpdateListener {

    public Location lastLocation;
    LocationService listener;
    public LocationSource() {

    }
    private OnLocationChangedListener mListener;
    @Override
    public void activate(@NonNull OnLocationChangedListener onLocationChangedListener) {
        System.out.println("CALLED");
        mListener = onLocationChangedListener;
    }
    @Override
    public void deactivate() {
        mListener = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        if (mListener != null) {
            mListener.onLocationChanged(location);
        }
    }
}
