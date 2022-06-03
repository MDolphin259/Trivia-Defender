package com.example.triviadefender;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ScoreDataAPI {

    @GET("/api/getScores")
    Call<List<Integer>> getScores();

    //TODO: CHANGE THE BODY ARGUMENT FROM AN INT TO WHATEVER DATA CLASS WE ESTABLISH
    @POST("/api/sendScore") Call<String> save(@Body int sd);

}
