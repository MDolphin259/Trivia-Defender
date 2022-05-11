package com.example.triviadefender;

import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Random;

public class PopUpHandler {

    private static ArrayList<TriviaQuestion> tql = new ArrayList<TriviaQuestion>();
    private static ArrayList<TriviaQuestion> repeated = new ArrayList<TriviaQuestion>();

    private static boolean popUpActive = false;

    private PopUpHandler(){

    }

    public static void setTrivia(ArrayList<TriviaQuestion> ql){
        tql = ql;
        System.out.println("Questions Loaded: " + ql.size());
    }

    public static void CallPopUp(GameActivity ga){
        if(popUpActive == false){
            popUpActive = true;
            ShowPopUp(ga);
        }
    }

    private static void ShowPopUp(GameActivity ga){
        final String[] abResults = new String[1];
        AlertDialog.Builder ab = new AlertDialog.Builder(ga);

        int index = 0;
        TriviaQuestion puq;
        if(tql.isEmpty() == false){
            index = new Random().nextInt(tql.size() - 1);
            puq = tql.get(index);
            repeated.add(puq);
            tql.remove(puq);
        }
        else{
            index = new Random().nextInt(repeated.size() - 1);
            puq = repeated.get(index);
        }

        String[] answers = new String[puq.getAnswers().size()];
        for(int i = 0; i < puq.getAnswers().size(); i++){
            answers[i] = puq.getAnswers().get(i);
        }
        ab.setTitle(puq.getQuestion());
        ab.setItems(answers, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which){
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
        ad.getWindow().setLayout(1800, 1000);
    }

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

    public static void endPreviousGame(){
        popUpActive = false;
    }

}

