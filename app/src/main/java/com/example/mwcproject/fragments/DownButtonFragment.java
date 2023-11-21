package com.example.mwcproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mwcproject.R;
import com.example.mwcproject.databinding.DownButtonFragmentBinding;

public class DownButtonFragment extends Fragment {

    DownButtonFragmentBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        binding = DownButtonFragmentBinding.inflate(inflater, container, false);
        binding.cameraBtn.setOnClickListener(view -> {
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_camera, CameraFragment.class, null)
                            .setReorderingAllowed(false)
                            .addToBackStack("Camera") // if you want to add it to the back stack
                            .commit();
                }
        );
        return binding.getRoot().findViewById(R.id.down_bts);
    }

    public DownButtonFragment() {
        super(R.layout.down_button_fragment);

    }

}
