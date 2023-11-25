package com.example.mwcproject.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mwcproject.R;
import com.example.mwcproject.databinding.DisplayPictureBinding;

import com.example.mwcproject.requests.RequestsHandler;

public class DisplayPicture extends Fragment {

    private DisplayPictureBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DisplayPictureBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.getImageButton.setOnClickListener((view) -> {
            binding.getImageButton.setEnabled(false);
            try {

            Bitmap bitmap = RequestsHandler.getImage(this.getContext(), "15", "10.01");
            setImageView(bitmap);
            } catch (Exception e){
                // TODO:
            }
            new Handler().postDelayed(()-> binding.getImageButton.setEnabled(true), 2000);
        });

        return root;
    }

    private void setImageView(Bitmap bitmap){
        ImageView iv = binding.getRoot().findViewById(R.id.display_image);
        iv.setImageBitmap(Bitmap.createScaledBitmap(bitmap, iv.getWidth(), iv.getHeight(), false));
    }
}
