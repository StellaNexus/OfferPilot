package com.offerpilot.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class JobViewModel extends ViewModel {

    private MutableLiveData<List<Job>> jobList = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Job>> getJobList() {
        return jobList;
    }

    public void addJob(Job job) {
        List<Job> currentList = jobList.getValue();
        if (currentList != null) {
            currentList.add(0, job);
            jobList.setValue(currentList);
        }
    }

    public Job getJob(int position) {
        List<Job> currentList = jobList.getValue();
        if (currentList != null && position >= 0 && position < currentList.size()) {
            return currentList.get(position);
        }
        return null;
    }
}