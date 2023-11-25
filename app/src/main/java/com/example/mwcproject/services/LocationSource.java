package com.example.mwcproject.services;

import android.location.Location;

import androidx.annotation.NonNull;

public class LocationSource implements com.google.android.gms.maps.LocationSource, LocationService.LocationUpdateListener {

    public Location getLastLocation;
    LocationService listener;
    public LocationSource(LocationService listener) {
        this.listener = listener;
    }
    private OnLocationChangedListener mListener;
    @Override
    public void activate(@NonNull OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }
    @Override
    public void deactivate() {
        mListener = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        getLastLocation = location;
        if (mListener != null) {
            mListener.onLocationChanged(location);
        }
    }
}
