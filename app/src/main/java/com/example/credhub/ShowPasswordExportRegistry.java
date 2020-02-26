package com.example.credhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ShowPasswordExportRegistry extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_password_export_registry);
        setTitle("Mostrar Contraseña / Exportar Registro");

        // Retrieving data from the last activity
        Bundle bundle = getIntent().getExtras();

        // Preparing all the views and buttons we need to operate with
        TextView app_id = findViewById(R.id.identificador_app);
        TextView username = findViewById(R.id.nombre_usuario);
        TextView pass = findViewById(R.id.contraseña);
        Button show_password = findViewById(R.id.mostrar_contraseña);
        Button delete_registry = findViewById(R.id.borrar_registro);
        Button export_registry = findViewById(R.id.make_nuevo_registro);

        String clicked_item = null;
        if (bundle != null) {
            clicked_item = bundle.getString("clicked_item");
        }
        app_id.setText(clicked_item);

        // Database utils are managed from a global class
        if (GlobalClass.dbHelper == null) {
            GlobalClass.dbHelper = new DatabaseHelper(getApplicationContext());
            GlobalClass.db = GlobalClass.dbHelper.getReadableDatabase();
        }

        // Once we have the service name, we look for the stored user name and password values
        String[] projection = {
                BaseColumns._ID,
                Database.DatabaseEntry.COLUMN_NAME_2,
                Database.DatabaseEntry.COLUMN_NAME_3
        };

        String selection = Database.DatabaseEntry.COLUMN_NAME_1 + " = ?";
        String[] selectionArgs = { clicked_item };

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

        String user, pw;
        cursor.moveToNext();
        user = cursor.getString(cursor.getColumnIndexOrThrow(Database.DatabaseEntry.COLUMN_NAME_2));
        pw = cursor.getString(cursor.getColumnIndexOrThrow(Database.DatabaseEntry.COLUMN_NAME_3));
        cursor.close();

        username.setText(user);
        pass.setText(pw);
        pass.setVisibility(View.INVISIBLE);

        // Showing password for only 5 seconds when user clicks
        show_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                TextView password = findViewById(R.id.contraseña);
                password.setVisibility(View.VISIBLE);
                password.postDelayed(new Runnable() {
                    public void run() {
                        TextView password = findViewById(R.id.contraseña);
                        password.setVisibility(View.INVISIBLE);
                    }
                }, 5000);
            }
        });

        // Deleting the current registry from database
        delete_registry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                TextView srv = findViewById(R.id.identificador_app);
                String service = srv.getText().toString();
                String selection = Database.DatabaseEntry.COLUMN_NAME_1 + " LIKE ?";
                String[] selectionArgs = { service };
                int deletedRows = GlobalClass.db.delete(Database.DatabaseEntry.TABLE_NAME, selection, selectionArgs);
                if (deletedRows != 0) Toast.makeText(ShowPasswordExportRegistry.this,"Entrada eliminada del registro",Toast.LENGTH_LONG).show();
                Intent mainMenu = new Intent(ShowPasswordExportRegistry.this, MainMenu.class);
                startActivity(mainMenu);
                finish();
            }
        });

        // Exporting current registry to remote repository
        export_registry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                TextView service, user, password;
                service = findViewById(R.id.identificador_app);
                user = findViewById(R.id.nombre_usuario);
                password = findViewById(R.id.contraseña);
                String[] strings = new String[4];
                strings[0] = "export";
                strings[1] = service.getText().toString();
                strings[2] = user.getText().toString();
                strings[3] = password.getText().toString();
                new RepositoryTask().execute(strings);
                Intent intent = new Intent(ShowPasswordExportRegistry.this, MainMenu.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Making sure we show updated data in all views
        Intent intent = new Intent(ShowPasswordExportRegistry.this, MainMenu.class);
        startActivity(intent);
    }
}
