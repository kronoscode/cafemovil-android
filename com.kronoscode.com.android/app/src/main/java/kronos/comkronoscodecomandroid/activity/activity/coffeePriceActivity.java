package kronos.comkronoscodecomandroid.activity.activity;
import kronos.comkronoscodecomandroid.R;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.kronoscode.cafemovil.android.app.model.CoffeeInvestingPrice;
import com.kronoscode.cafemovil.android.app.provider.CoffeePriceProvider;

import org.json.JSONException;

public class coffeePriceActivity extends BaseActivity {
    private TextView price_position;
    private TextView todaysRange;
    private TextView prevClose;
    private TextView open;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_price);

        Toolbar toolbar = findViewById(R.id.coffee_toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.title_activity_coffee));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_home);
        }

        price_position = findViewById(R.id.price_position);
        todaysRange = findViewById(R.id.todayRange);
        prevClose =  findViewById(R.id.prev_close);
        open = findViewById(R.id.open);

        JSONCoffeePriceTask jsonCoffeePriceTask = new JSONCoffeePriceTask();
        jsonCoffeePriceTask.execute();
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

    private class JSONCoffeePriceTask extends AsyncTask<String, Void, CoffeeInvestingPrice> {

        @Override
        protected CoffeeInvestingPrice doInBackground(String... strings) {
            CoffeeInvestingPrice coffeeInvestingPrice = new CoffeeInvestingPrice();
            String data = (new CoffeePriceProvider().getCoffeePrice());
            try {
                coffeeInvestingPrice = JsonCoffeePriceParser.getCoffeePrice(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return coffeeInvestingPrice;
        }

        @Override
        protected void onPostExecute(CoffeeInvestingPrice coffeeInvestingPrice) {
            super.onPostExecute(coffeeInvestingPrice);
            try {
                price_position.setText(coffeeInvestingPrice.coffeePrice.getMonth());
                todaysRange.setText(coffeeInvestingPrice.coffeePrice.getTodaysRange());
                open.setText(coffeeInvestingPrice.coffeePrice.getOpen());
                prevClose.setText("" + coffeeInvestingPrice.coffeePrice.getPrevClose());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
