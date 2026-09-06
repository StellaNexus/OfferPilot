package com.offerpilot.app;


import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;

public class JobListFragment extends Fragment {

    public JobListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_job_list, container, false);
        Button addJobButton = view.findViewById(R.id.button_add_job);

        addJobButton.setOnClickListener(v -> {
            navigateToAddJob();
        });

        return view;
    }
    private void navigateToAddJob(){
        AddJobFragment fragment = new AddJobFragment();
        requireActivity().getSupportFragmentManager().beginTransaction()
        .replace(android.R.id.content, fragment)
        .addToBackStack(null)
        .commit();

    }

}
