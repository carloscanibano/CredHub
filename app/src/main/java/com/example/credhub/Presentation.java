package com.example.credhub;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Presentation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Because we need to wait 3 seconds to show the image, we can take advantage
        if (GlobalClass.dbHelper == null) {
            GlobalClass.dbHelper = new DatabaseHelper(getApplicationContext());
            GlobalClass.db = GlobalClass.dbHelper.getWritableDatabase();
        }

        // Waiting 3 seconds to show the main photo and then moving to the main menu
        Thread reloj = new Thread(){
            public void run() {
                try{
                    sleep(3000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    Intent menuPrincipal = new Intent(Presentation.this, MainMenu.class);
                    startActivity(menuPrincipal);
                    finish();
                }
            }
        };
        reloj.start();
    }
}
