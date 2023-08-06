package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity { //Seite für den Login/ Signup des Users

    EditText username, password; // Dekleration der Eingabefelder
    Button loginButton, signupButton;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // Mit activity_login.xml

        username = findViewById(R.id.usernameEditText); //Initialisierung der Eingaben, Buttons und DBHelper.
        password = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupButton = (Button) findViewById(R.id.signupButton);

        DB = new DBHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() { //Funktion des Login-Button
            @Override
            public void onClick(View v) {
                String enteredUsername = username.getText().toString(); // In Textfelder Eingegebener Text
                String enteredPassword = password.getText().toString();

                if (enteredUsername.equals("") || enteredPassword.equals("")) { //Falls eines der Eingabefelder leer
                    Toast.makeText(Login.this, "Fülle beide Felder aus!", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkUserPass = DB.checkusernamepassword(enteredUsername, enteredPassword); //checkusernamepassword() vergleicht Eingaben mit DB
                    if (checkUserPass) {
                        Toast.makeText(Login.this, "Login erfolgreich", Toast.LENGTH_SHORT).show();
                        LoginData.setLoggedInUsername(enteredUsername);  //setLoggedInUsername() speichern des Usernames in LoginData-Klasse
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent); //
                    } else {
                        Toast.makeText(Login.this, "Benutzername oder Passwort falsch", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() { //Funktion des Signup-Button
            @Override
            public void onClick(View v) {
                String enteredUsername = username.getText().toString();
                String enteredPassword = password.getText().toString();

                if (enteredUsername.equals("") || enteredPassword.equals("")) {
                    Toast.makeText(Login.this, "Fülle beide Felder aus!", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkUser = DB.checkusername(enteredUsername); // checkusername() untersucht ob username schon vergeben ist
                    if (!checkUser) {
                        Boolean insert = DB.insertData(enteredUsername, enteredPassword); //insertData() Einfügen des Username und Password in DB
                        if (insert) {
                            Toast.makeText(Login.this, "Sign up erfolgreich", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Login.this, "Sign up fehlgeschlagen", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Login.this, "Benutzername schon vergeben", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        /*
         // Quiz Fragen in Datenbank hinzufügen. Format:quizDataList.add(new String[]{ "FRAGE", "ANTWORT", "OPTION1", "OPTION2", "OPTION3"});
        List<String[]> quizDataList = new ArrayList<>();

        quizDataList.add(new String[]{"Wie viele Planeten hat unser Sonnensystem?", "8", "6", "10", "12"});
        quizDataList.add(new String[]{"Was ist die Hauptstadt von Frankreich?", "Paris", "Rom", "Madrid", "London"});
        quizDataList.add(new String[]{"Welches Jahr wurde die Unabhängigkeit der Vereinigten Staaten erklärt?", "1776", "1789", "1812", "1848"});
        quizDataList.add(new String[]{"Wer schrieb das Buch 'Pride and Prejudice'?", "Jane Austen", "Emily Brontë", "Charlotte Brontë", "Virginia Woolf"});
        quizDataList.add(new String[]{"Was ist die chemische Formel für Wasser?", "H2O", "CO2", "NaCl", "O2"});
        quizDataList.add(new String[]{"Welcher berühmte Künstler malte die Mona Lisa?", "Leonardo da Vinci", "Michelangelo", "Vincent van Gogh", "Pablo Picasso"});
        quizDataList.add(new String[]{"Welche Sprache wird in Brasilien hauptsächlich gesprochen?", "Portugiesisch", "Spanisch", "Englisch", "Französisch"});
        quizDataList.add(new String[]{"Wer war der erste Mensch, der den Mount Everest bestiegen hat?", "Sir Edmund Hillary", "Reinhold Messner", "Tenzing Norgay", "George Mallory"});
        quizDataList.add(new String[]{"Welches Tier ist das größte auf der Erde?", "Blauwal", "Elefant", "Giraffe", "Grizzlybär"});
        quizDataList.add(new String[]{"Welches Element hat das chemische Symbol 'Fe'?", "Eisen", "Silber", "Fluor", "Helium"});
        quizDataList.add(new String[]{"Wer schrieb das Buch '1984'?", "George Orwell", "Aldous Huxley", "Ray Bradbury", "J.R.R. Tolkien"});
        quizDataList.add(new String[]{"In welchem Jahr fand die Französische Revolution statt?", "1789", "1804", "1832", "1871"});
        quizDataList.add(new String[]{"Welcher Planet ist der nächste zur Sonne?", "Merkur", "Venus", "Mars", "Jupiter"});
        quizDataList.add(new String[]{"Wer hat die Relativitätstheorie entwickelt?", "Albert Einstein", "Isaac Newton", "Galileo Galilei", "Nikola Tesla"});
        quizDataList.add(new String[]{"Welche Jahreszeit folgt auf den Herbst?", "Winter", "Frühling", "Sommer", "Oktober"});
        quizDataList.add(new String[]{"Welches Land ist der größte Produzent von Kaffee?", "Brasilien", "Kolumbien", "Vietnam", "Äthiopien"});
        quizDataList.add(new String[]{"Welcher Ozean liegt zwischen den Kontinenten Afrika und Australien?", "Indischer Ozean", "Atlantischer Ozean", "Pazifischer Ozean", "Südlicher Ozean"});
        quizDataList.add(new String[]{"Welcher Planet ist als 'roter Planet' bekannt?", "Mars", "Jupiter", "Saturn", "Uranus"});
        quizDataList.add(new String[]{"Wer schrieb das Buch 'To Kill a Mockingbird'?", "Harper Lee", "J.D. Salinger", "Mark Twain", "Ernest Hemingway"});
        quizDataList.add(new String[]{"Wie viele Spieler sind in einer Fußballmannschaft auf dem Feld?", "11", "9", "10", "12"});
        quizDataList.add(new String[]{"Welcher berühmte Physiker formulierte die Gesetze der Bewegung?", "Isaac Newton", "Albert Einstein", "Niels Bohr", "Max Planck"});
        quizDataList.add(new String[]{"Welche Farbe hat eine reife Banane?", "Gelb", "Grün", "Rot", "Orange"});
        quizDataList.add(new String[]{"Welche zwei Elemente ergeben Wasser?", "Wasserstoff und Sauerstoff", "Kohlenstoff und Sauerstoff", "Stickstoff und Sauerstoff", "Wasserstoff und Stickstoff"});
        quizDataList.add(new String[]{"Wer malte das berühmte Bild 'Die Sternennacht'?", "Vincent van Gogh", "Pablo Picasso", "Claude Monet", "Salvador Dalí"});
        quizDataList.add(new String[]{"Wie viele Kontinente gibt es auf der Erde?", "7", "5", "6", "8"});
        quizDataList.add(new String[]{"Welches Land hat die längste Küstenlinie der Welt?", "Kanada", "Russland", "Australien", "Brasilien"});
        quizDataList.add(new String[]{"Welche Farbe ergibt sich, wenn man Rot und Blau mischt?", "Violett", "Grün", "Orange", "Braun"});
        quizDataList.add(new String[]{"Wer schrieb das Buch '1984'?", "George Orwell", "Aldous Huxley", "Ray Bradbury", "J.R.R. Tolkien"});
        quizDataList.add(new String[]{"Wie viele Beine hat eine Spinne?", "8", "6", "10", "12"});
        quizDataList.add(new String[]{"Welches Element hat das chemische Symbol 'Na'?", "Natrium", "Nickel", "Neon", "Sauerstoff"});
        quizDataList.add(new String[]{"Welches Land hat die meisten Einwohner?", "China", "Indien", "Vereinigte Staaten", "Russland"});
        quizDataList.add(new String[]{"Wer war der erste Mensch im Weltall?", "Juri Gagarin", "Neil Armstrong", "Buzz Aldrin", "Alan Shepard"});
        quizDataList.add(new String[]{"Welcher Planet ist als 'der Ringplanet' bekannt?", "Saturn", "Jupiter", "Uranus", "Neptun"});
        quizDataList.add(new String[]{"Wer schrieb das Buch 'Die Odyssee'?", "Homer", "Sophokles", "Plato", "Aristoteles"});
        quizDataList.add(new String[]{"Welcher Wüstenplanet ist bekannt für seine riesigen Sanddünen?", "Sahara", "Gobi", "Atacama", "Kalahari"});
        quizDataList.add(new String[]{"Wie viele Zähne hat ein erwachsener Mensch normalerweise?", "32", "28", "36", "30"});
        quizDataList.add(new String[]{"Welche Farbe haben die Blutkörperchen?", "Rot", "Blau", "Grün", "Weiß"});
        quizDataList.add(new String[]{"Welches Land hat die meisten Olympischen Spiele ausgerichtet?", "Vereinigte Staaten", "Frankreich", "Deutschland", "Japan"});
        quizDataList.add(new String[]{"Wer schrieb das Buch 'Der alte Mann und das Meer'?", "Ernest Hemingway", "F. Scott Fitzgerald", "William Faulkner", "John Steinbeck"});
        quizDataList.add(new String[]{"Wie viele Bundesstaaten hat die USA?", "50", "48", "52", "54"});
        quizDataList.add(new String[]{"Welches Element hat das chemische Symbol 'Ag'?", "Silber", "Gold", "Aluminium", "Kupfer"});
        quizDataList.add(new String[]{"Wer war der erste Präsident der Vereinigten Staaten?", "George Washington", "Abraham Lincoln", "Thomas Jefferson", "John Adams"});
        quizDataList.add(new String[]{"Welche Farbe ergibt sich, wenn man Gelb und Rot mischt?", "Orange", "Grün", "Violett", "Braun"});
        quizDataList.add(new String[]{"Wer schrieb das Buch 'Moby-Dick'?", "Herman Melville", "Mark Twain", "Jules Verne", "Charles Dickens"});
        quizDataList.add(new String[]{"Welches Land ist für seine Pyramiden von Gizeh bekannt?", "Ägypten", "Mexiko", "Peru", "Sudan"});
        quizDataList.add(new String[]{"Wie viele Milliliter sind in einem Liter?", "1000", "500", "750", "1500"});
        quizDataList.add(new String[]{"Welcher Kontinent beherbergt den Amazonas-Regenwald?", "Südamerika", "Afrika", "Asien", "Australien"});
        quizDataList.add(new String[]{"Wer malte die berühmte Deckenfreske in der Sixtinischen Kapelle?", "Michelangelo", "Leonardo da Vinci", "Raphael", "Donatello"});
        quizDataList.add(new String[]{"Wie viele Spieler sind in einem Baseball-Team auf dem Feld?", "9", "7", "10", "11"});
        quizDataList.add(new String[]{"Welche Farbe hat der Stern auf der chinesischen Flagge?", "Gelb", "Rot", "Blau", "Weiß"});
        quizDataList.add(new String[]{"Welches Land ist für seine Tempelruinen von Angkor Wat bekannt?", "Kambodscha", "Thailand", "Vietnam", "Myanmar"});
        quizDataList.add(new String[]{"Welcher Musiker gilt als 'King of Pop'?", "Michael Jackson", "Elvis Presley", "Prince", "Madonna"});
        quizDataList.add(new String[]{"Welches Jahr wurde die Berliner Mauer errichtet?", "1961", "1955", "1972", "1980"});
        quizDataList.add(new String[]{"Welcher Planet ist als 'der Eisplanet' bekannt?", "Uranus", "Jupiter", "Saturn", "Neptun"});
        quizDataList.add(new String[]{"Wer schrieb das Buch 'Die Verwandlung'?", "Franz Kafka", "Friedrich Nietzsche", "Thomas Mann", "Hermann Hesse"});
        quizDataList.add(new String[]{"Welches Land hat die meisten Nobelpreisträger?", "Vereinigte Staaten", "Deutschland", "Großbritannien", "Frankreich"});
        quizDataList.add(new String[]{"Welcher Fluss fließt durch Paris?", "Seine", "Rhein", "Donau", "Themse"});
        quizDataList.add(new String[]{"Welche Farbe ergibt sich, wenn man Gelb und Blau mischt?", "Grün", "Violett", "Orange", "Braun"});
        quizDataList.add(new String[]{"Wer schrieb das Buch 'Der Herr der Ringe'?", "J.R.R. Tolkien", "George R.R. Martin", "C.S. Lewis", "J.K. Rowling"});
        quizDataList.add(new String[]{"Wie viele Bundesländer hat Deutschland?", "16", "10", "12", "20"});
        quizDataList.add(new String[]{"Welches Element hat das chemische Symbol 'Ca'?", "Calcium", "Kohlenstoff", "Kalium", "Kalzium"});
        quizDataList.add(new String[]{"Wer war der erste Mensch auf dem Mond?", "Neil Armstrong", "Buzz Aldrin", "Yuri Gagarin", "Alan Shepard"});
        quizDataList.add(new String[]{"Welcher Planet ist als 'der Riesenplanet' bekannt?", "Jupiter", "Saturn", "Uranus", "Neptun"});
        quizDataList.add(new String[]{"Wer schrieb das Buch 'Der kleine Prinz'?", "Antoine de Saint-Exupéry", "Jules Verne", "Victor Hugo", "Voltaire"});
        quizDataList.add(new String[]{"Welche Wüste erstreckt sich über Nordafrika?", "Sahara", "Gobi", "Atacama", "Kalahari"});


        // Loopt durch die erstelle Liste fügt jede in DB ein
        boolean allInserted = true;
        for (String[] quiz : quizDataList) {
            String question = quiz[0];
            String correctAnswer = quiz[1];
            String option1 = quiz[2];
            String option2 = quiz[3];
            String option3 = quiz[4];
            boolean inserted = DB.insertQuizContent(question, correctAnswer, option1, option2, option3); //insertQuizContent()
            if (!inserted) {
                allInserted = false;
                break;
            }
        }

// Check ob alle Fragen hinzugefügt wurden
        if (allInserted) {
            Toast.makeText(Login.this, "Quizinhalt hinzugefügt", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Login.this, "Fehler beim Hinzufügen des Quizinhalts", Toast.LENGTH_SHORT).show();
        }
        */
    }
    }

