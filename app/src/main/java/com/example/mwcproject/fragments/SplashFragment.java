package com.example.mwcproject.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.example.mwcproject.R;

public class SplashFragment extends Fragment {

    private RelativeLayout splashLayout;
    private static SplashFragment instance;

    public SplashFragment() {
        // Required empty public constructor
    }

    public static SplashFragment getInstance() {
        if (instance == null) {
            instance = new SplashFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.splash_screen_activity, container, false);
        splashLayout = view.findViewById(R.id.splash_layout);
        return view;
    }

    public void startTransition() {
        startFadeOutAnimation();
    }

    private void startFadeOutAnimation() {
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(5000); // Duration in milliseconds
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) { performTransitionAction(); }
            @Override
            public void onAnimationRepeat(Animation animation) { }
            @Override
            public void onAnimationStart(Animation animation) { }
        });

        splashLayout.startAnimation(fadeOut);
    }

    private void performTransitionAction() {
        // Transition logic after animation
    }
}