package com.example.service_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    //public static String CITY = "Denver";
    public static boolean star=false;
    public void Do(){
        registerReceiver(receiver, new IntentFilter(GisService.CHANNEL),RECEIVER_EXPORTED);
        Log.d("Res2","4");
        try
        {
            Intent intent = new Intent(this, GisService.class);
            startService(intent);
        }
        catch (Exception e){
            Error();
        }
    }
    public void Error(){
        Toast.makeText(this,"YOU SHOULD TURN ON INTERNET",Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText ed=findViewById(R.id.ed);
        ed.setText("Los Angeles");
        /*ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //TextView t2=findViewById(R.id.location);
                String cit = ed.getText().toString();
                HTTPRequest.CITY=cit;
                Log.d("Res",cit);
                ed.setBackgroundColor(R.color.white);

            }
        });*/


        Log.d("Res2","2");
        while (star){
            star=false;
            registerReceiver(receiver, new IntentFilter(GisService.CHANNEL),RECEIVER_EXPORTED);
            Log.d("Res2","4");
            Intent intent = new Intent(this, GisService.class);
            startService(intent);
        }
        Button butt=findViewById(R.id.butt);
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Res2","3");
                HTTPRequest.CITY= ed.getText().toString();
                Log.d("Res2",HTTPRequest.CITY);
                Do();
                star=true;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, GisService.class);
        stopService(intent);
    }
    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {

                Log.d("RESULT",intent.getStringExtra(GisService.INFO));
                String response = intent.getStringExtra(GisService.INFO);
                JSONObject start = new JSONObject(response);
                JSONObject main = start.getJSONObject("main");
                Double tempf= Double.parseDouble(main.getString("temp"))*1.8+32;
                String temp = main.getString("temp") +" C";
                String tempsf= Math.ceil(tempf*100)/100+" F";
                TextView t1= findViewById(R.id.temp);
                t1.setText(temp);
                TextView t6= findViewById(R.id.tempf);
                t6.setText(tempsf);
                JSONArray weather = start.getJSONArray("weather");
                Log.d("Res1","1");
                JSONObject wind = start.getJSONObject("wind");
                JSONObject sys = start.getJSONObject("sys");
                Log.d("Res1","3");
                String location = sys.getString("country")+", "+start.getString("name");
                Log.d("Res1","2");
                String wi = wind.getString("speed")+" Km/H "+wind.getString("deg")+" degrees";

                String we = weather.toString(0).substring(weather.toString(0).indexOf("description")+15,weather.toString(0).indexOf("icon")-4);

                String pressure = "pressure: "+ main.getString("pressure")+" humidity: "+main.getString("humidity");
                TextView t2=findViewById(R.id.location);
                TextView t5=findViewById(R.id.pressure);
                TextView t3=findViewById(R.id.wind);
                TextView t4=findViewById(R.id.weather);
                t2.setText(location);
                t3.setText(wi);
                t4.setText(we);
                t5.setText(pressure);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            } catch (Exception e){
                TextView t2=findViewById(R.id.location);
                t2.setText("THIS CITY DOESN'T EXIST");
            }
        }
    };
}