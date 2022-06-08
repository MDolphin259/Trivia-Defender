package com.example.triviadefender;

public class ScoreRecord implements java.io.Serializable {

    private int score;
    private String time;
    private String category;
    private int difficulty;

    public ScoreRecord(){}

    public int getScore() {
        return score;
    }

    public void setScore(int count) {
        this.score = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

}
