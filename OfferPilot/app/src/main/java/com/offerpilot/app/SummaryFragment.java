package com.offerpilot.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SummaryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_summary,
                container,
                false
        );

        Button backButton = view.findViewById(R.id.button_back_to_practice);

        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();

        });

        return view;
    }
}
