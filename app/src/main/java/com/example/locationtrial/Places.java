package com.example.locationtrial;

import com.google.android.gms.maps.model.LatLng;

public class Places {

    private LatLng coordinates;
    private String place_name;

    public Places(LatLng x,String name){
        coordinates = x;
        place_name = name;
    }

    public LatLng getCoordinates(){
        return coordinates;
    }

    public String getPlace_name() {
        return place_name;
    }
}
