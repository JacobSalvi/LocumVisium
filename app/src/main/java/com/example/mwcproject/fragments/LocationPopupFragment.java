package com.example.mwcproject.fragments;
import static com.example.mwcproject.requests.RequestsHandler.StringToBitMap;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mwcproject.R;
import com.example.mwcproject.databinding.LocationPopupFragmentBinding;
import com.example.mwcproject.requests.RequestsHandler;
import com.example.mwcproject.utils.LocationUtils;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocationPopupFragment extends Fragment {

    private String lat;
    private String lng;
    private TextView title;
    private ImageView image;
    private LocationPopupFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = LocationPopupFragmentBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.location_popup_fragment, container, false);

        // Set up touch listener to consume events
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }

        });

        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        this.title = binding.titleText;
        this.image = binding.imageViewPopupFragment;
        binding.closeBtn.setOnClickListener((x) ->  {
            getParentFragmentManager().popBackStack();
        });

        // Set up touch listener to consume events
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Consume the touch event
                return true;
            }
        });
    }


    private void fetchLocationData()  {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            try {
                final JSONObject result =  RequestsHandler.getMarkerData(this.getContext(), lat, lng);
                handler.post(() -> {
                    SetTheData(result);
                });
            } catch (Exception e) {
                handler.post(() -> System.out.println("Network request failed: " + e.getMessage()));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            lat = String.valueOf(getArguments().getDouble("Lat"));
            lng = String.valueOf(getArguments().getDouble("Lng"));
        }
        fetchLocationData();
    }

    private void SetTheData(JSONObject res) {
        try {
            // Accessing the "data" object
            JSONObject data = res.getJSONObject("data");

            // Extracting fields from data
            String description = data.optString("description", "No description provided");
            JSONObject location = data.optJSONObject("location");
            double latitude = 0.0;
            double longitude = 0.0;
            if (location != null) {
                JSONArray coordinates = location.optJSONArray("coordinates");
                if (coordinates != null) {
                    latitude = coordinates.optDouble(0, 0.0);
                    longitude = coordinates.optDouble(1, 0.0);
                }
                String type = location.optString("type", "No type provided");
            }
            String path = data.optString("path", "No path provided");
            String provider = data.optString("provider", "No provider provided");
            JSONArray tags = data.optJSONArray("tags");
            String titleData = data.optString("title", "No title provided");

            // Process tags if needed
            if (tags != null) {
                for (int i = 0; i < tags.length(); i++) {
                    String tag = tags.getString(i);
                    System.out.println("Tag: " + tag);
                }
            }

            String encodedImage = (String) res.get("file");
            Bitmap bitmap = StringToBitMap(encodedImage);
            title.setText(titleData);
            image.setImageBitmap(bitmap);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
