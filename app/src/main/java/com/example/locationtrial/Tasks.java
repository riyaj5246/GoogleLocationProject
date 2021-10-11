package com.example.locationtrial;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

    public String getTaskSubheading(){
        return (taskTime + "    " + taskDate + "    " + taskDescription);
    }

    public Date getDateTime(){
        String totDateTime = taskDate + " " + taskTime;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try {
            return sdf.parse(totDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
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

    public Long[] getTimeTillTask(){
        try{
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            Date today = new Date();
            Date task = this.getDateTime();
            System.out.println("Task date: " + formatter.format(this.getDateTime()));
            System.out.println(formatter.format(today));

            // Calucalte time difference
            // in milliseconds
            long difference_In_Time
                    = task.getTime() - today.getTime();

            // Calucalte time difference in seconds,
            // minutes, hours, years, and days

            long difference_In_Minutes
                    = TimeUnit
                    .MILLISECONDS
                    .toMinutes(difference_In_Time)
                    % 60;

            long difference_In_Hours
                    = TimeUnit
                    .MILLISECONDS
                    .toHours(difference_In_Time)
                    % 24;

            long difference_In_Days
                    = TimeUnit
                    .MILLISECONDS
                    .toDays(difference_In_Time)
                    % 365;

            long difference_In_Years
                    = TimeUnit
                    .MILLISECONDS
                    .toDays(difference_In_Time)
                    / 3651;

            // Print the date difference in
            // years, in days, in hours, in
            // minutes, and in seconds
            System.out.print(
                    "Difference"
                            + " between two dates is: ");

            // Print result
            System.out.println(
                    difference_In_Years
                            + " years, "
                            + difference_In_Days
                            + " days, "
                            + difference_In_Hours
                            + " hours, "
                            + difference_In_Minutes
                            + " minutes. ");

            Long[] differences = {difference_In_Years, difference_In_Days, difference_In_Hours, difference_In_Minutes};
            return differences;
        }
        catch(Exception e){

        }
        return null;
    }
}
