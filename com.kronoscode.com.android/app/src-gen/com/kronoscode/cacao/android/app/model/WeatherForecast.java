// (http://www.survivingwithandroid.com)

package com.kronoscode.cacao.android.app.model;

import java.util.ArrayList;
import java.util.List;

public class WeatherForecast {
    private List<DayForecast> daysForecast = new ArrayList<>();

    public void addForecast(DayForecast forecast) {
        daysForecast.add(forecast);
        System.out.println("Add forecast ["+forecast+"]");
    }

    public DayForecast getForecast(int dayNum) {
        return daysForecast.get(dayNum);
    }
}
