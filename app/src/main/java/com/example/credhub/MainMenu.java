package com.example.credhub;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                Database.DatabaseEntry.COLUMN_NAME_1,
                Database.DatabaseEntry.COLUMN_NAME_2,
                Database.DatabaseEntry.COLUMN_NAME_3
        };

        String sortOrder = Database.DatabaseEntry.COLUMN_NAME_1 + " DESC";

        Cursor cursor = db.query(
                Database.DatabaseEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        final ListView credListView = findViewById(R.id.lista_credenciales);
        ArrayList<String> credList = new ArrayList<>();
        credList.add("Gmail");
        credList.add("Windows");
        credList.add("GitHub");
        credList.add("Example");

        while (cursor.moveToNext()) {
            String serviceName = cursor.getString(
                    cursor.getColumnIndexOrThrow(Database.DatabaseEntry.COLUMN_NAME_1));
            credList.add(serviceName);
        }
        cursor.close();

        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, credList);
        credListView.setAdapter(arrayAdapter);

        credListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = (String) credListView.getItemAtPosition(position);
                Toast.makeText(MainMenu.this, clickedItem, Toast.LENGTH_LONG).show();
                Intent showPasswordExportRegistry = new Intent(MainMenu.this, ShowPasswordExportRegistry.class);
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
            }
        });

        import_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent importRegister = new Intent(MainMenu.this, ImportRegistry.class);
                startActivity(importRegister);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
