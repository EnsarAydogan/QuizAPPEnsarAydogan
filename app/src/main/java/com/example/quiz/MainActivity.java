package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.quiz.R;

public class MainActivity extends AppCompatActivity { //Hauptmenu zur Navigation
    private Button playButton, highscoreButton, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String username = LoginData.getLoggedInUsername(); //übernehmen des usernames aus LoginData

        playButton = findViewById(R.id.playButton);
        highscoreButton = findViewById(R.id.highscoreButton);
        logoutButton = findViewById(R.id.logoutButton);
        TextView usernametext = findViewById(R.id.usernametext);
        usernametext.setText("Benutzer: " + username);

        playButton.setOnClickListener(new View.OnClickListener() { //Start des Quiz
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Game.class);
                startActivity(intent);
            }
        });
        highscoreButton.setOnClickListener(new View.OnClickListener() { //Anzeigen der Highscores
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Highscore.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() { // Ausloggen
            @Override
            public void onClick(View v) {
                // Zurück zur Login-Seite
                LoginData.clearLoggedInUsername(); //clearLoggedInUsername() leert Username
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        DBHelper db = new DBHelper(this);

        // Überprüfung der Anzahl der DBinhalte für Developer
        int usersCount = db.getUsersCount();
        int scoresCount = db.getScoresCount();
        int quizCount = db.getQuizCount();

        Log.d("MainActivity", "Users count: " + usersCount);
        Log.d("MainActivity", "Scores count: " + scoresCount);
        Log.d("MainActivity", "Quiz count: " + quizCount);

        db.close();
    }

}
