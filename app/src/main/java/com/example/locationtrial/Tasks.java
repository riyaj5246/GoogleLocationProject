package com.example.locationtrial;

import android.os.Parcel;
import android.os.Parcelable;

public class Tasks implements Parcelable {

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

    public String getTaskName(){
        return taskName;
    }

    public String getListName() {
        return listName;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    protected Tasks(Parcel in) {
        taskName = in.readString();
        taskDescription = in.readString();
        taskTime = in.readString();
        listName = in.readString();
        taskDate = in.readString();
    }

    public static final Creator<Tasks> CREATOR = new Creator<Tasks>() {
        @Override
        public Tasks createFromParcel(Parcel in) {
            return new Tasks(in);
        }

        @Override
        public Tasks[] newArray(int size) {
            return new Tasks[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(taskName);
        parcel.writeString(taskDescription);
        parcel.writeString(taskTime);
        parcel.writeString(listName);
        parcel.writeString(taskDate);
    }
}
