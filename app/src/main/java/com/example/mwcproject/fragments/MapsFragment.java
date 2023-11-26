package com.example.mwcproject.fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.mwcproject.Permission.AbstractPermission;
import com.example.mwcproject.Permission.LocationPermission;
import com.example.mwcproject.R;
import com.example.mwcproject.services.Localisation.LocationService;
import com.example.mwcproject.services.Localisation.LocationSource;
import com.example.mwcproject.utils.LocationUtils;
import com.example.mwcproject.utils.LocationInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.List;


public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback, AbstractPermission.PermissionListener {

    private static final int START_ZOOM = 15;
    private GoogleMap mMap;
    private boolean isBound = false;
    private Marker userMarker;
    private LocationPermission permission;
    private LocationSource source;

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            isBound = true;
            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            LocationService locationService = binder.getService();
            source = new LocationSource(locationService);
            locationService.setLocalisationUpdateListener(new LocationService.LocationUpdateListener() {
                @Override
                public void onLocationChanged(Location location) {
                    updateMapLocationOnce(location);
                    locationService.clearLocalisationUpdateListern();
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        Context context = getActivity();
        if (context != null) {
            permission = LocationPermission.getInstance(context, (AppCompatActivity) getActivity());
            permission.AddListener(this);
            // Bind to LocationService
            Intent intent = new Intent(context, LocationService.class);
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Context context = getActivity();
        if (isBound && context != null) {
            context.unbindService(connection);
            isBound = false;
        }
    }

    public void updateMapLocationOnce(Location location) {
        LatLng userLocation = location != null ? LocationUtils.locationToLatLng(location)
                : new LatLng(46.003601, 8.953620);
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, START_ZOOM));
        }
        updateLocalisationUI();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            getMapAsync(this);
        }
        return view;
    }

    private void updateLocalisationUI() {
        try {
            if (permission != null && permission.isEnabled()) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        } catch (SecurityException e) {
            Log.e("MapsFragment", "Security Exception: " + e.getMessage());
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.map_style));

        updateLocalisationUI();
    }

    @Override
    public void onPermissionChange() {
        updateLocalisationUI();
    }


    public void setPointsOnMap(List<LocationInfo> locations) {
        HashMap<String, String> idToPath = new HashMap<>();
        for (LocationInfo location : locations) {
            Marker m = mMap.addMarker(location);
            assert m != null;
            idToPath.put(m.getId(), location.getPath());
        }
        mMap.setOnMarkerClickListener(marker -> {
            // TODO do something with the marker
            String currentMarkingId = marker.getId();
            System.out.println(idToPath.get(currentMarkingId));
            return true; // should return false?????
        });
    }

}
