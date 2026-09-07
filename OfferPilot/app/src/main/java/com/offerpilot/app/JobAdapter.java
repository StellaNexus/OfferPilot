package com.offerpilot.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Date;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {

    private List<Job> jobList;
    private OnItemClickListener onItemClickListener;

    public JobAdapter(List<Job> jobList) {
        this.jobList = jobList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textJobTitle;
        TextView textJobCompany;
        TextView textJobTime;

        public ViewHolder(View itemView) {
            super(itemView);
            textJobTitle = itemView.findViewById(R.id.text_job_title);
            textJobCompany = itemView.findViewById(R.id.text_job_company);
            textJobTime = itemView.findViewById(R.id.text_job_time);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_job, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.textJobTitle.setText(job.getTitle());
        holder.textJobCompany.setText(job.getCompany());
        holder.textJobTime.setText("最近练习: " + new Date(job.getCreatedAt()).toLocaleString());

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }
}