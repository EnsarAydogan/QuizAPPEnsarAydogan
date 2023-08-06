package com.example.quiz;

public class ScoreData {
    private String username;
    private int score;

    public ScoreData(String username, int score) {
        this.username = username;
        this.score = score;
    }
    //Wiedergabe von Score und Username
    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }
}

