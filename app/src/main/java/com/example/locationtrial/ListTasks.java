package com.example.locationtrial;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ListTasks extends Fragment {

    public ArrayList<Tasks> tasksList;

    private ArrayList<ArrayList<String>> tasksForEachLocation;
    private ArrayList<ArrayAdapter<String>> arrayAdaptersForEachLocation;
    private Places currentLocSelected;

    private ListView listView;
    private Button button;
    private EditText input;
    private Spinner dropdown;
    private ArrayList<Places> locations;
    private ArrayList<String> location_names;
    private ArrayAdapter<String> locationsAdapter;

    public ListTasks() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.list_tasks, container, false);

        listView = (ListView) view.findViewById(R.id.listview);
        button = (Button) view.findViewById(R.id.button);
        //input = (EditText) view.findViewById(R.id.editTextTextPersonName2);
        dropdown = (Spinner) view.findViewById(R.id.dropdown);

        tasksList = new ArrayList<>();

        try{
            tasksList = this.getArguments().getParcelableArrayList("Tasks List");
        }
        catch(Exception e){
        }
        locations = this.getArguments().getParcelableArrayList("places");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });

        tasksForEachLocation = new ArrayList<ArrayList<String>>();
        arrayAdaptersForEachLocation = new ArrayList<ArrayAdapter<String>>();

        setUpListViewListener();

        location_names = new ArrayList<>();
        getLocations();
        locationsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, location_names);
        dropdown.setAdapter(locationsAdapter);

        filterThroughTasks();
        manageDropDownSelections();

        return view;
    }

    private void manageDropDownSelections() {
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                button.setEnabled(true);
                if(arg2 != 0) {
                    currentLocSelected = locations.get(arg2 - 1);
                    System.out.println(currentLocSelected.getPlace_name());
                }
                else{
                    currentLocSelected = null;
                }
                listView.setAdapter(arrayAdaptersForEachLocation.get(arg2));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                button.setEnabled(false);
            }
        });
    }

    private void getLocations() {
        location_names.add("General Tasks");
        tasksForEachLocation.add(new ArrayList<String>());
        arrayAdaptersForEachLocation.add(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,tasksForEachLocation.get(0)));
        try{
            for(int i = 0; i < locations.size(); i++){
                location_names.add(locations.get(i).getPlace_name());
                //creates new arraylist for each location, and new array adapter to go along with it
                tasksForEachLocation.add(new ArrayList<String>());
                arrayAdaptersForEachLocation.add(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,tasksForEachLocation.get(i + 1)));
            }

        }
        catch (Exception e){

        }
    }

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Context context = getActivity().getApplicationContext();
                Toast.makeText(context, "Item Removed", Toast.LENGTH_LONG).show();

                int index;
                if(currentLocSelected != null){
                    index = locations.indexOf(currentLocSelected);
                }
                else{
                    index = 0;
                }

                tasksForEachLocation.get(index).remove(i);
                arrayAdaptersForEachLocation.get(index).notifyDataSetChanged();

                return true;
            }
        });
    }

    private void filterThroughTasks(){
        if(tasksList != null){
            for(Tasks t: tasksList){
                if(t.getListName().equals("General Tasks")){
                    tasksForEachLocation.get(0).add(t.getTaskName());
                }
                else{
                    int index = location_names.indexOf(t.getListName());
                    tasksForEachLocation.get(index).add(t.getTaskName());
                }
            }
            for(ArrayAdapter<String> arrayAdapter: arrayAdaptersForEachLocation){
                arrayAdapter.notifyDataSetChanged();
            }
        }
    }

    private void addItem(View view) {

        Fragment childFragment = new AddingTaskFragment();
        Bundle bundle = new Bundle();
        TextView myText = (TextView) dropdown.getSelectedView();
        String listName = myText.getText().toString();

        bundle.putString("List Name", listName);
        bundle.putParcelableArrayList("Tasks List", tasksList);
        bundle.putParcelableArrayList("Locations", locations);
        childFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_activity, childFragment).commit();

//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.replace(R.id.main_activity, childFragment).commit();

//        String itemText = input.getText().toString();
//
//        if(!itemText.equals("")){
//            int index;
//            if(currentLocSelected != null){
//                index = locations.indexOf(currentLocSelected);
//            }
//            else{
//                index = 0;
//            }
//
//            tasksForEachLocation.get(index).add(itemText);
//            arrayAdaptersForEachLocation.get(index).notifyDataSetChanged();
//            input.setText("");
//        }
//        else{
//            Toast.makeText(getActivity().getApplicationContext(), "Please enter text...", Toast.LENGTH_LONG).show();
//        }
    }
}