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
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ListTasks extends Fragment {

    public ArrayList<Tasks> tasksList;

    private ArrayList<ArrayList<String>> tasksForEachLocation;
    private ArrayList<ArrayAdapter<String>> arrayAdaptersForEachLocation;
    private Places currentLocSelected;

    private ListView listView;
    private Button button;
    private Button changeLocDemoBtn;
    private EditText input;
    private Spinner dropdown;
    private ArrayList<Places> locations;
    private ArrayList<String> location_names;
    private ArrayAdapter<String> locationsAdapter;

    private ArrayList<HashMap<String, String>> task_details = new ArrayList<>();
    private ArrayList<ArrayList<HashMap<String, String>>> listItemsForEachLocation = new ArrayList<>();
    private ArrayList<SimpleAdapter> adaptersForEachLocation = new ArrayList<>();

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
        dropdown = (Spinner) view.findViewById(R.id.dropdown);
        changeLocDemoBtn = view.findViewById(R.id.changeLocBtn);

        tasksList = new ArrayList<>();
        locations = new ArrayList<>();

        try{
            tasksList = this.getArguments().getParcelableArrayList("Tasks List");
            locations = this.getArguments().getParcelableArrayList("places");

        }
        catch(Exception e){
        }


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

        int listCounter = 0;
        for(HashMap<String, String> lists: task_details){
            Iterator it = lists.entrySet().iterator();
            while(it.hasNext()){
                HashMap<String, String> resultsMap = new HashMap<>();
                Map.Entry pair = (Map.Entry) it.next();
                resultsMap.put("First Line", pair.getKey().toString());
                resultsMap.put("Second Line", pair.getValue().toString());
                listItemsForEachLocation.get(listCounter).add(resultsMap);
            }
            listCounter++;
        }

        manageDropDownSelections();

        changeLocDemoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).demoChangingLocation();
            }
        });

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
              //  listView.setAdapter(arrayAdaptersForEachLocation.get(arg2));
                listView.setAdapter(adaptersForEachLocation.get(arg2));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                button.setEnabled(false);
            }
        });
    }

    private void getLocations() {
        location_names.add("General Tasks");
        task_details.add(new HashMap<>());
        listItemsForEachLocation.add(new ArrayList<>());
        adaptersForEachLocation.add(new SimpleAdapter(getActivity(),
                listItemsForEachLocation.get(0),
                R.layout.list_item,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.text1, R.id.text2}));
        tasksForEachLocation.add(new ArrayList<String>());
        arrayAdaptersForEachLocation.add(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,tasksForEachLocation.get(0)));
        try{
            for(int i = 0; i < locations.size(); i++){
                location_names.add(locations.get(i).getPlace_name());
                //creates new arraylist for each location, and new array adapter to go along with it
                tasksForEachLocation.add(new ArrayList<String>());
                arrayAdaptersForEachLocation.add(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,tasksForEachLocation.get(i + 1)));

                task_details.add(new HashMap<>());
                listItemsForEachLocation.add(new ArrayList<>());
                adaptersForEachLocation.add(new SimpleAdapter(getActivity(),
                        listItemsForEachLocation.get(i + 1),
                        R.layout.list_item,
                        new String[]{"First Line", "Second Line"},
                        new int[]{R.id.text1, R.id.text2}));
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
                Toast.makeText(context, "Item Completed!", Toast.LENGTH_LONG).show();

                int index;
                if(currentLocSelected != null){
                    index = locations.indexOf(currentLocSelected);
                }
                else{
                    index = 0;
                }
               // String taskName = tasksForEachLocation.get(index).get(i);
                String taskName = listItemsForEachLocation.get(index).get(i).get("First Line");
                System.out.println(taskName);

                task_details.get(index).remove(i);
                listItemsForEachLocation.get(index).remove(i);
                adaptersForEachLocation.get(index).notifyDataSetChanged();

                tasksForEachLocation.get(index).remove(i);
                arrayAdaptersForEachLocation.get(index).notifyDataSetChanged();

                for(Tasks t: tasksList){
                    if(t.getTaskName().equals(taskName)){
                        tasksList.remove(t);
                        break;
                    }
                }
                return true;
            }
        });
    }

    private void filterThroughTasks(){
        if(tasksList != null){
            for(Tasks t: tasksList){
                if(t.getListName().equals("General Tasks")){
                    task_details.get(0).put(t.getTaskName(), t.getTaskSubheading());
                    tasksForEachLocation.get(0).add(t.getTaskName());
                }
                else{
                    int index = location_names.indexOf(t.getListName());
                    tasksForEachLocation.get(index).add(t.getTaskName());
                    task_details.get(index).put(t.getTaskName(), t.getTaskSubheading());
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
        if(tasksList != null){
            bundle.putParcelableArrayList("Tasks List", tasksList);
        }
        else{
            bundle.putParcelableArrayList("Tasks List", new ArrayList<Tasks>());
        }
        bundle.putParcelableArrayList("places", locations);
        childFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_activity, childFragment).commit();

    }
}