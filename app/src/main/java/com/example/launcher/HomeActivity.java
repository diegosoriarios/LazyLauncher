package com.example.launcher;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends Activity {
    TextView txtTime,txtDate;
    Calendar c;
    SimpleDateFormat simpleDateFormat;
    SimpleDateFormat simpleTimeFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        simpleTimeFormat = new SimpleDateFormat("hh:mm a");
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtDate = (TextView) findViewById(R.id.txtDate);
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        c = Calendar.getInstance();
                        txtDate.setText(simpleDateFormat.format(c.getTime()));
                        txtTime.setText(simpleTimeFormat.format(c.getTime()));
                    }

                });

            }
        }, 0, 10);
    }

    public void showApps(View v) {
        Intent i = new Intent(HomeActivity.this, GetApps.class);
        startActivity(i);
    }


}