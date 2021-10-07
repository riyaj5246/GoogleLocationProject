package com.example.locationtrial;

import android.text.Editable;

public class Tasks {

    private final String taskName;
    private final String taskDescription;
    private final String taskTime;
    private final String listName;
    private final String taskDate;

    public Tasks(String name, String description, String time, String list, String date){
        taskName = name;
        taskDescription = description;
        taskTime = time;
        listName = list;
        taskDate = date;
    }


}
