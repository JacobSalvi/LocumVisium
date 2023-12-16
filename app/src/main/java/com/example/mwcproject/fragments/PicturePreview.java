package com.example.mwcproject.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mwcproject.R;
import com.example.mwcproject.databinding.PicturePreviewFragmentBinding;
import com.example.mwcproject.requests.RequestsHandler;
import com.example.mwcproject.services.Localisation.LocationService;
import com.example.mwcproject.utils.LocationUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PicturePreview extends Fragment  {

    private PicturePreviewFragmentBinding binding;
    private Bitmap imageBitmap;
    private final List<String> chosenTags = new ArrayList<>();
    private boolean isBound;
    Location location;
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            isBound = true;
            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            LocationService locationService = binder.getService();
            locationService.setLocalisationUpdateListener(new LocationService.LocationUpdateListener() {
                @Override
                public void onLocationChanged(Location location) {
                    UpdateLocation(location);
                }
            });
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

    private void UpdateLocation(Location location) {
        this.location = location;
    }

    @Override
    public void onStart() {
        super.onStart();
        Context context = getActivity();
        if (context != null) {
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = PicturePreviewFragmentBinding.inflate(inflater, container, false);
        TextInputEditText et = binding.getRoot().findViewById(R.id.description);

        createTags();

        Context ctx = getContext();
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("Failure");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                System.out.println("Success");
                int response_code = response.code();
                Looper.prepare();
                if (response_code != 200) {
                    Toast.makeText(ctx, "The server encountered an error, couldn't upload the image.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ctx, "Image sent correctly", Toast.LENGTH_SHORT).show();
                }
                Looper.loop();
            }
        };

        TextInputEditText inputTitle = binding.getRoot().findViewById(R.id.titleInput);
        binding.sendButton.setOnClickListener((view)->{
            String desc = et.getText().toString();
            String title = inputTitle.getText().toString();
            RequestsHandler.sendImage(imageBitmap, title, desc, chosenTags, location.getLongitude(), location.getLatitude(), location.getProvider(),callback, ctx);
            Fragment parent = getParentFragmentManager().findFragmentById(R.id.fragment_camera);
            getParentFragmentManager().beginTransaction().remove(parent).remove(this).commit();
        });
        setImageView(this.imageBitmap);
        location = LocationUtils.getLuganoLocation();
        return binding.getRoot();
    }

    private void createTags(){HorizontalScrollView tagsContainer = binding.getRoot().findViewById(R.id.tagsContainer);
        LinearLayout ll = new LinearLayout(this.getContext());
        tagsContainer.addView(ll);

        List<String> availableTags = Arrays.asList("Bar", "Restaurant", "Event", "CozyPlace","Park");

        for(String availableTag:  availableTags){
            Button b = new Button(this.getContext());
            b.setOnClickListener((view)->{
                if(!chosenTags.contains(availableTag)){
                    chosenTags.add(availableTag);
                    b.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                }else{
                    chosenTags.remove(availableTag);
                    b.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                }
            });
            b.setText(availableTag);
            ll.addView(b);
        }
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void setImageView(Bitmap bitmap){
        ImageView iv = binding.getRoot().findViewById(R.id.preview_image);
        iv.post(()-> iv.setImageBitmap(Bitmap.createScaledBitmap(bitmap, iv.getWidth(), iv.getHeight(), false)));
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        this.imageBitmap = StringToBitMap(bundle.getString("image"));
    }

    public PicturePreview(){

    }
}
