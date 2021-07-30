package kronos.comkronoscodecomandroid.activity.activity;

import org.json.JSONException;

import kronos.comkronoscodecomandroid.activity.adapter.ForecastWeatherAdapter;
import com.kronoscode.cafemovil.android.app.model.Weather;
import com.kronoscode.cafemovil.android.app.model.WeatherForecast;
import com.kronoscode.cafemovil.android.app.provider.WeatherProvider;

import android.Manifest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


import kronos.comkronoscodecomandroid.R;

public class weatherActivity extends AppCompatActivity {
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    Location gps_loc;
    Location network_loc;
    Location final_loc;
    double longitude;
    double latitude;
    private TextView cityText;
    private TextView condDescr;
    private TextView temp;
    private TextView feelsLike;
    private TextView windSpeed;

    private static String forecastDaysNum = "5";
    private ViewPager pager;

    private TextView hum;
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weather);

        Toolbar toolbar = findViewById(R.id.weather_toolbar);
        PagerTabStrip strip = findViewById(R.id.pager_title_strip);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        setTitle(getString(R.string.title_activity_weather));
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_home);
        }

        // dafault city
        String city = "Tegucigalpa";

        cityText = findViewById(R.id.cityText);
        condDescr = findViewById(R.id.condDescr);
        hum = findViewById(R.id.hum);
        temp = findViewById(R.id.temp);
        windSpeed = findViewById(R.id.windSpeed);
        imgView = findViewById(R.id.condIcon);
        feelsLike = findViewById(R.id.feelsLike);

        pager = findViewById(R.id.pager);

        strip.setDrawFullUnderline(false);
        strip.setTabIndicatorColor(getResources().getColor(R.color.list_item_text_color));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        try {

            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (gps_loc != null) {
            final_loc = gps_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        } else if (network_loc != null) {
            final_loc = network_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        }
        else {
            // set default weather data - current Managua
            latitude = 14.081999;
            longitude = -87.202438;
        }

        List<Address> addresses;
        try {
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            addresses = gcd.getFromLocation(latitude,longitude, 1);
            if (addresses.size() > 0){
                city = addresses.get(0).getLocality();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        String StrLat = String.valueOf(latitude);
        String StrLng = String.valueOf(longitude);

        // retrieve weather (current) data
        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{city,StrLat, StrLng});

        // retrieve forecast data
        JSONForecastTask forecastTask = new JSONForecastTask();
        forecastTask.execute(new String[]{city});

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public static String fmt(double d)
    {
        if (d == Math.floor(d)) {
            return String.format("%.0f", d); // Format is: 0 places after decimal point
        } else {
            return Double.toString(d);
        }
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {
        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ( (new WeatherProvider()).getWeatherData(params[1],params[2]));

            try {
                weather = JSONWeatherParser.getWeather(data);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;
        }


        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
            Log.i("weather", weather.toString());

            String iconCode = "ic_" + weather.currentCondition.getIcon();
            int imageResource = getResources().getIdentifier(iconCode, "drawable", getPackageName());

            imgView.setImageResource(imageResource);
            long windSpeedInKMH = Math.round(weather.wind.getSpeed() * 3.6);
            //fmt(weather.wind.getSpeed()* 3.6);

            String capitalizeDesc = weather.currentCondition.getDescr().substring(0, 1).toUpperCase() + weather.currentCondition.getDescr().substring(1);

            cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
            condDescr.setText(capitalizeDesc);
            temp.setText("" + Math.round((weather.temperature.getTemp())) + "° C");
            hum.setText("" + weather.currentCondition.getHumidity() + "%");
            feelsLike.setText("" + Math.round((weather.temperature.getFeelsLike())) + "° C");
//            press.setText("" + weather.currentCondition.getPressure() + " hPa");
            windSpeed.setText("" + Math.round(weather.wind.getSpeed() * 3.6) + " km/h");
//            windDeg.setText("" + weather.wind.getDeg() + "");

        }
    }

    private class JSONForecastTask extends AsyncTask<String,Void, WeatherForecast> {

        @Override
        protected WeatherForecast doInBackground(String... strings) {
            String forecastData = ((new WeatherProvider()).getForecastWeatherData(strings[0]));
            WeatherForecast forecast = new WeatherForecast();
            try {
                forecast = JSONWeatherParser.getForecastWeather(forecastData);
            } catch (JSONException | ParseException e){
                e.printStackTrace();
            }
            return forecast;
        }

        @Override
        protected void  onPostExecute(WeatherForecast forecastWeather) {
            super.onPostExecute(forecastWeather);
            ForecastWeatherAdapter adapter = new ForecastWeatherAdapter(Integer.parseInt(forecastDaysNum), getSupportFragmentManager() ,forecastWeather);
            pager.setAdapter(adapter);
        }
    }
}
