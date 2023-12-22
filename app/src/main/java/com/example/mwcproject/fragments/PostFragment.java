package com.example.mwcproject.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mwcproject.R;
import com.example.mwcproject.databinding.PostFragmentBinding;
import com.example.mwcproject.requests.RequestsHandler;


public class PostFragment extends Fragment {
    private PostFragmentBinding binding;

    public static PostFragment newInstance(final String title, final String description,
                                           final String imagePath) {
        PostFragment newPost = new PostFragment();
        Bundle b = new Bundle();
        b.putString("title", title);
        b.putString("description", description);
        b.putString("imagePath", imagePath);
        newPost.setArguments(b);
        return newPost;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PostFragmentBinding.inflate(inflater, container, false);
        final String title = getArguments().getString("title");
        final String desc = getArguments().getString("description");
        final String imagePath = getArguments().getString("imagePath");
        TextView titleView = binding.getRoot().findViewById(R.id.post_title);
        titleView.setText(title);
        loadImage(imagePath);
        TextView descView = binding.postDescription;
        descView.setText(desc);
        return binding.getRoot();
    }

    private void loadImage(final String imagePath) {
        Thread thread = new Thread(() -> {
            try {
                Bitmap image = RequestsHandler.getPicture(imagePath, getContext());
                getActivity().runOnUiThread(() -> {
                    ImageView iv = binding.getRoot().findViewById(R.id.post_image);
                    iv.setImageBitmap(image);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }
}

