package com.example.triviadefender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.triviadefender.R;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
    }

    //This is the method that will send Users back from a game over page to a home page
    public void sendMessage(View view){
        PopUpHandler.endPreviousGame(); //TODO: Will bring up with later update
        //TODO: Questions do not pop up when remaining missiles have not done animation after game is over. Probably got to activate cancel animator
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}