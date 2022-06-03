package com.example.triviadefender;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseAnalyticsSender {

    private static FirebaseAnalytics fb;

    public static void sendOpened(AppCompatActivity aca){

        fb = FirebaseAnalytics.getInstance(aca.getApplicationContext());
        Bundle started = new Bundle();
        started.putString("Game_Opened", "open!");
        fb.logEvent("Game_Opened", started);

    }

    public static void playAgainPressed(AppCompatActivity aca){
        fb = FirebaseAnalytics.getInstance(aca.getApplicationContext());
        Bundle playagain = new Bundle();
        playagain.putString("Play_Again", "playing again!");
        fb.logEvent("Play_Again_Pressed", playagain);
    }

}
