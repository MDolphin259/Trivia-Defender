package com.example.triviadefender;

import android.content.DialogInterface;
import android.widget.ImageView;
import android.graphics.Color;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Random;

//This class is used to deal with all things related to pop ups

public class PopUpHandler {

    private static ArrayList<TriviaQuestion> tql = new ArrayList<TriviaQuestion>();

    private static boolean popUpActive = false; //TODO: This was used during testing. The variable may not be needed and should be discussed about

    private PopUpHandler(){
    }

    //Gets the list of Trivia Questions as its own
    /*public static void setTrivia(ArrayList<TriviaQuestion> ql){
        tql = ql;
    }*/

    //Method to activate a popup. This also checks if another pop up is active already
    //We check if a pop up is already active so that a User does not deal with a case that they are spammed with pop ups
    public static void CallPopUp(GameActivity ga){
        if(popUpActive == false){
            popUpActive = true;
            ga.pauseGame(); //The game is about to create a pop up that will fill the screen so we need to pause projectiles
            ShowPopUp(ga);
        }
    }

    //This is the method to create the popup with the question and the answers
    private static void ShowPopUp(GameActivity ga){
        AlertDialog.Builder ab = new AlertDialog.Builder(ga);

        //Selects a random question to use

        TriviaQuestion puq;
        double probability = Math.random();
        if(probability<0.1){
            puq = Util.difficultyToQuestionList.get("hard").get(new Random().nextInt(Util.difficultyToQuestionList.get("hard").size() - 1));
        }else if(probability<0.35){
            puq = Util.difficultyToQuestionList.get("medium").get(new Random().nextInt(Util.difficultyToQuestionList.get("medium").size() - 1));
        }else{
            puq = Util.difficultyToQuestionList.get("easy").get(new Random().nextInt(Util.difficultyToQuestionList.get("easy").size() - 1));
        }
        System.out.println(puq.getDifficulty());
        //Used to make an array from an ArrayList since that is what the pop up methods for android uses
        String[] answers = new String[puq.getAnswers().size()];
        for(int i = 0; i < puq.getAnswers().size(); i++){
            answers[i] = puq.getAnswers().get(i);
        }

        //Make Title
        TextView tv = new TextView(ga);
        tv.setText(puq.getQuestion());
        tv.setTextSize(20);
        tv.setPadding(40,40,10,10);
        tv.setTextColor(Color.BLACK);
        ab.setCustomTitle(tv);

        //This section is to handle when Players choose one of the options in the trivia question
        TriviaQuestion finalPuq = puq;
        ab.setItems(answers, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which){ //USed to handle which item was selected
                String item = answers[which];

                String results = item + " is Wrong. The answer is " + finalPuq.getCorrect();

                //if answer to question is correct --- call incrementScore
                if(item.equals(finalPuq.getCorrect()) == true){
                    results = item + " is Correct!";
                    ga.incrementScore(false);
                }


                System.out.println(results);
                callSecond(results, ga);
            }
        });

        //These lines are used to show the trivia question pop up
        ab.setCancelable(false);
        AlertDialog ad = ab.create();
        ad.show();
        ad.getWindow().setLayout(1800, 1000); //needed since questions may not fit screen with default
    }

    //This is the method to call a second pop up to let Players know if they got the question right
    private static void callSecond(String results, GameActivity ga) {
        AlertDialog.Builder ab2 = new AlertDialog.Builder(ga);
        ab2.setTitle(results);

        //Line to set up result pop up
        ab2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ga.resumeGame(); //After a player presses okay the pop up is gone so we need to resume projectiles
                popUpActive = false;
                if (results.contains("Wrong")) {
                    ArrayList<ImageView> activeCannons = ga.getActiveCannons();
                    //ImageView unluckyCannon = activeCannons.get(0);
                    ga.getLayout().removeView(activeCannons.get(0));
                    activeCannons.remove(0);
                }
            }
        });

        //These lines are used to show the results pop up
        ab2.setCancelable(false);
        ab2.show();

    }

    //When the game is over, we want to change this variable back to false in case Players want to play again
    public static void endPreviousGame(){
        popUpActive = false;
    }

}

