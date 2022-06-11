package com.example.lbwapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.launchView).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {//轻点Touch屏幕，跳转到主页
//                Intent intent = new Intent(MainActivity.this, PhotoList.class);
//                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                Intent intent = new Intent(MainActivity.this, TakePhotos.class);
                startActivity(intent);
                return true;
            }
        });
//        //Click实现跳转
//        findViewById(R.id.launchView).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, PhotoList.class);
//                startActivity(intent);
//            }
//        });
//        //timer和timerTask实现跳转
//        Timer timer = new Timer();
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(MainActivity.this, PhotoList.class);
//                startActivity(intent);
//            }
//        };
//        timer.schedule(timerTask,2000);
    }
}