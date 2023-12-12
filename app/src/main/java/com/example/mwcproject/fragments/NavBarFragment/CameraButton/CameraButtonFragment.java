package com.example.mwcproject.fragments.NavBarFragment.CameraButton;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mwcproject.R;
import com.example.mwcproject.databinding.CameraButtonFragmentBinding;
import com.example.mwcproject.fragments.CameraFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CameraButtonFragment extends Fragment {


    public static CameraButtonFragment instance;
    boolean isCamera;
    CameraButtonFragmentBinding binding;

    FloatingActionButton camButton;


    public List<CameraButtonListener> buttonListeners;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        isCamera = false;
        super.onCreateView(inflater, container, savedInstanceState);
        binding =  CameraButtonFragmentBinding.inflate(inflater, container, false);

        camButton = binding.getRoot().findViewById(R.id.camera_btn);
        binding.cameraBtn.setOnClickListener(view -> {
                    buttonListeners.forEach(CameraButtonListener::OnButtonClick);
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_camera, CameraFragment.class, null)
                            .setReorderingAllowed(false)
                            .addToBackStack("Camera") // if you want to add it to the back stack
                            .commit();
                    OnChangeButton();
                }
        );
        instance = this;
        buttonListeners = new ArrayList<>();
        return binding.getRoot();
    }


    public static void AddToList(CameraButtonListener itself) {
        instance.buttonListeners = new ArrayList<>();
        instance.buttonListeners.add(itself);
    }

    private void OnChangeButton() {

        Drawable drawable = camButton.getDrawable();
        if (drawable instanceof AnimatedVectorDrawable) {
            AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
            animatedVectorDrawable.start();
        }

    }

}
