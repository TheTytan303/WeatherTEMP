package edu.Ciesla.models;

import java.util.Comparator;

public class WeatherComparator implements Comparator<Weather> {

    @Override
    public int compare(Weather w1, Weather w2){
        return Integer.compare(w1.getId(), w2.getId());
    }
}
