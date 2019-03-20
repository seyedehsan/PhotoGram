package com.example.mobileapplicationproject;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.images.ImageManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Console;
import java.util.logging.ConsoleHandler;

public class MainActivity extends AppCompatActivity {
    private ImageView imgLogo;
    private static int DELAY_TIME_OUT = 5000;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgLogo = (ImageView)findViewById(R.id.imgLogo);

        //Display Logo using Animation
        Animation fadeIn = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        imgLogo.startAnimation(fadeIn);


        //Create new Handler to set a delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },DELAY_TIME_OUT);
    }
}
