package kronos.comkronoscodecomandroid.activity.activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.kronoscode.cacao.android.app.model.Location;
import com.kronoscode.cacao.android.app.model.Weather;
import com.kronoscode.cacao.android.app.provider.WeatherProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.List;
import java.util.Locale;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import kronos.comkronoscodecomandroid.R;

public class weatherActivity extends Activity {
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    Location gps_loc;
    Location network_loc;
    Location final_loc;
    double longitude;
    double latitude;
    String userCity, userAddress;

    private TextView cityText;
    private TextView condDescr;
    private TextView temp;
    private TextView press;
    private TextView feelsLike;
    private TextView windSpeed;
    private TextView windDeg;

    private TextView hum;
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        String city = "Chinandega";
        cityText = findViewById(R.id.cityText);
        condDescr = findViewById(R.id.condDescr);
        hum = findViewById(R.id.hum);
        temp = findViewById(R.id.temp);
        windSpeed = findViewById(R.id.windSpeed);
        imgView = findViewById(R.id.condIcon);
        feelsLike = findViewById(R.id.feelsLike);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
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
            latitude = 12.1364;
            longitude = -86.2514;
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


        JSONWeatherTask task = new JSONWeatherTask();
//        task.execute(new String[]{city});
//        task.execute(city);
        task.execute(new String[]{city,StrLat, StrLng});

    }

    public static String fmt(double d)
    {
        if (d == Math.floor(d)) {
            return String.format("%.0f", d); // Format is: 0 places after decimal point
        } else {
            return Double.toString(d);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_about, menu);
//        return true;
//    }


    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {
        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ( (new WeatherProvider()).getWeatherData(params[1],params[2]));

            try {
                weather = JSONWeatherParser.getWeather(data);

                // retrieve the icon
                weather.iconData = ( (new WeatherProvider()).getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;
        }


        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
            Log.i("weather", weather.toString());



            if (weather.iconData != null && weather.iconData.length > 0) {
                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
                imgView.setImageBitmap(img);
            }
            String windSpeedInKMH = String.format("%.2f", weather.wind.getSpeed() * 3.6);
            //fmt(weather.wind.getSpeed()* 3.6);

            cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
            condDescr.setText(weather.currentCondition.getDescr());
            temp.setText("" + Math.round((weather.temperature.getTemp())) + "° C");
            hum.setText("" + weather.currentCondition.getHumidity() + "%");
            feelsLike.setText("" + Math.round((weather.temperature.getFeelsLike())) + "° C");
//            press.setText("" + weather.currentCondition.getPressure() + " hPa");
            windSpeed.setText("" + windSpeedInKMH + " km/h");
//            windDeg.setText("" + weather.wind.getDeg() + "");

        }
    }
}
