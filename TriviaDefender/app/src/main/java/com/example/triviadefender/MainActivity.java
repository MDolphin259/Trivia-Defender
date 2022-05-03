package com.example.triviadefender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * The MainActivity is the first screen that the user sees.
 * It provides 3 levels of difficulty: easy, medium, hard.
 * Once the user picks the difficulty level, the game begins.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Util.getInstance().loadSounds(this);
    }

    public void difficultySelected(View v){
        //difficulty class
        switch (v.getId()) {
            case R.id.easyButton:
                //TODO: easy setup
                break;
            case R.id.mediumButton:
                //TODO: medium setup
                break;
            case R.id.hardButton:
                //TODO: hard setup
                break;
        }
        //Go to GameActivity

        Intent i = new Intent(MainActivity.this, GameActivity.class);
        //TODO: send the difficulty level setup to GameActivity.class
        i.putExtra("DIFFICULTY", 5000);
        startActivity(i);
    }


}