package com.example.mwcproject.fragments.NavBarFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mwcproject.R;
import com.example.mwcproject.databinding.NavBarFragmentBinding;


public class NavBarFragment extends Fragment {

    NavBarFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        binding = NavBarFragmentBinding.inflate(inflater, container, false);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_camera_btn, CameraButtonFragment.class, null)
                .setReorderingAllowed(false)
                .commit();

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



    public NavBarFragment() {
       // super(R.layout.down_button_fragment);
    }


}
