package com.example.defensecommander;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 4000;
    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        layout = findViewById(R.id.splashLayout);

        // Possibly check perm's here
        // Possibly load required resources here
        loadSounds();
        fullScreenMode();

        //Alpha transition
        ImageView titleImage = findViewById(R.id.titleImage);
        final ObjectAnimator alpha = ObjectAnimator.ofFloat(titleImage, "alpha", 0.0f, 1f);
        alpha.setInterpolator(new LinearInterpolator());
        alpha.setDuration(3000);
        alpha.start();

        // Handler is used to execute something in the future
        new Handler().postDelayed(() -> {
            // This method will be executed once the timer is over
            // Start your app main activity
            if(checkIfReady()){
                SoundPlayer.getInstance().start("background");
            }

            Intent i =
                    new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            //overridePendingTransition(R.anim.slide_in, R.anim.slide_out); // new act, old act
            // close this activity
            finish();
        }, SPLASH_TIME_OUT);
    }

    private void fullScreenMode() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void loadSounds(){
        SoundPlayer.getInstance().setupSound(this, "background", R.raw.background, true);
        SoundPlayer.getInstance().setupSound(this, "base_blast", R.raw.base_blast, false);
        SoundPlayer.getInstance().setupSound(this, "interceptor_blast", R.raw.interceptor_blast,false);
        SoundPlayer.getInstance().setupSound(this, "interceptor_hit_missile", R.raw.interceptor_hit_missile,false);
        SoundPlayer.getInstance().setupSound(this, "launch_interceptor", R.raw.launch_interceptor,false);
        SoundPlayer.getInstance().setupSound(this, "launch_missile", R.raw.launch_missile,false);
        SoundPlayer.getInstance().setupSound(this, "missile_miss", R.raw.missile_miss,false);
    }

    private boolean checkIfReady() {
       if (SoundPlayer.loadCount != SoundPlayer.doneCount) {
            String msg = String.format(Locale.getDefault(),
                    "Sound loading not complete (%d of %d),\n" +
                            "Please try again in a moment",
                    SoundPlayer.doneCount, SoundPlayer.loadCount);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}