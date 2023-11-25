package com.example.mwcproject.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mwcproject.R;
import com.example.mwcproject.databinding.PicturePreviewFragmentBinding;
import com.example.mwcproject.requests.RequestsHandler;

import java.io.IOException;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PicturePreview extends Fragment {

    private PicturePreviewFragmentBinding binding;
    private Bitmap imageBitmap;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = PicturePreviewFragmentBinding.inflate(inflater, container, false);
        EditText et = binding.description;

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

        binding.sendButton.setOnClickListener((view)->{
            String desc = et.getText().toString();
            RequestsHandler.sendImage(imageBitmap, desc, callback, ctx);
            Fragment parent = getParentFragmentManager().findFragmentById(R.id.fragment_camera);
            getParentFragmentManager().beginTransaction().remove(parent).remove(this).commit();
        });
        setImageView(this.imageBitmap);

        return binding.getRoot();
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
