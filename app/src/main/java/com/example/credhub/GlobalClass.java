package com.example.credhub;

import android.app.Application;
import android.widget.ArrayAdapter;

public abstract class GlobalClass extends Application {
    public static String selectedItem = "";
    public static ArrayAdapter mainMenuAdapter;
}
