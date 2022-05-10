package com.example.triviadefender;

import static com.android.volley.VolleyLog.TAG;
import static com.example.triviadefender.CannonFire.activeShotCount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.Log;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    public static int screenHeight;
    public static int screenWidth;
    private ConstraintLayout layout;
    private MissileMaker missileMaker;
    private QuestionMaker questionMaker;
    private TextView scoreText, scoreValue;
    private int score = 0;
    private boolean allBasesGone;

    //Instantiate list of available cannons
    public static ArrayList<ImageView> activeCannons = new ArrayList<>();


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Instantiate cannons on create
        ImageView cannon1 = findViewById(R.id.Cannon1);
        ImageView cannon2 = findViewById(R.id.Cannon2);
        ImageView cannon3 = findViewById(R.id.Cannon3);

        //Add available cannons to list of available cannons
        activeCannons.add(cannon1);
        activeCannons.add(cannon2);
        activeCannons.add(cannon3);

        //Get intent from MainActivity to set game difficulty
        Intent i = getIntent();
        int in = i.getIntExtra("DIFFICULTY",0);
        System.out.println(in);

        //Gets the Questions from the intent
        ArrayList<TriviaQuestion> ql = (ArrayList<TriviaQuestion>) i.getSerializableExtra("QUESTIONS");
        System.out.println("Questions Loaded: " + ql.size());
        PopUpHandler.setTrivia(ql);

        Util util = Util.getInstance();
        util.fullScreenMode(this);
        Util.ScreenWidthHeight swh = util.getScreenDimensions(this);
        screenHeight = swh.getHeight();
        screenWidth = swh.getWidth();
        layout = findViewById(R.id.gameLayout);

        //Print score text to the screen -- using activity_game.xml
        scoreText = (TextView) findViewById(R.id.scoreText);
        scoreValue = (TextView) findViewById(R.id.scoreValue);
        scoreValue.setText("0");

        //TODO: fix the handler to recognize touches on the screen

        layout.setOnTouchListener((view, motionEvent) -> {
            System.out.println("we are in setOnTouchListener");
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                handleTouch(motionEvent.getX(), motionEvent.getY());
            }
            return false;
        });