package com.example.triviadefender;

import android.content.DialogInterface;

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
    public static void setTrivia(ArrayList<TriviaQuestion> ql){
        tql = ql;
        System.out.println("Questions Loaded: " + ql.size());
    }

    //Method to activate a popup. This also checks if another pop up is active already
    //We check if a pop up is already active so that a User does not deal with a case that they are spammed with pop ups
    public static void CallPopUp(GameActivity ga){
        if(popUpActive == false){
            popUpActive = true;
            ShowPopUp(ga);
        }
    }

    //This is the method to create the popup with the question and the answers
    private static void ShowPopUp(GameActivity ga){
        final String[] abResults = new String[1];
        AlertDialog.Builder ab = new AlertDialog.Builder(ga);

        int index = new Random().nextInt(tql.size() - 1);
        TriviaQuestion puq = tql.get(index);;


        String[] answers = new String[puq.getAnswers().size()];
        for(int i = 0; i < puq.getAnswers().size(); i++){
            answers[i] = puq.getAnswers().get(i);
        }
        ab.setTitle(puq.getQuestion());
        ab.setItems(answers, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which){ //USed to handle which item was selected
                String item = answers[which];
                String results = item + " is Correct!";
                if(item.equals(puq.getCorrect()) == false){
                    results = item + " is Wrong. The answer is " + puq.getCorrect();
                }
                abResults[0] = results;
                System.out.println(results);
                callSecond(abResults[0], ga);
            }
        });
        ab.setCancelable(false);
        AlertDialog ad = ab.create();
        ad.show();
        ad.getWindow().setLayout(1800, 1000); //needed since questions may not fit screen with default
    }

    //This is the method to call a second pop up to let Players know if they got the question right
    private static void callSecond(String results, GameActivity ga) {
        AlertDialog.Builder ab2 = new AlertDialog.Builder(ga);
        ab2.setTitle(results);
        ab2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                popUpActive = false;
            }
        });
        ab2.setCancelable(false);
        ab2.show();

    }

    //When the game is over, we want to change this variable back to false in case Players want to play again
    public static void endPreviousGame(){
        popUpActive = false;
    }

}

