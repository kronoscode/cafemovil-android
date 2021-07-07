package kronos.comkronoscodecomandroid.activity.adapter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

//import kronos.comkronoscodecomandroid.activity.adapter.ForecastWeatherAdapter;
import kronos.comkronoscodecomandroid.R;
import kronos.comkronoscodecomandroid.activity.fragment.DayForecastFragment;

import com.kronoscode.cacao.android.app.model.WeatherForecast;
import com.kronoscode.cacao.android.app.provider.WeatherProvider;
import com.kronoscode.cacao.android.app.model.DayForecast;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class ForecastWeatherAdapter extends FragmentPagerAdapter{
    private int numDays;
    private FragmentManager fm;
    private WeatherForecast forecast;
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public ForecastWeatherAdapter(int numDays, FragmentManager fm, WeatherForecast forecast) {
        super(fm);
        this.numDays = numDays;
        this.fm = fm;
        this.forecast = forecast;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        // We calculate the next days adding position to the current date
        Date d = new Date();
        Calendar gc =  new GregorianCalendar();
        gc.setTime(d);
        gc.add(GregorianCalendar.DAY_OF_MONTH, position);

        return sdf.format(gc.getTime());


    }
    @Override
    public Fragment getItem(int num) {
        DayForecast dayForecast = forecast.getForecast(num);
        DayForecastFragment f = new DayForecastFragment();
        f.setForecast(dayForecast);
        return f;
    }

    //    Number of the days we have the forecast
    @Override
    public int getCount() {
        return numDays;
    }
}
