package com.example.credhub;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public abstract class GlobalClass extends Application {
    public static SQLiteDatabase db = null;
    public static DatabaseHelper dbHelper = null;
    public static String selectedItem = "";
    public static ArrayAdapter mainMenuAdapter;
    public static ArrayList<String> repositoryItems = new ArrayList<>();
}
