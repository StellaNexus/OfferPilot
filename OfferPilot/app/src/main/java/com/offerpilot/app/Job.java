package com.offerpilot.app;

import java.util.UUID;

public class Job {
    private String id;
    private String title;
    private String company;
    private String description;
    private long createdAt;

    public Job(String title,String company,String description){
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.company = company;
        this.description = description;
        this.createdAt = System.currentTimeMillis();
    }

    public String getId(){return id;}
    public String getTitle(){return title;}
    public String getCompany(){return company;}
    public String getDescription(){return description;}
    public long getCreatedAt(){return createdAt;}
}
