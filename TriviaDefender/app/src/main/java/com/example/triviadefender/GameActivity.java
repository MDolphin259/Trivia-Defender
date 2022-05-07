package com.example.triviadefender;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.Log;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

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


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent i = getIntent();
        int in = i.getIntExtra("DIFFICULTY",0);
        System.out.println(in);

        //Gets the Questions from the intent
        ArrayList<TriviaQuestion> ql = (ArrayList<TriviaQuestion>) i.getSerializableExtra("QUESTIONS");
        System.out.println("Questions Loaded: " + ql.size());

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
        /*
        layout.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                handleTouch(motionEvent.getX(), motionEvent.getY());
            }
            return false;
        });
         */
        allBasesGone = false;

        missileMaker = new MissileMaker(this, screenWidth, screenHeight);
        new Thread(missileMaker).start();
        questionMaker = new QuestionMaker(this, screenWidth, screenHeight);
        new Thread(questionMaker).start();

    }

    public ConstraintLayout getLayout() {
        return layout;
    }
    public void removeMissile(Missile m) {
        missileMaker.removeMissile(m);
    }
    public void removeQuestion(Question q) {
        questionMaker.removeQuestion(q);
    }
    public void applyMissileBlast(Missile missile, int id) {
        missileMaker.applyMissileBlast(missile, id);
    }
    //method to increment the score
    public void incrementScore(){
        score+=1;
        //Log to the console
        Log.i("score",String.valueOf(score));
        scoreValue.setText(String.valueOf(score));
    }


    public void handleTouch(float x2, float y2) {
        /*
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

         */
    }

    public boolean checkAllBasesGone(){
        return allBasesGone;
    }

    //TODO: remove this so that the game end by itself
    public void stopGame() {
        questionMaker.setRunning(false);
        missileMaker.setRunning(false);
        Intent i = new Intent(GameActivity.this, GameOverActivity.class);
        startActivity(i);
    }
}