package com.kronoscode.cacao.android.app.model;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DayForecast {
    private static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    public Weather weather = new Weather();
//    public ForecastTemp forecastTemp = new ForecastTemp();
    public long timestamp;

//    public class ForecastTemp {
//        public float day;
//        public float min;
//        public float max;
//        public float night;
//        public float eve;
//        public float morning;
//    }
    public String getStringDate() {
        return df.format(new Date(timestamp));
    }
}
