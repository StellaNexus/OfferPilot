package com.offerpilot.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class JobDetailFragment extends Fragment {

    private Job job;  // 当前显示的岗位

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_job_detail, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int position = getArguments().getInt("position", 0);
        job = JobListFragment.jobList.get(position);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 绑定控件
        TextView textTitle = view.findViewById(R.id.text_detail_title);
        TextView textCompany = view.findViewById(R.id.text_detail_company);
        TextView textJd = view.findViewById(R.id.text_detail_jd);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        MaterialButton buttonStart = view.findViewById(R.id.button_start_practice);

        // 显示数据
        textTitle.setText(job.getTitle());
        textCompany.setText(job.getCompany());
        textJd.setText(job.getDescription());

        // 返回箭头
        toolbar.setNavigationOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        // 开始练习
        buttonStart.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "练习模块待接入", Toast.LENGTH_SHORT).show();
        });
    }

}
