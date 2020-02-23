package com.example.credhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NewRegister extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_register);
        setTitle("Nuevo Registro");

        // Creating a list for Android installed applications and finding the proper view
        final ListView appListView = findViewById(R.id.lista_aplicaciones);
        ArrayList<String> appList = new ArrayList<>();

        //Get a list of installed apps
        final String TAG = ImportRegistry.class.getSimpleName();
        final PackageManager pm = getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            Log.d(TAG, "Installed package :" + packageInfo.packageName);
            Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));

            appList.add(getPackageManager().getApplicationLabel(packageInfo.applicationInfo).toString());
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, appList);
        appListView.setAdapter(arrayAdapter);

        // Selected item gets stored inside our global class to perform operations inside listeners
        appListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GlobalClass.selectedItem = (String) appListView.getItemAtPosition(position);
                //Toast.makeText(NewRegister.this,clickedItem,Toast.LENGTH_LONG).show();
            }
        });

        // When the action starts, first we check for a possible update, if not, an insert is done
        Button nuevo_registro_action = findViewById(R.id.nuevo_registro_action);
        nuevo_registro_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                EditText campo_usuario = findViewById(R.id.campo_usuario);
                EditText campo_password = findViewById(R.id.campo_password);

                // All fields must be selected in order to perform the operation
                if ((campo_usuario.getText().toString().trim().length() == 0) ||
                        (campo_password.getText().toString().trim().length() == 0) ||
                        (GlobalClass.selectedItem.trim().length() == 0)) {
                    Toast.makeText(NewRegister.this,"Introduzca usuario, contraseña y seleccione un servicio",Toast.LENGTH_LONG).show();
                } else {
                    if (GlobalClass.dbHelper == null) {
                        GlobalClass.dbHelper = new DatabaseHelper(getApplicationContext());
                        GlobalClass.db = GlobalClass.dbHelper.getWritableDatabase();
                    }

                    // Is the selected item inside our database?
                    String[] projection = {
                      Database.DatabaseEntry.COLUMN_NAME_1
                    };
                    String selection = Database.DatabaseEntry.COLUMN_NAME_1 + " = ?";
                    String[] selectionArgs = { GlobalClass.selectedItem };
                    String sortOrder = Database.DatabaseEntry.COLUMN_NAME_1 + " DESC";
                    Cursor cursor = GlobalClass.db.query(
                            Database.DatabaseEntry.TABLE_NAME,
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder
                    );

                    ContentValues values = new ContentValues();

                    // If cursor has values, it means we need to perform an update, else, an insert
                    if (cursor.getCount() > 0) {
                        values.put(Database.DatabaseEntry.COLUMN_NAME_1, GlobalClass.selectedItem);
                        values.put(Database.DatabaseEntry.COLUMN_NAME_2, campo_usuario.getText().toString());
                        values.put(Database.DatabaseEntry.COLUMN_NAME_3, campo_password.getText().toString());
                        String select = Database.DatabaseEntry.COLUMN_NAME_1 + " LIKE ?";
                        String[] selectArgs = { GlobalClass.selectedItem };
                        int count = GlobalClass.db.update(
                                Database.DatabaseEntry.TABLE_NAME,
                                values,
                                select,
                                selectArgs
                        );

                        if (count != 0) Toast.makeText(NewRegister.this,"El registro existe; se ha actualizado",Toast.LENGTH_LONG).show();
                    } else {
                        values.put(Database.DatabaseEntry.COLUMN_NAME_1, GlobalClass.selectedItem);
                        values.put(Database.DatabaseEntry.COLUMN_NAME_2, campo_usuario.getText().toString());
                        values.put(Database.DatabaseEntry.COLUMN_NAME_3, campo_password.getText().toString());
                        long newRowId = GlobalClass.db.insert(Database.DatabaseEntry.TABLE_NAME, null, values);
                        Toast.makeText(NewRegister.this,"Registro añadido con id: " + newRowId,Toast.LENGTH_LONG).show();
                    }

                    // Freeing resources and going back to the main menu
                    cursor.close();
                    Intent intent = new Intent(NewRegister.this, MainMenu.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        // Making sure we show views with updated database info
        Intent intent = new Intent(NewRegister.this, MainMenu.class);
        startActivity(intent);
    }
}
