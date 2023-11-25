package com.example.mwcproject.fragments;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
import com.example.mwcproject.services.LocationService;
import com.example.mwcproject.services.LocationSource;
import com.example.mwcproject.services.LocationUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mwcproject.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.Objects;

public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback, AbstractPermission.PermissionListener {

    private final int START_ZOOM = 15;
    private GoogleMap mMap;
    ActivityMapsBinding binding;
    private boolean isBound = false;
    private Marker userMaker;
    LocationPermission permission;

    LocationSource source;

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
        permission = LocationPermission.getInstance(getContext(), (AppCompatActivity) getActivity());
        permission.AddListener(this);
        // Bind to LocationService
        Intent intent = new Intent(getActivity(), LocationService.class);
        getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (isBound) {
            getActivity().unbindService(connection);
            isBound = false;
        }
    }

    public void updateMapLocationOnce(Location location) {
        mMap.setLocationSource(source);
        LatLng userLocation = null;
        if (location != null) {
            userLocation = LocationUtils.locationToLatLng(location);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, START_ZOOM));

        } else  {
            userLocation = new LatLng(46.003601, 8.953620);
            mMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(userLocation, START_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
        updateLocalisationUI();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = ActivityMapsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private Marker createUserMarker(LatLng position) {

        int height = 100;
        int width = 100;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Draw a circle
        Paint paint = new Paint();
        paint.setColor(Color.BLUE); // Set the color of the marker
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 4, height / 4, width / 4, paint);

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);

        return mMap.addMarker(new MarkerOptions()
                .position(position)
                .title("Your Location")
                .icon(bitmapDescriptor));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear(); // clear all markers
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this.getContext(), R.raw.map_style));
    }

    private void updateLocalisationUI() {
        try {
            if (permission.isEnabled()) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", Objects.requireNonNull(e.getMessage()));
        }
    }


    @Override
    public void onPermissionChange() {
        updateLocalisationUI();
    }
}
