package com.example.defensecommander;

import android.content.DialogInterface;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ScoresDatabaseHandler implements Runnable {

    private final String TAG = getClass().getSimpleName();

    private final HighScores context;
    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm", Locale.getDefault());

    private int userScore;
    private int level;
    private Connection conn;
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String APP_SCORES = "AppScores";
    private final static String dbName = "chri5558_DB_Sample";
    private final static String dbURL = "jdbc:mysql://christopherhield.com:3306/chri5558_missile_defense";
    private final static String dbUser = "chri5558_student";
    private final static String dbPass = "ABC.123";
    private String finalTop10;
/*
    private final int id;
    private final String name;
    private final double gpa;
*/
    ScoresDatabaseHandler(HighScores ctx, int score, int level) {
        context = ctx;
        this.userScore = score;
        this.level = level;
    }

    public void run() {

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(dbURL, dbUser, dbPass);

            /*
            StringBuilder sb = new StringBuilder();

            Statement stmt = conn.createStatement();


            String sql = "insert into " + APP_SCORES + " values (" +
                    id + ", '" + name + "', " + gpa + ", " +
                    System.currentTimeMillis() + ")";
            Log.d(TAG, "run: " + sql);

            int result = stmt.executeUpdate(sql);

            stmt.close();

            String response = "Student " + name + " added (" + result + " record)\n\n";

            sb.append(response);
            sb.append(getAllTopTen());
*/

            getAllTopTen();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private void getAllTopTen() throws SQLException {


        Statement stmt = conn.createStatement();
        String sql = "select * from " + APP_SCORES + " order by Score desc limit 10";
        StringBuilder sb = new StringBuilder();
        ResultSet rs = stmt.executeQuery(sql);

        int resultsCount= 0;
        int count = 1;
        int lastScore = 0;
        sb.append(String.format(
                "%-12s %-12s %-12s %-12s %-12s%n", "#", "Init", "Level", "Score", "Date/Time"));
        while (rs.next()) {
            resultsCount++;
            long millis = rs.getLong(1);
            String initials = rs.getString(2);
            int score = rs.getInt(3);
            int level = rs.getInt(4);
            System.out.println(millis);
            sb.append(String.format(
                    "%-12d %-12s %-12d %-12d %-12s%n", count, initials, level, score, sdf.format(new Date(millis))));
            count++;
            lastScore = score;
        }

        finalTop10 = sb.toString();

        System.out.println(lastScore);
        System.out.println(userScore);

        if(lastScore <= userScore || resultsCount<10){ //add user score

            context.runOnUiThread(new Runnable() {
                public void run() {
                    //Alert Dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    // Create an edittext and set it to be the builder's view
                    final EditText et = new EditText(context);
                    et.setInputType(InputType.TYPE_CLASS_TEXT);
                    et.setGravity(Gravity.CENTER_HORIZONTAL);
                    builder.setView(et);
                    builder.setView(et);
                    et.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

                    // lambda can be used here (as is below)
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {

                                            Statement stmt1 = conn.createStatement();
                                            String sql1 = "insert into " + APP_SCORES + " values (" +
                                                    System.currentTimeMillis() + ", '" + et.getText() + "', " + userScore + ", " +
                                                    + level + ")";
                                            Log.d(TAG, "run: " + sql1);
                                            int result = stmt1.executeUpdate(sql1);
                                            stmt1.close();

                                            //new list
                                            Statement stmt = conn.createStatement();
                                            String sql = "select * from " + APP_SCORES + " order by Score desc limit 10";
                                            StringBuilder sb = new StringBuilder();
                                            ResultSet rs = stmt.executeQuery(sql);
                                            sb.append(String.format(
                                                    "%-12s %-12s %-12s %-12s %-12s%n", "#", "Init", "Level", "Score", "Date/Time"));


                                            int count = 1;
                                            while (rs.next()) {
                                                long millis = rs.getLong(1);
                                                String initials = rs.getString(2);
                                                int score = rs.getInt(3);
                                                int level = rs.getInt(4);
                                                sb.append(String.format(
                                                        "%-12d %-12s %-12d %-12d %-12s%n", count, initials, level, score, sdf.format(new Date(millis))));
                                                count++;
                                            }
                                            finalTop10 = sb.toString();
                                            conn.close();
                                            context.setResults(finalTop10);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                thread.start();



                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                    // lambda can be used here (as is below)
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

                    builder.setMessage("Please enter your initials (up to 3 characters):");
                    builder.setTitle("You are a Top Player!");

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });

        }
        rs.close();
        stmt.close();
        context.setResults(finalTop10);

    }

}
