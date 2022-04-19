package com.example.defensecommander;

import static com.example.defensecommander.Interceptor.idVal;
import static com.example.defensecommander.Interceptor.interceptorCount;
import static com.example.defensecommander.MissileMaker.bases;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static int screenHeight;
    public static int screenWidth;
    private ConstraintLayout layout;
    private TextView score, level;
    private MissileMaker missileMaker;
    private int scoreValue;
    private int levelValue = 1;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fullScreenMode();
        getScreenDimensions();
        setupClouds();

        layout = findViewById(R.id.layout);
        score = findViewById(R.id.score);
        level = findViewById(R.id.level);

        layout.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                handleTouch(motionEvent.getX(), motionEvent.getY());
            }
            return false;
        });

        missileMaker = new MissileMaker(this, screenWidth, screenHeight);
        new Thread(missileMaker).start();

    }

    public void handleTouch(float x2, float y2) {
        double screenPortion = screenHeight*0.8;
        if(y2>screenPortion) return;
        if(interceptorCount>2) return;
        ImageView closestBase = null;
        float maxDistance = Float.MAX_VALUE;
        for(ImageView iv: bases){
            double x1 = iv.getX() + (0.5 * iv.getWidth());
            double y1 = iv.getY() + (0.5 * iv.getHeight());
            float f = (float) Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
            if(f<maxDistance) {
                maxDistance = f;
                closestBase = iv;
            }
        }
        if(closestBase!=null){
            Log.d(TAG, "handleTouch: " + maxDistance);
            Interceptor i = new Interceptor(this,  (float) (closestBase.getX()), (float) (closestBase.getY() - 30), x2, y2);
            SoundPlayer.getInstance().start("launch_interceptor");
            i.launch();
        }
    }

    public void increaseLevel() {
        levelValue +=1;
        if(levelValue>20){
            gameOver();
        }
        runOnUiThread(() -> level.setText(String.format(Locale.getDefault(), "Level %d", levelValue)));

    }


    public void removeMissile(Missile m) {
        missileMaker.removeMissile(m);
    }

    public ConstraintLayout getLayout() {
        return layout;
    }

    private void setupClouds() {
        ViewGroup layout = findViewById(R.id.layout);
        new CloudsBackground(this, layout, R.drawable.clouds, 15000);

    }

    private void getScreenDimensions() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
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

    public void applyMissileBlast(Missile missile, int id) {
        missileMaker.applyMissileBlast(missile, id);
    }

    public void incrementScore() {
        scoreValue+=1;
        score.setText(String.format(Locale.getDefault(), "%d", scoreValue));
    }

    public void applyInterceptorHit(Interceptor interceptor, int id) {
        missileMaker.applyInterceptorBlast(interceptor, id);
    }

    public void gameOver() {
        SoundPlayer.getInstance().stop("background");
        missileMaker.setRunning(false);

        ImageView go = findViewById(R.id.gameOver);
        go.setVisibility(View.VISIBLE);
        go.setZ(20);
        final ObjectAnimator alpha = ObjectAnimator.ofFloat(go, "alpha", 0.0f, 1f);
        alpha.setInterpolator(new LinearInterpolator());
        alpha.setDuration(3000);
        alpha.start();
        new Handler().postDelayed(() -> {
            Intent i =
                    new Intent(MainActivity.this, HighScores.class);
            i.putExtra("SCORE", scoreValue);
            i.putExtra("LEVEL", levelValue);
            startActivity(i);


            finish();
        }, 3000);
    }


    public void exitApp(){
        finish();
    }





}