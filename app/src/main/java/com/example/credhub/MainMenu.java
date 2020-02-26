package com.example.credhub;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Database utils are managed in a global class
        if (GlobalClass.dbHelper == null) {
            GlobalClass.dbHelper = new DatabaseHelper(getApplicationContext());
            GlobalClass.db = GlobalClass.dbHelper.getWritableDatabase();
        }

        //In case you want to delete the local database, use this line
        //GlobalClass.db.delete(Database.DatabaseEntry.TABLE_NAME, null, null);

        // The following actions perform a select SQL statement
        String[] projection = {
                BaseColumns._ID,
                Database.DatabaseEntry.COLUMN_NAME_1,
                Database.DatabaseEntry.COLUMN_NAME_2,
                Database.DatabaseEntry.COLUMN_NAME_3
        };

        String sortOrder = Database.DatabaseEntry.COLUMN_NAME_1 + " DESC";

        Cursor cursor = GlobalClass.db.query(
                Database.DatabaseEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        // Filling our list with database retrieved data
        final ListView credListView = findViewById(R.id.lista_credenciales);
        ArrayList<String> credList = new ArrayList<>();

        while (cursor.moveToNext()) {
            String serviceName = cursor.getString(
                    cursor.getColumnIndexOrThrow(Database.DatabaseEntry.COLUMN_NAME_1));
            credList.add(serviceName);
        }
        cursor.close();

        // Filling our list view with items
        GlobalClass.mainMenuAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, credList);
        credListView.setAdapter(GlobalClass.mainMenuAdapter);
        credListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = (String) credListView.getItemAtPosition(position);
                Intent showPasswordExportRegistry = new Intent(MainMenu.this, ShowPasswordExportRegistry.class);
                // Making sure we know in the next activity which value the user selected
                showPasswordExportRegistry.putExtra("clicked_item", clickedItem);
                startActivity(showPasswordExportRegistry);
            }
        });

        Button new_register = findViewById(R.id.nuevo_registro);
        Button import_register = findViewById(R.id.importar_registro);

        new_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent newRegister = new Intent(MainMenu.this, NewRegister.class);
                startActivity(newRegister);
                finish();
            }
        });

        import_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent importRegister = new Intent(MainMenu.this, ImportRegistry.class);
                startActivity(importRegister);
                finish();
            }
        });
    }
}
