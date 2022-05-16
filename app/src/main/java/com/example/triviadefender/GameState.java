package com.example.triviadefender;

//This is the class that is used to handle pausing and resuming some of the game items

public class GameState {

    private static boolean pause = false;

    private GameState() {}

    //Method to check whether the game is at a paused state
    public static boolean checkPause() {return pause;}

    //Used to pause items on the screen. Right now this is just some projectiles
    public static void callPauseState(MissileMaker mm, QuestionMaker qm){
        mm.pauseMissiles();
        qm.pauseQuestions();
        changeState();
    }

    //Used to resume items on the screen.
    public static void callResumeState(MissileMaker mm, QuestionMaker qm){
        mm.resumeMissiles();
        qm.resumeQuestions();
        changeState();
    }

    //Changes the state of the game
    private static void changeState(){
        if(pause == true){
            pause = false;
        }
        else{
            pause = true;
        }
    }


}
