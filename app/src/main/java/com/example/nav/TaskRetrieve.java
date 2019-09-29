package com.example.nav;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class TaskRetrieve {

    private String task,id;

    public TaskRetrieve(){}

    public TaskRetrieve(String task, String id) {
        this.task = task;
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}