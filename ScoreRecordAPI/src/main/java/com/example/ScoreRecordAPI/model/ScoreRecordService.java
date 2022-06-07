package com.example.ScoreRecordAPI.model;

import com.google.firebase.database.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ScoreRecordService {

    //private ArrayList<ScoreRecord> dummy = new ArrayList<>();

    public String sendScore(ScoreRecord score){
        //dummy.add(score);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbr = db.getReference(ScoreRecord.class.getSimpleName());

        dbr.push().setValueAsync(score);
        return "OK";
    }

    public ArrayList<ScoreRecord> getScores(){

        ArrayList<ScoreRecord> sd = new ArrayList<>();

        final boolean[] wait = {true};

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbr = db.getReference(ScoreRecord.class.getSimpleName());
        dbr.orderByChild("score").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot item:dataSnapshot.getChildren()) {
                    ScoreRecord s = item.getValue(ScoreRecord.class);
                    System.out.println("NEW: " + s.getScore());
                    sd.add(s);
                }
                wait[0] = false;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.toString());
            }
        });

        //Need to wait for the async thread to finish
        while(wait[0]){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return sd;
    }

}
