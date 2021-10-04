package com.example.locationtrial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private ArrayList<Places> locations = new ArrayList<>();
    FloatingActionButton addLocButton, confirmLoc, deleteLoc;
    EditText locNameText;
    MarkerOptions selectedLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addLocButton = (FloatingActionButton) findViewById(R.id.addLocBtn);
        locNameText = (EditText) findViewById(R.id.locNameTextField);
        confirmLoc = (FloatingActionButton) findViewById(R.id.confirmLoc);
        deleteLoc = (FloatingActionButton) findViewById(R.id.deleteLoc);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
    }

    private void fetchLastLocation() {
        System.out.println("getting here");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            System.out.println("here pt two");
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        System.out.println("here pt 3");
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                System.out.println("here pt 4");

                if (location != null){
                    System.out.println("here pt 5");
                    currentLocation = location;
                    System.out.println("lat: " + currentLocation.getLatitude() + ", " + " long: " + currentLocation.getLongitude());
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + ""+ currentLocation.getLongitude(),Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
                    supportMapFragment.getMapAsync(MainActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("getting here");
        LatLng myLoc;
        myLoc = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(myLoc)
                .title("Current Location");

        googleMap.addMarker(markerOptions);

        // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(myLoc)             // Sets the center of the map to school
                .zoom(15)                   // Sets the zoom
                .tilt(35)                   // Sets the tilt of the camera to 20 degrees
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                googleMap.clear();
                addCurrentLoc(googleMap);
                addSavedLocations(googleMap);
                selectedLoc = new MarkerOptions().position(point);
                googleMap.addMarker(selectedLoc);
                addLocButton.setVisibility(View.VISIBLE);
                deleteLoc.setVisibility(View.VISIBLE);
            }
        });

        addLocButton.setOnClickListener(view -> {
            locNameText.setVisibility(View.VISIBLE);
            confirmLoc.setVisibility(View.VISIBLE);
            addLocButton.setVisibility(View.INVISIBLE);
            locNameText.setText(R.string.Location_Name);
        });

        confirmLoc.setOnClickListener(view -> {
            LatLng newPos = selectedLoc.getPosition();
            String newName = locNameText.getText().toString();
            locations.add(new Places(newPos, newName));
            confirmLoc.setVisibility(View.INVISIBLE);
            deleteLoc.setVisibility(View.INVISIBLE);
            locNameText.setVisibility(View.INVISIBLE);
            googleMap.clear();
            addCurrentLoc(googleMap);
            addSavedLocations(googleMap);
        });

        deleteLoc.setOnClickListener(view -> {
            googleMap.clear();
            addCurrentLoc(googleMap);
            addSavedLocations(googleMap);
            locNameText.setVisibility(View.INVISIBLE);
            confirmLoc.setVisibility(View.INVISIBLE);
            addLocButton.setVisibility(View.INVISIBLE);
            deleteLoc.setVisibility(View.INVISIBLE);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }
    }

    private void addCurrentLoc(GoogleMap map){
        map.clear();
        LatLng myLoc;
        myLoc = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(myLoc)
                .title("Current Location");

        map.addMarker(markerOptions);
    }

    private void addSavedLocations(GoogleMap googleMap) {
        for(Places p: locations){
            MarkerOptions markerOptions = new MarkerOptions().position(p.getCoordinates())
                    .title(p.getPlace_name());

            googleMap.addMarker(markerOptions);
        }
    }
}