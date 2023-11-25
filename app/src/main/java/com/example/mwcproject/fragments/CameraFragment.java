package com.example.mwcproject.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.example.mwcproject.R;
import com.example.mwcproject.databinding.CameraFragmentBinding;;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

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
        CameraFragment a = this;
        binding.captureButton.setOnClickListener((view) -> {
            binding.captureButton.setEnabled(false);
            imageCapture.takePicture(ContextCompat.getMainExecutor(this.getContext()),
                    new ImageCapture.OnImageCapturedCallback() {
                        @Override
                        public void onCaptureSuccess(@NonNull ImageProxy image) {
                            super.onCaptureSuccess(image);
                            System.out.println("image is " + image);
                            // image to string
                            ByteArrayOutputStream os = new ByteArrayOutputStream();
                            image.toBitmap().compress(Bitmap.CompressFormat.PNG, 100, os);
                            byte[] bytes = os.toByteArray();
                            String encoded = Base64.encodeToString(bytes, Base64.DEFAULT);
                            Bundle bundle = new Bundle();
                            bundle.putString("image", encoded);
                            getParentFragmentManager().beginTransaction()
                                    .replace(R.id.picture_description, PicturePreview.class, bundle)
                                    .setReorderingAllowed(true)
                                    .commit();
                        }

                        @Override
                        public void onError(@NonNull ImageCaptureException error) {
                            System.out.println("error is " + error);
                        }
                    });
        });
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.startCamera();

    }

    private void startCamera(){
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
        Preview preview = new Preview.Builder().build();
        CameraSelector cs = new CameraSelector.Builder().requireLensFacing(
                CameraSelector.LENS_FACING_BACK).build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        pv.bindToLifecycle(this, cs, preview, this.imageCapture);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
