package com.example.nav;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class TaskRetrieve {

    String task;
    Date datentime;

    public TaskRetrieve(){}

    public TaskRetrieve(String task) {
        this.task = task;
    }

    public Date getDatentime() {
        return datentime;
    }

    public void setDatentime(Date datentime) {
        this.datentime = datentime;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

}