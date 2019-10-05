package com.example.nav;

import java.util.Date;

public class TaskSender {

    String task;
    Date datentime;

    public TaskSender(){}

    public TaskSender(Date datentime, String task) {
        this.datentime = datentime;
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
