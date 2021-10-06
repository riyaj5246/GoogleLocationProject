package com.example.locationtrial;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
    private ListView listView;
    private Button button;
    private EditText input;
    private Spinner dropdown;
    private ArrayList<String> locations;
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


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });

        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);
        setUpListViewListener();

        locations = new ArrayList<>();
        getLocations();
        locationsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, locations);
        dropdown.setAdapter(locationsAdapter);

        return view;
    }

    private void getLocations() {

    }

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Context context = getActivity().getApplicationContext();
                Toast.makeText(context, "Item Removed", Toast.LENGTH_LONG).show();

                items.remove(i);
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void addItem(View view) {
        String itemText = input.getText().toString();

        if(!itemText.equals("")){
            itemsAdapter.add(itemText);
            input.setText("");
        }
        else{
            Toast.makeText(getActivity().getApplicationContext(), "Please enter text...", Toast.LENGTH_LONG).show();
        }
    }
}