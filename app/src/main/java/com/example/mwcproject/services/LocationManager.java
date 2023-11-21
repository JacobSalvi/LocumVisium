package com.example.mwcproject.services;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public final class LocationManager {
    private static LocationManager instance;
    private Location location;
    List<LocationListener> listeners;

    private LocationManager() {
        location = new Location("UserLocation");
        // Lugano Coordinate optimization
        location.setLongitude(8.96004);
        location.setLatitude(46.01008);
        listeners = new ArrayList<>();
    }

    public static synchronized LocationManager getInstance() {
        if (instance == null) {
            instance = new LocationManager();
        }
        return instance;
    }

    public void subscribeToOnLocationChange(LocationListener itself) {
        listeners.add(itself);
    }

    public Location getLocation() {
        return location ;
    }

    public LatLng getLatLng() {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public void setLocation(Location location) {
        this.location = location;
        for (LocationListener listener : listeners) {
            listener.OnLocationChanged(this.location);
        }
    }

}
