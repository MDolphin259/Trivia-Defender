package com.example.triviadefender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
    }

    //This is the method that will send Users back from a game over page to a home page
    public void sendMessage(View view){
        GameState.resetGameState();
        PopUpHandler.endPreviousGame();
        Util.difficultyToQuestionList.clear();
        Intent intent = new Intent(this, MainActivity.class);
        FirebaseAnalyticsSender.playAgainPressed(this);
        startActivity(intent);
    }
}