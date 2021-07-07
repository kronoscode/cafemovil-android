package kronos.comkronoscodecomandroid.activity.fragment;

import kronos.comkronoscodecomandroid.R;
import com.kronoscode.cacao.android.app.provider.WeatherProvider;
import com.kronoscode.cacao.android.app.model.DayForecast;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DayForecastFragment extends Fragment {
    private DayForecast dayForecast;
    private ImageView iconWeather;

    public DayForecastFragment() {}

    public void setForecast(DayForecast dayForecast) {

        this.dayForecast = dayForecast;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dayforecast_fragment, container, false);

        TextView tempView = v.findViewById(R.id.tempForecast);
        TextView descView = v.findViewById(R.id.skydescForecast);
        tempView.setText( (int) (dayForecast.weather.temperature.getMinTemp()) + "-" + (int) (dayForecast.weather.temperature.getMaxTemp()) );
        descView.setText(dayForecast.weather.currentCondition.getDescr());
//        iconWeather = v.findViewById(R.id.forCondIcon);
        // Now we retrieve the weather icon
//        JSONIconWeatherTask task = new JSONIconWeatherTask();
//        task.execute(new String[]{dayForecast.weather.currentCondition.getIcon()});

        return v;
    }

//    private class JSONIconWeatherTask extends AsyncTask<String, Void, byte[]> {
//
//        @Override
//        protected byte[] doInBackground(String... params) {
//
//            byte[] data = null;
//
//            try {
//
//                // Let's retrieve the icon
//                data = ( (new WeatherProvider()).getImage(params[0]));
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return data;
//        }
//        @Override
//        protected void onPostExecute(byte[] data) {
//            super.onPostExecute(data);
//
//            if (data != null) {
//                Bitmap img = BitmapFactory.decodeByteArray(data, 0, data.length);
//                iconWeather.setImageBitmap(img);
//            }
//        }
//    }
}