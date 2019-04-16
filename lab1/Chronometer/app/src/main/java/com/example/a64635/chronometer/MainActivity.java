package com.example.a64635.chronometer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.widget.Button;
import android.widget.Chronometer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View.OnClickListener;

import java.sql.Time;

public class MainActivity extends AppCompatActivity{

    Chronometer ch ,ch2;
    Button  start,pause,reset,stop;
    long lasttime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ch=(Chronometer)findViewById(R.id.chronometer);
        ch2=(Chronometer) findViewById(R.id.chronometer2);
        start = (Button)findViewById(R.id.button1);
        pause = (Button) findViewById(R.id.button2);
        reset = (Button) findViewById(R.id.button3);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lasttime != 0) {
                    ch.setBase(ch.getBase() + (SystemClock.elapsedRealtime() - lasttime));
                }
                else
                    ch.setBase(SystemClock.elapsedRealtime());
                ch.start();
                ch2.setBase(SystemClock.elapsedRealtime());
                ch2.start();
                start.setEnabled(false);
                reset.setEnabled(true);
                pause.setEnabled(true);
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setEnabled(true);
                reset.setEnabled(true);
                pause.setEnabled(false);
                ch.stop();
                ch2.stop();
                lasttime = SystemClock.elapsedRealtime();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setEnabled(true);
                reset.setEnabled(false);
                pause.setEnabled(true);
                ch.stop();
                ch.setBase(SystemClock.elapsedRealtime());
                lasttime = 0;
                ch2.setBase(SystemClock.elapsedRealtime());
                ch2.stop();
            }
        });


    }
}

