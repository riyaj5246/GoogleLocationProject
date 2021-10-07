package com.example.locationtrial;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import android.widget.Toast;

import java.util.ArrayList;


public class ListTasks extends Fragment {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;

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
        input = (EditText) view.findViewById(R.id.editTextTextPersonName2);
        dropdown = (Spinner) view.findViewById(R.id.dropdown);

        locations = this.getArguments().getParcelableArrayList("places");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });

        tasksForEachLocation = new ArrayList<ArrayList<String>>();
        arrayAdaptersForEachLocation = new ArrayList<ArrayAdapter<String>>();

        //items = new ArrayList<>();
        //itemsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);
        setUpListViewListener();

        location_names = new ArrayList<>();
        getLocations();
        locationsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, location_names);
        dropdown.setAdapter(locationsAdapter);

        manageDropDownSelections();

        return view;
    }

    private void manageDropDownSelections() {
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                button.setEnabled(true);
                currentLocSelected = locations.get(arg2);
                listView.setAdapter(arrayAdaptersForEachLocation.get(arg2));
                System.out.println(currentLocSelected.getPlace_name());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                button.setEnabled(false);
            }
        });
    }

    private void getLocations() {
        for(int i = 0; i < locations.size(); i++){
            location_names.add(locations.get(i).getPlace_name());
            //creates new arraylist for each location, and new array adapter to go along with it
            tasksForEachLocation.add(new ArrayList<String>());
            arrayAdaptersForEachLocation.add(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,tasksForEachLocation.get(i)));
        }

    }

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Context context = getActivity().getApplicationContext();
                Toast.makeText(context, "Item Removed", Toast.LENGTH_LONG).show();

                //TODO: change to remove from correct arraylist
                int index = locations.indexOf(currentLocSelected);
                tasksForEachLocation.get(index).remove(i);
                arrayAdaptersForEachLocation.get(index).notifyDataSetChanged();

//                items.remove(i);
//                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void addItem(View view) {
        String itemText = input.getText().toString();

        if(!itemText.equals("")){
            int index = locations.indexOf(currentLocSelected);
            tasksForEachLocation.get(index).add(itemText);
            arrayAdaptersForEachLocation.get(index).notifyDataSetChanged();
            input.setText("");
        }
        else{
            Toast.makeText(getActivity().getApplicationContext(), "Please enter text...", Toast.LENGTH_LONG).show();
        }
    }
}