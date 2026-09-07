package com.offerpilot.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.appbar.MaterialToolbar;
import android.widget.Toast;

public class AddJobFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_add_job,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputLayout tilPosition = view.findViewById(R.id.til_position);
        TextInputLayout tilCompany = view.findViewById(R.id.til_company);
        TextInputLayout tilJd = view.findViewById(R.id.til_jd);
        MaterialButton buttonSave = view.findViewById(R.id.button_save);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });
        buttonSave.setOnClickListener(v -> {
            String position = tilPosition.getEditText().getText().toString().trim();
            String company = tilCompany.getEditText().getText().toString().trim();
            String jd = tilJd.getEditText().getText().toString().trim();
            if (position.isEmpty()) {
                tilPosition.setError("此项必填");
                return;
            }
            if (company.isEmpty()) {
                tilCompany.setError("此项必填");
                return;
            }
            if (jd.isEmpty()) {
                tilJd.setError("此项必填");
                return;
            }
            Job newJob = new Job(position, company, jd);
            JobListFragment.jobList.add(0, newJob);

            Toast.makeText(requireContext(), "保存成功", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        });

    }
}
