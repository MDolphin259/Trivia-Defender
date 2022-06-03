package com.example.triviadefender;

import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ScoreServerCaller {

    public static void ScoreSend(int s){
        RetrofitService retrofitService = new RetrofitService();
        ScoreDataAPI eapi = retrofitService.getRetrofit().create(ScoreDataAPI.class);


        //TODO: May need to change String to something else
        eapi.save(s).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
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

        //TODO: May need to change String to something else
        //Since this is the last thread call, we should call some sort of Method from GA regardless of failure or not
        eapi.getScores().enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, retrofit2.Response<List<Integer>> response) {
                for(int i : response.body()){
                    System.out.println("SCORE: " + i);
                }
            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {
                System.out.println("No Scores Available");
            }
        });
    }
}
