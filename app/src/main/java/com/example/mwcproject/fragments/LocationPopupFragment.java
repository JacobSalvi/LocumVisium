package com.example.mwcproject.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mwcproject.databinding.LocationPopupFragmentBinding;

public class LocationPopupFragment extends Fragment {

    private String ID;
    private TextView title;
    private LocationPopupFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = LocationPopupFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        title = binding.titleText;
        if (getArguments() != null) {
            ID = getArguments().getString("ID");
            System.out.println(ID);
        }


        binding.closeBtn.setOnClickListener((x) ->  {
            getParentFragmentManager().popBackStack();
        });
        title.setText(ID);
    }
}
