package com.example.mwcproject.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mwcproject.Permission.LocationPermission;
import com.example.mwcproject.R;
import com.example.mwcproject.databinding.ScrollFeedBinding;
import com.example.mwcproject.requests.RequestsHandler;
import com.example.mwcproject.services.Localisation.LocationService;
import com.example.mwcproject.utils.LocationUtils;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScrollFeed extends Fragment {

    private ScrollFeedBinding binding;


    private boolean isBound = true;

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            isBound = true;
            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            LocationService locationService = binder.getService();
            locationService.setLocalisationUpdateListener(new LocationService.LocationUpdateListener() {
                @Override
                public void onLocationChanged(Location location) {
                    locationChange(location);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) { isBound = false; }
    };

    private LatLng userPosition = LocationUtils.locationToLatLng(LocationUtils.getLuganoLocation());

    private void locationChange(Location location) {
        userPosition = LocationUtils.locationToLatLng(location);
    }

    private List<Post> posts = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ScrollFeedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        Context context = getActivity();
        if (context != null) {
            // Bind to LocationService
            Intent intent = new Intent(context, LocationService.class);
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE);

            Thread thread = new Thread(() -> {
                try  {
                    // Your code goes here
                    JSONObject res = RequestsHandler.getLocationList(userPosition, 20000, getContext());
                    JSONArray data = res.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject postData = data.getJSONObject(i);
                        String title = postData.getString("title");
                        String description = postData.getString("description");
                        String imagePath = postData.getString("path");
                        posts.add(new Post(title,postData.getString("description")));
                        LinearLayout ll = binding.getRoot().findViewById(R.id.scroll_feed_ll);
                        getParentFragmentManager().beginTransaction().add(ll.getId(), PostFragment.newInstance(title, description, imagePath), title).commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
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

}


