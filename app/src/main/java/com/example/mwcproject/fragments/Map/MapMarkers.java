package com.example.mwcproject.fragments.Map;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import androidx.fragment.app.FragmentManager;
import com.example.mwcproject.R;
import com.example.mwcproject.fragments.LocationPopupFragment;
import com.example.mwcproject.requests.RequestsHandler;
import com.example.mwcproject.services.Localisation.LocationService;
import com.example.mwcproject.utils.LocationMarker;
import com.example.mwcproject.utils.LocationUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class MapMarkers  {
    private static final int START_ZOOM = 15;
    public static MapMarkers instance;
    private static final int RANGE = 20000;
    private final Context context;
    private final GoogleMap mMap;
    private final FragmentManager fragmentManager;
    boolean isBound;

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            isBound = true;
            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            LocationService locationService = binder.getService();
            locationService.setLocalisationUpdateListener(new LocationService.LocationUpdateListener() {
                @Override
                public void onLocationChanged(Location location) {
                    onLocationChange(location);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) { isBound = false; }
    };
    public MapMarkers(GoogleMap map, Context context, FragmentManager fragmentManager,  LatLng userLocation) {
        this.mMap = map;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.userLocation = userLocation;
        Intent intent = new Intent(context, LocationService.class);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        handler.post(runnableCode);
        instance = this;
    }
    LatLng userLocation;
    public void onLocationChange(Location location) {
        userLocation = LocationUtils.locationToLatLng(location); }

    private Handler handler = new Handler();
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            // Your code here
            updateMarkers();
            // Repeat this runnable code block again every 30 seconds
            handler.postDelayed(this, 5000);
        }
    };

    public static void updateMarkers() { instance.getLocations(instance.userLocation);}

    private class FetchLocationsTask extends AsyncTask<LatLng, Void, JSONObject> {
        private Exception exception = null;

        @Override
        protected JSONObject doInBackground(LatLng... params) {
            try {
                return RequestsHandler.getLocationList(params[0], RANGE, context);
            } catch (Exception e) {
                exception = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (exception != null) {
                System.out.println("Network request failed: " + exception.getMessage());
            }
            if (result != null) {
                System.out.println(result);
            }
        }
    }



    public void getLocations(LatLng userPosition) {
        if (userPosition != null) {
            mMap.clear();
            new FetchLocationsTask().execute(userPosition);
        } else {
            System.out.println("Null position");
        }
    }

    public void setPointsOnMap(List<LocationMarker> locations) {
        HashMap<String, String> idToPath = new HashMap<>();
        for (LocationMarker location : locations) {
            Marker m = mMap.addMarker(location);
            assert m != null;
            idToPath.put(m.getId(), location.getPath());
        }

        mMap.setOnMarkerClickListener(marker -> {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getMakerLocationsWithOffset(marker.getPosition()), START_ZOOM));

            Bundle args = new Bundle();
            args.putString("ID", marker.getTitle()); // Use the marker ID to get the path

            // Create a new instance of the fragment with the arguments
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_location_popup, LocationPopupFragment.class, args)
                    .addToBackStack("Position photo")
                    .setReorderingAllowed(true)
                    .commit();

            return true; // or return false if you want the default behavior as well
        });
    }


    private static final float Y_OFFSET_MARKER = 0.006f;

    private LatLng getMakerLocationsWithOffset(LatLng markerLocation) {
        return  new LatLng(markerLocation.latitude - Y_OFFSET_MARKER, markerLocation.longitude);
    }


}
