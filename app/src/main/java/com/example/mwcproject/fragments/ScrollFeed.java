package com.example.mwcproject.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mwcproject.databinding.ScrollFeedBinding;
import com.example.mwcproject.requests.RequestsHandler;
import com.example.mwcproject.services.Localisation.LocationService;
import com.example.mwcproject.utils.LocationUtils;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ScrollFeed extends Fragment {

    private ScrollFeedBinding binding;
    private boolean isBound = false;
    private boolean isFetchingLocationData = false;
    private LocationService locationService;
    private LatLng userPosition = LocationUtils.locationToLatLng(LocationUtils.getLuganoLocation());

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            isBound = true;
            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            locationService = binder.getService();
            locationService.setLocalisationUpdateListener(ScrollFeed.this::locationChange);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
            locationService = null;
        }
    };

    private void locationChange(Location location) {
        userPosition = LocationUtils.locationToLatLng(location);
        fetchLocationData();
    }

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
            Intent intent = new Intent(context, LocationService.class);
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }
    }

    private void fetchLocationData() {
        if (!isFetchingLocationData && userPosition != null) {
            isFetchingLocationData = true;
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            executor.execute(() -> {
                try {
                    JSONObject res = RequestsHandler.getLocationList(userPosition, 1000, getContext());
                    handler.post(() -> {
                        try {
                            updateUIWithPosts(res);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            isFetchingLocationData = false;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(() -> isFetchingLocationData = false);
                }
            });
        }
    }

    private void updateUIWithPosts(JSONObject res) throws JSONException {
        JSONArray data = res.getJSONArray("data");
        getActivity().runOnUiThread(() -> {
            for (int i = 0; i < data.length(); i++) {
                try {
                    JSONObject postData = data.getJSONObject(i);
                    String title = postData.getString("title");
                    String description = postData.getString("description");
                    String imagePath = postData.getString("path");
                    addPostFragment(title, description, imagePath);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addPostFragment(String title, String description, String imagePath) {
        PostFragment postFragment = PostFragment.newInstance(title, description, imagePath);
        getChildFragmentManager().beginTransaction()
                .add(binding.scrollFeedLl.getId(), postFragment, title)
                .commit();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isBound) {
            Context context = getActivity();
            if (context != null) {
                context.unbindService(connection);
                isBound = false;
            }
        }
    }
}


