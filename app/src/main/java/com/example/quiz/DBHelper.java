package com.example.quiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Login.db";

    public DBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }


    public void onCreate(SQLiteDatabase MyDB) {
        Log.d("DBHelper", "onCreate method called");
        MyDB.execSQL("create Table users(username TEXT primary key, password TEXT)");
        MyDB.execSQL("create Table scores(username TEXT, score INTEGER)");
        MyDB.execSQL("create Table quiz(quiz_id INTEGER primary key autoincrement, question TEXT, correct_answer TEXT, wrong_answer1 TEXT, wrong_answer2 TEXT, wrong_answer3 TEXT)");
    }

    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
    }

    public Boolean insertData(String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = MyDB.insert("users", null, contentValues);
        return result != -1;
    }

    public Boolean checkusername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[]{username});
        return cursor.getCount() > 0;
    }

    public Boolean checkusernamepassword(String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[]{username, password});
        return cursor.getCount() > 0;
    }

    public Boolean insertScore(String username, int score) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("score", score);
        long result = MyDB.insert("scores", null, contentValues);
        return result != -1;
    }
    public int getUsersCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users", null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public int getScoresCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM scores", null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public int getQuizCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM quiz", null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public Boolean insertQuizContent(String question, String correctAnswer, String wrongAnswer1, String wrongAnswer2, String wrongAnswer3) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("question", question);
        contentValues.put("correct_answer", correctAnswer);
        contentValues.put("wrong_answer1", wrongAnswer1);
        contentValues.put("wrong_answer2", wrongAnswer2);
        contentValues.put("wrong_answer3", wrongAnswer3);
        long result = MyDB.insert("quiz", null, contentValues);
        return result != -1; //result ungleich -1 bedeuted erfolgreiche Einfügung
    }

    public void deleteQuizContent() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        MyDB.delete("quiz", null, null);
        MyDB.close();
    }
    public List<ScoreData> getTopScoresWithUsernames() {
        List<ScoreData> highscores = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT username, score FROM scores ORDER BY score DESC LIMIT 10";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) { //checkt ob daten vorhanden sind
            do { //wenn ja fügt es username und passwort in highscore hinzu
                String username = cursor.getString(cursor.getColumnIndex("username"));
                int score = cursor.getInt(cursor.getColumnIndex("score"));
                ScoreData entry = new ScoreData(username, score);
                highscores.add(entry);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return highscores;
    }
    public List<ScoreData> getTopScoresForUser(String username) {
        List<ScoreData> highscores = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT username, score FROM scores WHERE username = ? ORDER BY score DESC LIMIT 10";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor.moveToFirst()) {
            do {
                String user = cursor.getString(cursor.getColumnIndex("username"));
                int score = cursor.getInt(cursor.getColumnIndex("score"));
                ScoreData entry = new ScoreData(user, score);
                highscores.add(entry);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return highscores;
    }
    public List<QuizQuestion> getAllQuizQuestions() {
        List<QuizQuestion> quizQuestionsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM quiz";

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(selectQuery, null);

            // Loop through the cursor and create QuizQuestion objects
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int quizId = cursor.getInt(cursor.getColumnIndex("quiz_id"));
                    String question = cursor.getString(cursor.getColumnIndex("question"));
                    String correctAnswer = cursor.getString(cursor.getColumnIndex("correct_answer"));
                    String wrongAnswer1 = cursor.getString(cursor.getColumnIndex("wrong_answer1"));
                    String wrongAnswer2 = cursor.getString(cursor.getColumnIndex("wrong_answer2"));
                    String wrongAnswer3 = cursor.getString(cursor.getColumnIndex("wrong_answer3"));

                    // Create a QuizQuestion object and add it to the list
                    QuizQuestion quizQuestion = new QuizQuestion(quizId, question, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3);
                    quizQuestionsList.add(quizQuestion);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DBHelper", "Error while getting quiz questions: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        // Close the database connection
        db.close();

        return quizQuestionsList;


    }
}
