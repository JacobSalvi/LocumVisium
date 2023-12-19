package com.example.mwcproject.fragments.NavBarFragment;
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
import com.example.mwcproject.databinding.NavBarFragmentBinding;
import com.example.mwcproject.fragments.NavBarFragment.CameraButton.CameraButtonFragment;
import com.example.mwcproject.fragments.NavBarFragment.CameraButton.CameraButtonListener;
import com.example.mwcproject.fragments.ScrollFeed;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class NavBarFragment extends Fragment implements CameraButtonListener {

    NavBarFragmentBinding binding;

    public static NavBarFragment instance;
    BottomNavigationView navbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        binding = NavBarFragmentBinding.inflate(inflater, container, false);

        //CameraButtonFragment cameraButtonFragment = new CameraButtonFragment();
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_camera_btn, CameraButtonFragment.class, null)
                .setReorderingAllowed(false)
                .commit();

        navbar = binding.bottomNavigationView;
        navbar.setSelectedItemId(R.id.map_btn);
        navbar.setOnItemSelectedListener(item -> {
             CameraButtonFragment.changeCamToButton();
             if (item.getItemId() == R.id.map_btn) {
                 getParentFragmentManager().popBackStack();
             }else if(item.getItemId() == R.id.scroll_feed_btn){
                 getParentFragmentManager().popBackStack();
                 getParentFragmentManager().beginTransaction()
                         .replace(R.id.scroll_feed, ScrollFeed.class, null)
                         .setReorderingAllowed(false)
                         .addToBackStack("ScrollFeed")
                         .commit();
             }
            return true;
        });
        instance = this;
        return binding.getRoot().findViewById(R.id.down_bts);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fragmentManager = getParentFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(this);
        fragmentTransaction.commit();
    }


    public static void ShowNavBar() {
        instance.showFragment();
    }

    public void showFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(
                R.anim.slide_in,
                R.anim.slide_out
        );
        fragmentTransaction.show(this);
        fragmentTransaction.commit();
    }


    public static void SetMapBtn() {
        instance.navbar.setSelectedItemId(R.id.map_btn);
    }


    @Override
    public void OnButtonClick() {
        navbar.setSelectedItemId(R.id.placeholder);
    }
}
