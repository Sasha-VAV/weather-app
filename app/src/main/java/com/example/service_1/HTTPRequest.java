package com.example.service_1;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class HTTPRequest implements Runnable{
    public static String api = "https://api.openweathermap.org/data/2.5/weather?lat=37.27&lon=-107.88&appid=14357d947032c5d34b3ff761ddad9e84&units=metric";
    public static final String APIKEY = "ca6188f9ad6b3fd6e07ca0fcef77a139";
    public static String CITY = "Denver";
    //public static Exception e;
    public static boolean ex=false;
    private Handler handler;
    private URL url;
    public HTTPRequest(Handler h) {
        this.handler=h;
        try {
            String s="https://api.openweathermap.org/data/2.5/weather?q="+CITY+"&appid="+APIKEY+"&units=metric";
            Log.d("Res2",s);
            //s="https://rapidapi.com/blog/access-global-weather-data-with-these-weather-apis/";
            this.url=new URL(s);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            StringBuilder response = new StringBuilder();
            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            Scanner in = new Scanner(reader);
            while (in.hasNext()) {
                response.append(in.nextLine());
            }
            in.close();
            //response.append("{\"coord\":{\"lon\":-107.88,\"lat\":37.27},\"weather\":[{\"id\":741,\"main\":\"Fog\",\"description\":\"fog\",\"icon\":\"50d\"}],\"base\":\"stations\",\"main\":{\"temp\":1.02,\"feels_like\":1.02,\"temp_min\":-0.79,\"temp_max\":3.03,\"pressure\":1021,\"humidity\":96},\"visibility\":805,\"wind\":{\"speed\":0,\"deg\":0},\"clouds\":{\"all\":100},\"dt\":1702568230,\"sys\":{\"type\":2,\"id\":2008074,\"country\":\"US\",\"sunrise\":1702563456,\"sunset\":1702598089},\"timezone\":-25200,\"id\":5420241,\"name\":\"Durango\",\"cod\":200}");
            connection.disconnect();
            Message msg = Message.obtain();
            msg.obj = response.toString();
            handler.sendMessage(msg);
        }catch (Exception e){
            ex=true;
        }
    }
}
