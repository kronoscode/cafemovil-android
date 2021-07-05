package com.kronoscode.cacao.android.app.provider;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherProvider {
    private static String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?units=metric&lat=";
    private static String IMG_URL = "https://openweathermap.org/img/w/";
    private static String APP_ID = "6cf86728f50d9c8c0f4f086970faeb5b";
    private static String BASE_FORECAST_URL = "https://api.openweathermap.org/data/2.5/forecast?q=";

    public String getWeatherData(String lat, String lng) {
        HttpURLConnection con = null ;
        InputStream is = null;

        try {
            con = (HttpURLConnection) ( new URL(BASE_URL + lat + "&lon=" + lng + "&lang=es&appid=" + APP_ID)).openConnection();
//            con = (HttpURLConnection) ( new URL(BASE_URL + "&lang=es&appid=" + APPID)).openConnection();
            con.setRequestMethod("GET");

            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // read the response
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while (  (line = br.readLine()) != null )
                buffer.append(line + "\\r\\n");

            is.close();
//            br.close();
            con.disconnect();
            return buffer.toString();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }
        return null;
    }

    public byte[] getImage(String code) {
        HttpURLConnection con = null ;
        InputStream is = null;
        try {
            con = (HttpURLConnection) ( new URL(IMG_URL + code + ".png")).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
//            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            is = con.getInputStream();
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while ( is.read(buffer) != -1)
                baos.write(buffer);

            return baos.toByteArray();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;

    }

    public String getForecastWeatherData(String city) {
        HttpURLConnection con = null ;
        InputStream is = null;

        try {

            // Forecast
            String url = BASE_FORECAST_URL + city + "&&units=metric&lang=es&appid=" + APP_ID;
//            if (lang != null)
//                url = url + "&lang=" + lang;
//
//            url = url + "&cnt=" + forecastDayNum;
            con = (HttpURLConnection) ( new URL(url)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            StringBuffer buffer1 = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br1 = new BufferedReader(new InputStreamReader(is));
            String line1 = null;
            while (  (line1 = br1.readLine()) != null )
                buffer1.append(line1 + "\r\n");

            is.close();
            con.disconnect();

            System.out.println("Buffer ["+buffer1.toString()+"]");
            return buffer1.toString();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;

    }
}
