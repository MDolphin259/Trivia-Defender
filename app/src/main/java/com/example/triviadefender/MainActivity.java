package com.example.triviadefender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The MainActivity is the first screen that the user sees.
 * It provides 3 levels of difficulty: easy, medium, hard.
 * Once the user picks the difficulty level, the game begins.
 */
public class MainActivity extends AppCompatActivity {

    public ArrayList<TriviaQuestion> qList = new ArrayList<TriviaQuestion>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Util.getInstance().loadSounds(this);
    }

    public void difficultySelected(View v) {
        //difficulty class
        switch (v.getId()) {
            case R.id.easyButton:
                //TODO: easy setup
                break;
            case R.id.mediumButton:
                //TODO: medium setup
                break;
            case R.id.hardButton:
                //TODO: hard setup
                break;
        }
        //Calls the API After a button is pressed
        //We can add parameters such as the difficulty level later if needed
        CallAPI();
    }

    //Calls the API
    private void CallAPI(){
        RequestQueue queue = Volley.newRequestQueue(this.getApplicationContext());

        //Sets up URL for request
        String URL = "https://opentdb.com/api.php";
        Uri.Builder buildURL = Uri.parse(URL).buildUpon();
        buildURL.appendQueryParameter("amount", "10"); //to set up an amount
        buildURL.appendQueryParameter("category", "23"); //to set up a category
        String urlToUse = buildURL.build().toString();
        ArrayList<TriviaQuestion> newsList = new ArrayList<TriviaQuestion>();

        //Used to determine what to do with a successful response
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray triviaQuestions = response.getJSONArray("results");
                    for(int i=0; i<triviaQuestions.length(); i++){
                        String q = ((JSONObject)triviaQuestions.get(i)).getString("question");
                        String c = (((JSONObject)triviaQuestions.get(i)).getString("correct_answer"));
                        JSONArray a = (((JSONObject)triviaQuestions.get(i)).getJSONArray("incorrect_answers"));
                        ArrayList<String> al = new ArrayList<String>();
                        al.add(c);
                        for(int j = 0; j < a.length(); j++){
                            String answer = a.get(j).toString();
                            al.add(answer);
                        }
                        Collections.shuffle(al);
                        TriviaQuestion news = new TriviaQuestion(q,c,al);
                        newsList.add(news);
                    }
                    //After we are done adding questions, we send the list to be added to qList
                    addQuestionList(newsList);
                } catch (Exception e) {
                    System.out.println("Error in response Volley");
                    e.printStackTrace();
                }
            }
        };

        //Used to handle with errors with request later
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                    System.out.println("Error Listener Invoked");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("Error Listener Invoked");
            }
        };

        //Make the JsonObjectRequest
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error);

        //Add Request to queue to start
        queue.add(jsonObjectRequest);
    }

    //Establishes an ArrayList for qlist first before launching the game
    private void addQuestionList(ArrayList<TriviaQuestion> q){
        this.qList = q;
        launchGame();
    }

    private void launchGame(){
        Intent i = new Intent(MainActivity.this, GameActivity.class);
        //TODO: send the difficulty level setup to GameActivity.class
        i.putExtra("DIFFICULTY", 5000);
        i.putExtra("QUESTIONS", qList); //sends the list of trivia questions to the GameActivity

        startActivity(i);
    }
}