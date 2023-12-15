package com.example.mwcproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mwcproject.R;
import com.example.mwcproject.databinding.ScrollFeedBinding;
import com.example.mwcproject.requests.RequestsHandler;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScrollFeed extends Fragment {

    private ScrollFeedBinding binding;

    private List<Post> posts = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ScrollFeedBinding.inflate(inflater, container, false);
        Thread thread = new Thread(() -> {
            try  {
                // Your code goes here
                JSONObject res = RequestsHandler.getLocationList(new LatLng(0,0), 100, getContext());
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

        return binding.getRoot();
    }

    public ScrollFeed(){

    }

}


