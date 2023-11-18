package com.example.stepappv4.ui.DisplayPicture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.stepappv4.R;
import com.example.stepappv4.databinding.DisplayPictureBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DisplayPicture extends Fragment {

    private DisplayPictureBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DisplayPictureBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.getImageButton.setOnClickListener((view) -> {
            binding.getImageButton.setEnabled(false);
            getImage();
            new Handler().postDelayed(()-> binding.getImageButton.setEnabled(true), 2000);
        });

        return root;
    }

    private void getImage(){
        HttpUrl mySearchUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("10.21.17.0")
                .port(3000)
                .addPathSegment("location")
                .addQueryParameter("latitude", "15")
                .addQueryParameter("longitude", "10.01")
                .build();
        Request request = new Request.Builder()
                .url(mySearchUrl)
                .method("GET", null)
                .build();

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("Failure");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                System.out.println("Success");
                try {
                    String resString = response.body().string();
                    JSONObject resBody = new JSONObject(resString);
                    String encodedImage = (String) resBody.get("file");
                    Bitmap bitmap = StringToBitMap(encodedImage);
                    new Handler(Looper.getMainLooper()).post(
                            () -> setImageView(bitmap)
                    );

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void setImageView(Bitmap bitmap){
        ImageView iv = binding.getRoot().findViewById(R.id.display_image);
        iv.setImageBitmap(Bitmap.createScaledBitmap(bitmap, iv.getWidth(), iv.getHeight(), false));
//        iv.setBackgroundColor(Color.argb(255, 255, 0,0));
    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
