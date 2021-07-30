package com.kronoscode.cafemovil.android.app.model;

public class CoffeeInvestingPrice {
    public CoffeePrice coffeePrice = new CoffeePrice();

    public class CoffeePrice {
        private String month;
        private String open;
        private float prevClose;
        private String todaysRange;

        public String getMonth() { return month; }
        public void setMonth(String month) { this.month = month; }

        public String getOpen() { return open; }
        public void setOpen(String open) { this.open = open; }

        public float getPrevClose() { return prevClose; }
        public void setPrevClose(float prevClose) { this.prevClose = prevClose; }

        public String getTodaysRange() { return todaysRange; }
        public void setTodaysRange(String todaysRange){ this.todaysRange = todaysRange; }

    }

}
