package kronos.comkronoscodecomandroid.activity.activity;

import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.kronoscode.cacao.android.app.model.Location;
import com.kronoscode.cacao.android.app.model.Weather;
import com.kronoscode.cacao.android.app.model.WeatherForecast;
import com.kronoscode.cacao.android.app.model.DayForecast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class JSONWeatherParser {

    public static Weather getWeather(String data) throws JSONException  {
        Weather weather = new Weather();
        // We create out JSONObject from the data
        JSONObject jObj = new JSONObject(data);

        // We start extracting the info
        Location loc = new Location();

        JSONObject coordObj = getObject("coord", jObj);
        loc.setLatitude(getFloat("lat", coordObj));
        loc.setLongitude(getFloat("lon", coordObj));

        JSONObject sysObj = getObject("sys", jObj);
        loc.setCountry(getString("country", sysObj));
        loc.setCity(getString("name", jObj));
        weather.location = loc;

        // We get weather info (This is an array)
        JSONArray jArr = jObj.getJSONArray("weather");

        // We use only the first value
        JSONObject JSONWeather = jArr.getJSONObject(0);
        weather.currentCondition.setWeatherId(getInt("id", JSONWeather));
        weather.currentCondition.setDescr(getString("description", JSONWeather));
        weather.currentCondition.setCondition(getString("main", JSONWeather));
        weather.currentCondition.setIcon(getString("icon", JSONWeather));

        JSONObject mainObj = getObject("main", jObj);
        weather.currentCondition.setHumidity(getInt("humidity", mainObj));
        weather.currentCondition.setPressure(getInt("pressure", mainObj));
        weather.temperature.setMaxTemp(getFloat("temp_max", mainObj));
        weather.temperature.setMinTemp(getFloat("temp_min", mainObj));
        weather.temperature.setTemp(getFloat("temp", mainObj));
        weather.temperature.setFeelsLike(getFloat("feels_like", mainObj));

        // Wind
        JSONObject wObj = getObject("wind", jObj);
        weather.wind.setSpeed(getFloat("speed", wObj));
        weather.wind.setDeg(getFloat("deg", wObj));

        // Clouds
        JSONObject cObj = getObject("clouds", jObj);
        weather.clouds.setPerc(getInt("all", cObj));

        // download the icon to show

        return weather;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static WeatherForecast getForecastWeather(String data) throws JSONException, ParseException {

        WeatherForecast forecast = new WeatherForecast();

        // We create out JSONObject from the data
        JSONObject jObj = new JSONObject(data);

        JSONArray jArr = jObj.getJSONArray("list"); // Here we have the forecast for every day

//        LocalDate localTodayDate = LocalDate.now();
//        String txtLocalTodayDate = localTodayDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
//        Date todayDate = new SimpleDateFormat("dd/MM/yyyy").parse(txtLocalTodayDate);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat fmt = new SimpleDateFormat("yyyy/MM/dd");

        Date date = new Date();
        String dateStrFormat = fmt.format(date);

        Date todayHandlerDate = fmt.parse(dateStrFormat);
        Calendar calendar = Calendar.getInstance();

        //  format today date - get day of month as int
        calendar.setTime(date);
        int todayDayOfWeek = calendar.get(Calendar.DAY_OF_MONTH);

        // We traverse all the array and parse the data
        for (int i=0; i < jArr.length(); i++) {
            // forecast format
            Date handleForecastDate;
            JSONObject jDayForecast = jArr.getJSONObject(i);
            String forecastStrDate = getString("dt_txt", jDayForecast);
            Date forecastDate = new SimpleDateFormat("yyyy-MM-dd").parse(forecastStrDate);
            String forecastDailyDate = fmt.format(forecastDate);
            handleForecastDate = fmt.parse(forecastDailyDate);

            //  forecast iteration date - get day of month as int
            calendar.setTime(forecastDate);
            int forecastDayOfWeek = calendar.get(Calendar.DAY_OF_MONTH);
            int todayDayPlusOne = todayDayOfWeek + 1;


            if(forecastDayOfWeek == todayDayOfWeek || forecastDayOfWeek==todayDayPlusOne){
                System.out.println("same day:");
                if (forecastDayOfWeek == todayDayPlusOne) {
                    todayDayOfWeek += 1;
                }

                // Now we have the json object so we can extract the data
                DayForecast df = new DayForecast();

                // We retrieve the timestamp (dt)
                df.timestamp = jDayForecast.getLong("dt");

                JSONObject mainForecastObj = getObject("main", jDayForecast);
                df.weather.temperature.setMaxTemp(getFloat("temp_max", mainForecastObj));
                df.weather.temperature.setMinTemp(getFloat("temp_min", mainForecastObj));

                // Pressure & Humidity
                df.weather.currentCondition.setPressure(getInt("pressure", mainForecastObj));
//            df.weather.currentCondition.setHumidity((int) jDayForecast.getDouble("humidity"));
                df.weather.currentCondition.setHumidity(getInt("humidity", mainForecastObj));

                // ...and now the weather
                JSONArray jWeatherArr = jDayForecast.getJSONArray("weather");
                JSONObject jWeatherObj = jWeatherArr.getJSONObject(0);
                df.weather.currentCondition.setWeatherId(getInt("id", jWeatherObj));
                df.weather.currentCondition.setDescr(getString("description", jWeatherObj));
                df.weather.currentCondition.setCondition(getString("main", jWeatherObj));
                df.weather.currentCondition.setIcon(getString("icon", jWeatherObj));

                todayDayOfWeek += 1;

                forecast.addForecast(df);

            }
            else{
                // it goes for days like 31 + 1, there's no 32,so update date to 1
                if (forecastDayOfWeek == 1 && (todayDayOfWeek == 30 || todayDayOfWeek == 31)){
                    todayDayOfWeek += 1;
                }

            }

        }

        return forecast;
    }

    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }
}
