package kronos.comkronoscodecomandroid.activity.activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import com.kronoscode.cacao.android.app.model.CoffeeInvestingPrice;

public class JsonCoffeePriceParser {

    public static CoffeeInvestingPrice getCoffeePrice(String data) throws JSONException{
        CoffeeInvestingPrice coffeeInvestingPrice = new CoffeeInvestingPrice();
        JSONObject jObj = new JSONObject(data);

        coffeeInvestingPrice.coffeePrice.setMonth(getString("Month", jObj));
        coffeeInvestingPrice.coffeePrice.setOpen(getString("Open", jObj));
        coffeeInvestingPrice.coffeePrice.setPrevClose(getFloat("Prev. Close", jObj));
        coffeeInvestingPrice.coffeePrice.setTodaysRange(getString("Todays Range", jObj));
        return coffeeInvestingPrice;

    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }
}
