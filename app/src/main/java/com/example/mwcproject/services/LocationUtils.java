package com.example.mwcproject.services;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class LocationUtils {

    public static LatLng locationToLatLng(Location location) {


        return new LatLng(location.getLatitude(), location.getLongitude());
    }
}
