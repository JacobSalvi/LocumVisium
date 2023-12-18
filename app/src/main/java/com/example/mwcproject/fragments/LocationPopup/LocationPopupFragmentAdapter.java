package com.example.mwcproject.fragments.LocationPopup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class LocationPopupFragmentAdapter extends FragmentStateAdapter {

    public LocationPopupFragmentAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                //return new ImageFragment();
            case 1:
                // Return other fragments for other pages
                // ...
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2; // Or more, depending on the number of pages you have
    }
}
