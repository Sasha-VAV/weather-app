package com.example.service_1;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.security.Provider;
import java.util.List;
import java.util.Map;

public class GisService extends Service {

    public static final String CHANNEL = "GIS_SERVICE";
    public static final String INFO = "INFO";
    private Handler h;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Ress1","1");
        h = new Handler(Looper.getMainLooper()){

            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.d("Ress2","2");
                super.handleMessage(msg);

                String responce =(String) msg.obj;
                Intent i = new Intent(CHANNEL);
                i.putExtra(INFO,responce);
                sendBroadcast(i);

            }
        };
        Log.d("Ress2","3");
        //Toast.makeText(this, "Служба создана",Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Toast.makeText(this, "Служба запущена",Toast.LENGTH_SHORT).show();
        try
        {
            Thread weatherThread = new Thread(new HTTPRequest(h));
            weatherThread.start();
            if (HTTPRequest.ex){
                Toast.makeText(this,"YOU SHOULD TURN ON INTERNET",Toast.LENGTH_LONG).show();
                HTTPRequest.ex= false;
            }

        }
        catch (Exception e){
            Toast.makeText(this,"YOU SHOULD TURN ON INTERNET",Toast.LENGTH_LONG).show();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
