package com.example.locationtrial;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddingTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddingTaskFragment extends Fragment {

    private EditText addTaskTitle, addTaskDescription, taskTime, taskDate;
    private ArrayList<Tasks> allTasks;
    private Button addTask;
    private String taskList;
    int mYear, mMonth, mDay;
    int mHour, mMinute;
    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;

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
        taskDate = screenview.findViewById(R.id.taskDate);
        taskList = this.getArguments().getString("List Name");
        allTasks = this.getArguments().getParcelableArrayList("Tasks List");

        taskDate.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(getActivity(),
                        (view1, year, monthOfYear, dayOfMonth) -> {
                            taskDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            datePickerDialog.dismiss();
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
            return true;
        });

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

        addTask.setOnClickListener(view ->{
            if(validateFields()){
                Tasks newTask = new Tasks(addTaskTitle.getText().toString(), addTaskDescription.getText().toString(), taskTime.getText().toString(), "General Activity", taskDate.getText().toString());
                allTasks.add(newTask);
                clearAllFields();
                getActivity().getFragmentManager().popBackStack();

//                //TODO: FIGURE OUT HOW TO CLOSE OUT THE CHILD FRAGMENT
//                FragmentManager fm = getFragmentManager();
//                for (Fragment frag : fm.getFragments()) {
//                    if (frag.isVisible()) {
//                        FragmentManager childFm = frag.getChildFragmentManager();
//                        if (childFm.getBackStackEntryCount() > 0) {
//                            childFm.popBackStack();
//                            return;
//                        }
//                    }
//                }
            }
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

    private void clearAllFields(){
        addTaskTitle.setText("");
        addTaskDescription.setText("");
        taskDate.setText("");
        taskTime.setText("");
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}