package com.offerpilot.app;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class JobListFragment extends Fragment {

    private RecyclerView recyclerView;
    private View emptyState;
    private FloatingActionButton fabAddJob;
    public static List<Job> jobList = new ArrayList<>();
    private JobAdapter adapter;

    public JobListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_list, container, false);

        emptyState = view.findViewById(R.id.empty_state);
        recyclerView = view.findViewById(R.id.recycler_jobs);
        Button addJobButton = view.findViewById(R.id.button_add_job);
        fabAddJob = view.findViewById(R.id.fab_add_job);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new JobAdapter(jobList);
        adapter.setOnItemClickListener(position -> {
            JobDetailFragment detailFragment = new JobDetailFragment();
            Bundle args = new Bundle();
            args.putInt("position", position);
            detailFragment.setArguments(args);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });
        recyclerView.setAdapter(adapter);

        addJobButton.setOnClickListener(v -> navigateToAddJob());
        fabAddJob.setOnClickListener(v -> navigateToAddJob());

        updateUI();
        return view;
    }

    private void navigateToAddJob() {
        AddJobFragment fragment = new AddJobFragment();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void updateUI() {
        if (jobList.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            fabAddJob.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            fabAddJob.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        updateUI();
    }
}