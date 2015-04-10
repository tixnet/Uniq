package com.uniqapp.android.uniq;

import android.content.Context;

import java.util.List;

/**
 * Created by Mateusz Gaździak on 2015-04-09.
 */
public class City {
    private String name;
    private int numberOfZones = 1;
    private List<String> coordinates;


    public City(Context context, String name) {
        this.name = name;
   /*     coordinates =  Arrays.asList(getResources().getStringArray(R.array.coordinates));*/
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(List<String> coordinates) {
        this.coordinates = coordinates;
    }

    public void setNumberOfZones(int numberOfZones) {
        this.numberOfZones = numberOfZones;
    }

    public int getNumberOfZones() {
        return numberOfZones;

    }

    public List<String> getCoordinates() {
        return coordinates;
    }
}
