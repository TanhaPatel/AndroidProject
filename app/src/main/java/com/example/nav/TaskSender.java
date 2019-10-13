package com.example.nav;

import java.util.Date;

public class TaskSender {

    String task, date, time;

    public TaskSender() {
    }

    public TaskSender(String task, String date, String time) {
        this.task = task;
        this.date = date;
        this.time = time;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
