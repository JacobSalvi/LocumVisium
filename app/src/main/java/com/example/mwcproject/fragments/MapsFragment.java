package com.example.mwcproject.fragments;

import androidx.annotation.NonNull;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mwcproject.R;
import com.example.mwcproject.services.LocationService;
import com.example.mwcproject.utils.LocationInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mwcproject.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;


public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private final int START_ZOOM = 15;
    private GoogleMap mMap;
    ActivityMapsBinding binding;
    private boolean isBound = false;
    private Marker userMaker;
    private final ServiceConnection connection = new ServiceConnection() {

    @Override
    public void onServiceConnected(ComponentName className, IBinder service) {
        LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
        LocationService locationService = binder.getService();

        // Assuming you have a method in LocationService to set a callback
        locationService.setLocalisationUpdateListener(new LocationService.LocationUpdateListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateMapLocation(location);
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


    public void updateMapLocation(Location location) {



        System.out.println("Update Location");
        if (location != null && mMap != null) {

            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            System.out.println(userLocation);

            if (userMaker == null) {
                userMaker = createUserMarker(userLocation);
            } else {
                userMaker.setPosition(userLocation);
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 5));
            //mMap.addMarker(new MarkerOptions().position(userLocation).title("Current Location"));
        }
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

        /*
        LatLng defaultLocation = manager.getLatLng();
        mMap.addMarker(new MarkerOptions().position(defaultLocation).title("Lugano"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, START_ZOOM));

        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this.getContext(), R.raw.map_style));


         */
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
