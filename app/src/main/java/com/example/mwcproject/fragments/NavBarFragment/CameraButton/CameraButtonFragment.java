package com.example.mwcproject.fragments.NavBarFragment.CameraButton;
import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mwcproject.R;
import com.example.mwcproject.databinding.CameraButtonFragmentBinding;
import com.example.mwcproject.fragments.CameraFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CameraButtonFragment extends Fragment {


    public static CameraButtonFragment instance;
    boolean isCamera;
    CameraButtonFragmentBinding binding;

    FloatingActionButton camButton;


    CameraButtonListener callback;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment specificFragment = fragmentManager.findFragmentByTag("NavBarFragment");
        if (specificFragment instanceof CameraButtonListener) {
            callback = (CameraButtonListener) specificFragment;
        } else {
            throw new RuntimeException("NavBarFragment must implement CameraButtonListener");
        }
    }
    public Optional<CameraButtonListener> buttonListener = Optional.empty();





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
                    callback.OnButtonClick();
                    buttonListener.ifPresent(CameraButtonListener::OnButtonClick);
                    if(!buttonListener.isPresent()) {
                        getParentFragmentManager().popBackStack();
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.fragment_camera, CameraFragment.class, null)
                                .setReorderingAllowed(false)
                                .addToBackStack("Camera") // if you want to add it to the back stack
                                .commit();
                        OnChangeButton();
                    }
                }
        );
        instance = this;
        return binding.getRoot();
    }


    public static void setCameraBtnCallback(CameraButtonListener itself) {
        if(itself==null){
            instance.buttonListener = Optional.empty();
        }else{
            instance.buttonListener = Optional.of(itself);
        }
    }

    private void OnChangeButton() {

        Drawable drawable = camButton.getDrawable();
        if (drawable instanceof AnimatedVectorDrawable) {
            AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
            animatedVectorDrawable.start();
        }

    }

}
