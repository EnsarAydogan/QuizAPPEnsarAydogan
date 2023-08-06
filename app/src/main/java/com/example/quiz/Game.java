package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections; //Listen und Buttons shuffeln
import java.util.List;

public class Game extends AppCompatActivity implements View.OnClickListener {
    private DBHelper DB;
    private TextView questionTextView;
    private Button[] answerButtons; //Button-Array für die Antwort-Schaltflächen
    private Button nextQuestionButton, backToMenuButton;
    private String username;
    private int currentQuestionIndex;
    private List<QuizQuestion> quizQuestions;
    private boolean isAnswered; //wurde eine der Antwortoptionen gedrückt -- TRUE/FALSE
    private int score;

    private TextView scoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        DB = new DBHelper(this);
        username = LoginData.getLoggedInUsername();
        TextView usernametext = findViewById(R.id.usernametext);
        usernametext.setText("Benutzer: " + username);

        scoreTextView = findViewById(R.id.scoreTextView); //Anzeige "SCORE:" score


        questionTextView = findViewById(R.id.questionTextView);
        answerButtons = new Button[4];
        answerButtons[0] = findViewById(R.id.answerButton1);
        answerButtons[1] = findViewById(R.id.answerButton2);
        answerButtons[2] = findViewById(R.id.answerButton3);
        answerButtons[3] = findViewById(R.id.answerButton4);

        for (Button button : answerButtons) { //Listerner für jeden Antwort-Button
            button.setOnClickListener(this);
        }

        quizQuestions = new ArrayList<>();
        currentQuestionIndex = 0;
        isAnswered = false;
        score = 0;

        loadQuizQuestions(); //nutzt getAllQuizQuestions() und mischt in quizQuestion Liste
        displayQuestion(); // zeigt aktuelle Frage und mischt die Antwortmöglichkeit


        nextQuestionButton = findViewById(R.id.nextQuestionButton);
        backToMenuButton = findViewById(R.id.backToMenuButton);

        //"Nächste Frage" und "Zurück zum Hauptmenu" Buttons unsichtbar
        nextQuestionButton.setVisibility(View.GONE);
        backToMenuButton.setVisibility(View.GONE);

        nextQuestionButton.setOnClickListener(this);
        backToMenuButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) { //Klickaktionen für die Buttons
        if (v == backToMenuButton) {
            backToMenu();
        } else if (v == nextQuestionButton) {
            nextQuestion();
        } else {
            if (!isAnswered) {
                Button clickedButton = (Button) v;
                checkAnswer(clickedButton);
            }
        }
    }

    private void nextQuestion() {
        if (currentQuestionIndex < quizQuestions.size() - 1) { // IF noch Fragen in quizQuestion übrig
            currentQuestionIndex++;
            displayQuestion(); //Zurücksetzen der Fragen, Antworten, isAnswered und der nextQuestion/backtoMenu
            resetButtons();
            isAnswered = false;
            nextQuestionButton.setVisibility(View.GONE);
            backToMenuButton.setVisibility(View.GONE);
        } else {
            // Keine Fragen übrig --> Quiz vorbei
            nextQuestionButton.setVisibility(View.GONE);
            backToMenuButton.setVisibility(View.VISIBLE);
        }
    }


    private void checkAnswer(Button clickedButton) {
        QuizQuestion currentQuestion = quizQuestions.get(currentQuestionIndex);
        String selectedAnswer = clickedButton.getText().toString();

        for (Button button : answerButtons) {
            if (button.getText().toString().equals(currentQuestion.getCorrectAnswer())) {
                // Korrekte Antwort grün einfärben
                button.setBackgroundResource(R.drawable.button_background_green);
            } else {
                // Falsche Antwort rot einfärben
                button.setBackgroundResource(R.drawable.button_background_red);
            }
        }

        if (selectedAnswer.equals(currentQuestion.getCorrectAnswer())) { //IF ausgewählte Antwort = korrekte Antwort
            Toast.makeText(this, "Richtig!", Toast.LENGTH_SHORT).show();
            score++;
            nextQuestionButton.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "Falsch!", Toast.LENGTH_SHORT).show();

            backToMenuButton.setVisibility(View.VISIBLE);

        }

        isAnswered = true;
        scoreTextView.setText("SCORE: " + score);

        if (currentQuestionIndex >= quizQuestions.size() - 1) { //Saved wenn alle Frage beantwortet
            saveScore();
        }
    }
    private void backToMenu() {
        saveScore();
        Intent intent = new Intent(Game.this, MainActivity.class);
        startActivity(intent);
        finish(); //Beenden von Game-Activity
    }

    private void resetButtons() {
        for (Button button : answerButtons) {
            button.setBackgroundResource(R.drawable.button_background);
        }
    }

    private void loadQuizQuestions() {
        quizQuestions = DB.getAllQuizQuestions();
        Collections.shuffle(quizQuestions);
    }

    private void displayQuestion() {
        if (currentQuestionIndex < quizQuestions.size()) {
            QuizQuestion currentQuestion = quizQuestions.get(currentQuestionIndex);
            questionTextView.setText(currentQuestion.getQuestion());
            List<String> answers = new ArrayList<>();
            answers.add(currentQuestion.getCorrectAnswer());
            answers.add(currentQuestion.getWrongAnswer1());
            answers.add(currentQuestion.getWrongAnswer2());
            answers.add(currentQuestion.getWrongAnswer3());
            Collections.shuffle(answers);
            for (int i = 0; i < answerButtons.length; i++) { //Anzeigen der Antwortmöglichkeiten auf den Buttons
                answerButtons[i].setText(answers.get(i));
            }
        }
    }

    private void saveScore() {
        DB.insertScore(username, score);
    }
}
