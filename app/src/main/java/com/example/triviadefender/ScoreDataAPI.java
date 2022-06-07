package com.example.triviadefender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ScoreDataAPI {

    @GET("/api/getScores")
    Call<List<ScoreRecord>> getScores();

    //TODO: CHANGE THE BODY ARGUMENT FROM AN INT TO WHATEVER DATA CLASS WE ESTABLISH
    @POST("/api/postScore")
    Call<String> save(@Body ScoreRecord sd);

}
