package com.example.locationtrial;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class Places implements Parcelable {

    private LatLng coordinates;
    private String place_name;

    public Places(LatLng x,String name){
        coordinates = x;
        place_name = name;
    }


    protected Places(Parcel in) {
        super();
        coordinates = in.readParcelable(LatLng.class.getClassLoader());
        place_name = in.readString();
    }

    public static final Creator<Places> CREATOR = new Creator<Places>() {
        @Override
        public Places createFromParcel(Parcel in) {
            return new Places(in);
        }

        @Override
        public Places[] newArray(int size) {
            return new Places[size];
        }
    };

    public LatLng getCoordinates(){
        return coordinates;
    }

    public String getPlace_name() {
        return place_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(coordinates, i);
        parcel.writeString(place_name);
    }
}
