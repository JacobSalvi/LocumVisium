package com.example.mwcproject.fragments.maps.maps;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mwcproject.R;
import com.example.mwcproject.services.LocationListener;
import com.example.mwcproject.services.LocationManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mwcproject.databinding.ActivityMapsBinding;

public class MapsFragment extends Fragment implements OnMapReadyCallback, LocationListener {


    private final int START_ZOOM = 15;
    private GoogleMap mMap;
    //private FragmentMapsBinding binding;
    ActivityMapsBinding binding;

    LocationManager manager;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        manager = LocationManager.getInstance();
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear(); // clear all markers

        LatLng defaultLocation = manager.getLatLng();
        mMap.addMarker(new MarkerOptions().position(defaultLocation).title("Lugano"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, START_ZOOM));

        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this.getContext(), R.raw.map_style));
    }

    @Override
    public void OnLocationChanged(Location location) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
    }
}
