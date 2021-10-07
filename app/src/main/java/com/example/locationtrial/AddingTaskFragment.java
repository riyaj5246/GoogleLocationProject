package com.example.locationtrial;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.BreakIterator;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddingTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddingTaskFragment extends Fragment {

    private EditText addTaskTitle, addTaskDescription, taskTime;
    private Button addTask;
    int mHour, mMinute;
    TimePickerDialog timePickerDialog;

    public AddingTaskFragment() {
        // Required empty public constructor
    }


    public static AddingTaskFragment newInstance(String param1, String param2) {
        AddingTaskFragment fragment = new AddingTaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View screenview = inflater.inflate(R.layout.fragment_adding_task, container, false);

        addTaskTitle = screenview.findViewById(R.id.addTaskTitle);
        addTaskDescription = screenview.findViewById(R.id.addTaskDescription);
        taskTime = screenview.findViewById(R.id.taskTime);
        addTask = screenview.findViewById(R.id.addTask);

        taskTime.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                timePickerDialog = new TimePickerDialog(getActivity(),
                        (view12, hourOfDay, minute) -> {
                            taskTime.setText(hourOfDay + ":" + minute);
                            timePickerDialog.dismiss();
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
            return true;
        });

        // Inflate the layout for this fragment
        return screenview;
    }

    public boolean validateFields() {
        if(addTaskTitle.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Please enter a valid title", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(addTaskDescription.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Please enter a valid description", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(taskTime.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Please enter time", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}