package com.example.triviadefender;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

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
import java.util.HashMap;

public class Util {
    private static Util instance;
    private static HashMap<String, String> catToNum = new HashMap<>();
    public static HashMap<String, ArrayList<TriviaQuestion>> difficultyToQuestionList = new HashMap<>();

    public static Util getInstance() {
        if (instance == null)
            instance = new Util();
        return instance;
    }

    public HashMap<String,String> getCatToNum(){
        return catToNum;
    }

    public void populateQuestionsByCategory(Activity activity){
        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());

        //Sets up URL for request
        String URL = "https://opentdb.com/api_category.php";
        Uri.Builder buildURL = Uri.parse(URL).buildUpon();
        String urlToUse = buildURL.build().toString();

        //Used to determine what to do with a successful response
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray triviaQuestions = response.getJSONArray("trivia_categories");
                    for(int i=0; i<triviaQuestions.length(); i++){
                        String id = ((JSONObject)triviaQuestions.get(i)).getString("id");
                        String name = (((JSONObject)triviaQuestions.get(i)).getString("name"));
                        catToNum.put(name,id);
                    }
                    System.out.println(catToNum);

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

    /**
     * Remove all System UI elements from the screen
     * @param activity
     */
    public void fullScreenMode(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    /**
     * Returns the screen width and height
     * @param activity
     */
    public ScreenWidthHeight getScreenDimensions(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return new ScreenWidthHeight(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    /**
     * Loads all sounds at the beginning
     */
    public void loadSounds(Activity activity){
        SoundPlayer.getInstance().setupSound(activity, "background", R.raw.background, true);
        SoundPlayer.getInstance().setupSound(activity, "base_blast", R.raw.base_blast, false);
        SoundPlayer.getInstance().setupSound(activity, "interceptor_blast", R.raw.interceptor_blast,false);
        SoundPlayer.getInstance().setupSound(activity, "interceptor_hit_missile", R.raw.interceptor_hit_missile,false);
        SoundPlayer.getInstance().setupSound(activity, "launch_interceptor", R.raw.launch_interceptor,false);
        SoundPlayer.getInstance().setupSound(activity, "launch_missile", R.raw.launch_missile,false);
        SoundPlayer.getInstance().setupSound(activity, "missile_miss", R.raw.missile_miss,false);
    }

    public void storeByDifficulty(ArrayList<TriviaQuestion> questionList) {
        for(TriviaQuestion tq: questionList){
            String difficulty = tq.getDifficulty();
            if(!difficultyToQuestionList.containsKey(difficulty)){
                difficultyToQuestionList.put(difficulty,new ArrayList<TriviaQuestion>());
            }
            difficultyToQuestionList.get(difficulty).add(tq);
        }
    }

    public class ScreenWidthHeight{
        private int width;
        private int height;

        ScreenWidthHeight(int width, int height){
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}
