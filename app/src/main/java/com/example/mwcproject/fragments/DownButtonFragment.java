package com.example.mwcproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mwcproject.R;
import com.example.mwcproject.databinding.NavBarBinding;


public class DownButtonFragment extends Fragment {

    NavBarBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        binding = NavBarBinding.inflate(inflater, container, false);
        binding.cameraBtn.setOnClickListener(view -> {
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_camera, CameraFragment.class, null)
                            .setReorderingAllowed(false)
                            .addToBackStack("Camera") // if you want to add it to the back stack
                            .commit();
                }
        );


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
             if (item.getItemId() == R.id.map_btn) {
                 getParentFragmentManager().popBackStack();
             }
            return true;
        });
        /*
        binding..setOnClickListener(view -> {

        });*/
        return binding.getRoot().findViewById(R.id.down_bts);
    }



    public DownButtonFragment() {
       // super(R.layout.down_button_fragment);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}
