package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
public class MyScore extends AppCompatActivity {
    private DBHelper DB;
    private String username;
    private ListView highscoreListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myscore);

        DB = new DBHelper(this);
        username = LoginData.getLoggedInUsername();

        TextView usernametext = findViewById(R.id.usernametext);
        usernametext.setText("Benutzer: " + username);

        highscoreListView = findViewById(R.id.highscoreListView);

        displayHighscores();

        Button backToMenuButton = findViewById(R.id.backToMenuButton);
        Button gohighscoreButton = findViewById(R.id.gohighscoreButton);

        gohighscoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gohighscore();

            }
        });
        backToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });
    }

    private void displayHighscores() {
        String loggedInUsername = LoginData.getLoggedInUsername();

        List<ScoreData> highscoreList = DB.getTopScoresForUser(loggedInUsername);

        List<String> formattedHighscores = new ArrayList<>();
        for (int i = 0; i < highscoreList.size(); i++) {
            ScoreData entry = highscoreList.get(i);
            formattedHighscores.add(i+1 +": "+entry.getUsername() + ": " + entry.getScore());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, formattedHighscores);
        highscoreListView.setAdapter(adapter);
    }
    private void backToMenu() {
        Intent intent = new Intent(MyScore.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void gohighscore() {
        Intent intent = new Intent(MyScore.this, Highscore.class);
        startActivity(intent);
        finish();
    }

}
