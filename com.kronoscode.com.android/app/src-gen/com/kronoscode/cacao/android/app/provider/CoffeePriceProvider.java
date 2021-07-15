package com.kronoscode.cacao.android.app.provider;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CoffeePriceProvider {
    private static String BASE_URL = "https://ihcafemovil.ihcafe.hn/price-api";
//    private static String BASE_URL = "http://192.168.1.3:8000";
    public String getCoffeePrice(){
        HttpURLConnection con = null ;
        InputStream is = null;

        try {
            con = (HttpURLConnection) ( new URL(BASE_URL)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
//            con.setDoOutput(true);
            con.connect();

            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while (  (line = br.readLine()) != null )
                buffer.append(line + "\\r\\n");

            is.close();
            con.disconnect();
            System.out.println();
            String bufferToString = buffer.toString();
            bufferToString = bufferToString.replaceAll("\"\\{", "{");
            bufferToString = bufferToString.replaceAll("\\}\"", "}");
            bufferToString = bufferToString.replaceAll("\\\\", "");

            return bufferToString;
//            return buffer.toString().replaceAll("\\}\"|\"\\{","");//("{|}")

        } catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Exception e) {e.printStackTrace();}
            try { con.disconnect(); } catch(Exception e) {}
        }
        return null;

    }

}
