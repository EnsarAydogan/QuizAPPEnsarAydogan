package com.example.quiz;

public class LoginData {
    private static String loggedInUsername;

    public static void setLoggedInUsername(String username) { //speichert username
        loggedInUsername = username;
    }

    public static String getLoggedInUsername() { //gibt username wieder
        return loggedInUsername;
    }

    public static void clearLoggedInUsername() { //nulled username
        loggedInUsername = null;
    }

}