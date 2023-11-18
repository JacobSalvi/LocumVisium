package com.example.stepappv4.ui.Camera;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.example.stepappv4.R;
import com.example.stepappv4.databinding.CameraFragmentBinding;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.util.Base64;

public class CameraFragment extends Fragment  {
    private CameraFragmentBinding binding;
    private PreviewView previewView;
    private ImageCapture imageCapture;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = CameraFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        previewView = root.findViewById(R.id.previewView);
        binding.captureButton.setOnClickListener((view)-> imageCapture.takePicture(ContextCompat.getMainExecutor(this.getContext()),
                new ImageCapture.OnImageCapturedCallback() {
            @Override
            public void onCaptureSuccess(@NonNull ImageProxy image) {
                super.onCaptureSuccess(image);
                System.out.println("image is "+image);
                sendImage(image);
            }

            @Override
            public void onError(@NonNull ImageCaptureException error){
                System.out.println("error is "+error);
            }
        }));
        return root;
    }


    private void sendImage(ImageProxy image){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.toBitmap().compress(Bitmap.CompressFormat.PNG,100 ,os);
        byte[] bytes = os.toByteArray();
        String encoded = Base64.encodeToString(bytes, Base64.DEFAULT);
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("picture", encoded)
                .addFormDataPart("text","ciao come va again")
                .addFormDataPart("longitude","10.01")
                .addFormDataPart("latitude","15")
                .build();
        Request request = new Request.Builder()
                .url("http://10.21.17.0:3000/upload")
                .method("POST", body)
                .build();
        try {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    System.out.println("Failure");
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    System.out.println("Success");
                    response.code();
                }
            });
        } catch (Exception e){
            System.out.println("Error is"+e);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.startCamera();

    }

    private void startCamera(){
        System.out.println("startcamera");
        this.imageCapture =
                new ImageCapture.Builder()
                        .setTargetRotation(binding.getRoot().getDisplay().getRotation())
                        .build();
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =  ProcessCameraProvider.getInstance(this.getContext());
        cameraProviderFuture.addListener(()->{
            ProcessCameraProvider cameraProvider = null;
            try {
                cameraProvider = cameraProviderFuture.get();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            bindPreview(cameraProvider);
        }, ContextCompat.getMainExecutor(this.getContext()));

    }

    private void bindPreview(@NonNull ProcessCameraProvider pv){
        System.out.println("bindpreview");
        Preview preview = new Preview.Builder().build();
        CameraSelector cs = new CameraSelector.Builder().requireLensFacing(
                CameraSelector.LENS_FACING_BACK).build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        Camera camera = pv.bindToLifecycle(this, cs, preview, this.imageCapture);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
