package com.example.defensecommander;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class HighScores extends AppCompatActivity {
    private int score;
    private int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        Intent intent = getIntent();
        score = intent.getIntExtra("SCORE", 0);
        level = intent.getIntExtra("LEVEL", 0);
        ScoresDatabaseHandler dbh =
                new ScoresDatabaseHandler(this, score, level);
        new Thread(dbh).start();
        fullScreenMode();
    }

    public void setResults(String allTopTen) {
        TextView topPlayersList = findViewById(R.id.topPlayersList);
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                topPlayersList.setText(allTopTen);
            }
        });
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

    public void exit(View v){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }
}