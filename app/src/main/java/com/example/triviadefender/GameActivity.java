package com.example.triviadefender;

import static com.android.volley.VolleyLog.TAG;
import static com.example.triviadefender.CannonFire.activeShotCount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class GameActivity extends AppCompatActivity {
    public static int screenHeight;
    public static int screenWidth;
    private ConstraintLayout layout;
    private MissileMaker missileMaker;
    private QuestionMaker questionMaker;
    private TextView scoreText, scoreValue;
    private int score = 0;
    private boolean allBasesGone;
    private int questionMarkScore;
    private int previous10 = 10;
    private long start;
    private long finish;
    private String categoryID;

    //Instantiate list of available cannons
    public static ArrayList<ImageView> activeCannons = new ArrayList<>();


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        start = System.nanoTime();

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
         questionMarkScore = i.getIntExtra("DIFFICULTY",1);
         System.out.println(questionMarkScore);
        System.out.println("---------------------HHEEELLLOOOO-------------------------");

        categoryID = i.getStringExtra("CATEGORY");

        //Gets the Questions from the intent
        //ArrayList<TriviaQuestion> ql = (ArrayList<TriviaQuestion>) i.getSerializableExtra("QUESTIONS");
        //System.out.println("Questions Loaded: " + ql.size());
        //PopUpHandler.setTrivia(ql); //retrieves the question list

        Util util = Util.getInstance();
        util.fullScreenMode(this);
        Util.ScreenWidthHeight swh = util.getScreenDimensions(this);
        screenHeight = swh.getHeight();
        screenWidth = swh.getWidth();
        layout = findViewById(R.id.gameLayout);

        System.out.println(previous10);

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


    //method to increment the score using a boolean flag to
    //detect type of icon object
    public void incrementScore(boolean missileFlag){
        //if bullet collides with missile
        if (missileFlag == true) {
            score += 1;
        }

        //call questionMarkScoring to detect scoring based on difficulty level and if
        //user answered question correct to update the score -- call from PopUpHandler method

        if(missileFlag == false) {
            score+=questionMarkScore;
        }

        //Log to the console
        Log.i("score",String.valueOf(score));
        scoreValue.setText(String.valueOf(score));
    }

    public boolean scoreCalc(){
        // if score is above the next interval of 10, decrease delay
        if (score >= previous10) {
            previous10 += 10;
            return true;
        }
        return false;
    }


    public void handleTouch(float x2, float y2) {
        double screenPortion = screenHeight*0.8;
        if(y2>screenPortion) return;
        System.out.println("we are in handleTouch");

        //interceptor count refers to the number of active shots fired at missiles
        //Is reduced if the shot connects with a missile
        if(activeShotCount>2) return;
        ImageView closestCannon = null;
        float maxDistance = Float.MAX_VALUE;
        for(ImageView iv: activeCannons){
            double x1 = iv.getX() + (0.5 * iv.getWidth());
            double y1 = iv.getY() + (0.5 * iv.getHeight());
            float f = (float) Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
            if(f<maxDistance) {
                maxDistance = f;
                closestCannon = iv;

            }
        }
        if(closestCannon!=null){
            Log.d(TAG, "handleTouch: " + maxDistance);
            CannonFire i = new CannonFire(this,  (float) (closestCannon.getX()), (float) (closestCannon.getY() - 30), x2, y2);
            SoundPlayer.getInstance().start("launch_interceptor");
            i.launch();

        }
    }

    public boolean checkAllBasesGone(){
        return allBasesGone;
    }

    public void stopGame() {
        questionMaker.setRunning(false);
        missileMaker.setRunning(false);

        finish = System.nanoTime();
        long time = finish - start;
        long convert = TimeUnit.SECONDS.convert(time, TimeUnit.NANOSECONDS);
        System.out.println("TIME in Seconds: " + convert);

        ScoreRecord sr = new ScoreRecord();
        sr.setScore(score);
        sr.setTime(convert + " seconds");
        sr.setCategory(categoryID);
        sr.setDifficulty(questionMarkScore);

        ScoreServerCaller.ScoreSend(sr);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ScoreServerCaller.ScoresRetrieve(GameActivity.this);

        //TODO: May need to move 2 lines into a new function that waits for the threads used to talk to Score API
        //Intent i = new Intent(GameActivity.this, GameOverActivity.class);
        //startActivity(i);
    }

    //pauseGame and resumeGame are used as middle man classes for QuestionMaker since it has no access to missileMaker
    //I thought it would be good to set up this way to not put too much code in GameActivity
    public void pauseGame(){
        GameState.callPauseState(missileMaker, questionMaker);
    }

    public void resumeGame(){
        GameState.callResumeState(missileMaker, questionMaker);
    }

    public void applyInterceptorHit(CannonFire cannonFire, int id) {
        missileMaker.applyInterceptorBlast(cannonFire, id);
        questionMaker.applyInterceptorBlast(cannonFire, id); //To check if any questions got hit
    }
    public ArrayList<ImageView> getActiveCannons() {
        return activeCannons;
    }
    public int getDifficulty() {
            Intent i = getIntent();
            int difficulty = i.getIntExtra("DIFFICULTY",1);
            return difficulty;
    }

    public void gameOverTransition(ArrayList<ScoreRecord> ScoreList){
        Intent i = new Intent(GameActivity.this, GameOverActivity.class);
        i.putExtra("SCORES", ScoreList);
        startActivity(i);
    }
}
