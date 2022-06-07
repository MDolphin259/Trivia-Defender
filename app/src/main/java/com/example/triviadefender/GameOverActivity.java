package com.example.triviadefender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class GameOverActivity extends AppCompatActivity {

    ArrayList<ScoreRecord> sd = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        TextView scoringText = findViewById(R.id.ScoreList);

        Intent i = getIntent();
        ArrayList<ScoreRecord> intentList = (ArrayList<ScoreRecord>) i.getSerializableExtra("SCORES");
        sd = intentList;

        StringBuilder sb = new StringBuilder();

        if(sd == null){
            sb.append("No Scores Available");
        }
        else{
            //TODO: add code to display scores retrieved from sd
            sb.append(String.format("%-12s %-12s %-12s %-12s \n", "Score", "Category", "Difficulty", "time"));
            for(ScoreRecord score: sd){
                String c = score.getCategory();
                int s = score.getScore();
                int d = score.getDifficulty();
                String t = score.getTime();

                sb.append(String.format("%-12s %-12s %-12s %-12s \n", s, c, d, t));
            }
        }

        String listing = sb.toString();
        scoringText.setText(listing);

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