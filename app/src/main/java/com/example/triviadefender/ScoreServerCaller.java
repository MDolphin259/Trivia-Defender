package com.example.triviadefender;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ScoreServerCaller {

    public static void ScoreSend(ScoreRecord s){
        RetrofitService retrofitService = new RetrofitService();
        ScoreDataAPI eapi = retrofitService.getRetrofit().create(ScoreDataAPI.class);


        //TODO: May need to change String to something else
        eapi.save(s).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                System.out.println(response.body());
                System.out.println("DATA SENT!");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("DATA NOT SENT!!!");
            }
        });
    }

    public static void ScoresRetrieve(GameActivity ga){

        RetrofitService retrofitService = new RetrofitService();
        ScoreDataAPI eapi = retrofitService.getRetrofit().create(ScoreDataAPI.class);

        ArrayList<ScoreRecord> sr = new ArrayList<>();

        //TODO: May need to change String to something else
        //Since this is the last thread call, we should call some sort of Method from GA regardless of failure or not
        eapi.getScores().enqueue(new Callback<List<ScoreRecord>>() {
            @Override
            public void onResponse(Call<List<ScoreRecord>> call, retrofit2.Response<List<ScoreRecord>> response) {
                System.out.println(response.body().size());
                for(ScoreRecord i : response.body()){
                    System.out.println("SCORE: " + i.getScore());
                    System.out.println("CATEGORY: " + i.getCategory());
                    System.out.println("DIFFICULTY: " + i.getDifficulty());
                    System.out.println("TIME: " + i.getTime());
                    sr.add(i);
                }
                ga.gameOverTransition(sr);
            }

            @Override
            public void onFailure(Call<List<ScoreRecord>> call, Throwable t) {
                System.out.println("No Scores Available");
                ga.gameOverTransition(null);
            }
        });
    }
}
