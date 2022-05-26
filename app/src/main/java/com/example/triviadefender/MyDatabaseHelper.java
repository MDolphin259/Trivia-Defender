package com.example.triviadefender;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "HighScores.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "scores";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_SCORE = "score";
    private static final String COLUMN_TIME = "time";
    //public static final String COLUMN_PLAYS = "num_plays";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_SCORE + " TEXT, " +
                        COLUMN_TIME + " INTEGER)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    void addScore(int score, int time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("COLUMN_SCORE", score);
        cv.put("COLUMN_TIME", time);
        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1){
            Toast.makeText(context, "failed to insert", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "SUCCESS!!", Toast.LENGTH_SHORT).show();
        }
    }
}
