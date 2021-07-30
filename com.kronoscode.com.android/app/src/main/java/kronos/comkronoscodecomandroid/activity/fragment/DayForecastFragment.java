package kronos.comkronoscodecomandroid.activity.fragment;

import kronos.comkronoscodecomandroid.R;

import com.kronoscode.cacao.android.app.model.DayForecast;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DayForecastFragment extends Fragment {
    private DayForecast dayForecast;
//    private ImageView iconWeather;

    public DayForecastFragment() {}

    public void setForecast(DayForecast dayForecast) {

        this.dayForecast = dayForecast;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dayforecast_fragment, container, false);
        ImageView forecastIcon = v.findViewById(R.id.forecastIcon);
        TextView forecastTemp = v.findViewById(R.id.forecastTemp);
        TextView tempMinMaxView = v.findViewById(R.id.forecastMinMax);
        TextView descView = v.findViewById(R.id.skydescForecast);

        // Icon code from API response
        String iconCode = "ic_" + dayForecast.weather.currentCondition.getIcon();
        int imageResource = getResources().getIdentifier(iconCode, "drawable", getActivity().getPackageName());

//        Capitalize Description
        String capitalizeDesc = dayForecast.weather.currentCondition.getDescr().substring(0, 1).toUpperCase() + dayForecast.weather.currentCondition.getDescr().substring(1);
//        forecastIcon.setImageDrawable(res.getDrawable(R.drawable.ic_01d));
        forecastIcon.setImageResource(imageResource);
        forecastTemp.setText("" + Math.round(dayForecast.weather.temperature.getTemp()) + "° C");
        tempMinMaxView.setText("Min: " + Math.round(dayForecast.weather.temperature.getMinTemp()) + "° " + " Max:" + Math.round(dayForecast.weather.temperature.getMaxTemp()) + "°" );
        descView.setText(capitalizeDesc);
//        iconWeather = v.findViewById(R.id.forCondIcon);

        return v;
    }

}