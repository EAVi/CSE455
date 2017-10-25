package com.example.captain.schedit;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen config = new EasySplashScreen(SplashScreen.this)
        .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(5000)
                .withBackgroundColor(Color.parseColor("#ECF0F1"))
                .withLogo(R.drawable.sch)
                .withHeaderText("Welcome to Schedit")
                .withFooterText("2017")
                .withBeforeLogoText("by SAAVFOXDEV")
                .withAfterLogoText("Start scheduling");


                //text color

        config.getHeaderTextView().setTextColor(Color.WHITE);
        config.getFooterTextView().setTextColor(Color.WHITE);
        config.getAfterLogoTextView().setTextColor(Color.WHITE);
        config.getBeforeLogoTextView().setTextColor(Color.WHITE);

        //set view

        View view = config.create();

        //set view to content

        setContentView(view);

    }
}
