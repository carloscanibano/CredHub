package com.example.credhub;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.widget.TextView;

public class ShowPasswordExportRegistry extends AppCompatActivity {
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_password_export_registry);

        Bundle bundle = getIntent().getExtras();

        TextView app_id = findViewById(R.id.identificador_app);
        TextView username = findViewById(R.id.nombre_usuario);
        TextView password = findViewById(R.id.contraseña);

        String clicked_item = null;
        if (bundle != null) {
            clicked_item = bundle.getString("clicked_item");
        }
        app_id.setText(clicked_item);

        dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                Database.DatabaseEntry.COLUMN_NAME_2,
                Database.DatabaseEntry.COLUMN_NAME_3
        };

        String selection = Database.DatabaseEntry.COLUMN_NAME_1 + " = ?";
        String[] selectionArgs = { clicked_item };

        String sortOrder = Database.DatabaseEntry.COLUMN_NAME_1 + " DESC";

        Cursor cursor = db.query(
                Database.DatabaseEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        String name, pass;
        cursor.moveToNext();
        name = cursor.getString(cursor.getColumnIndexOrThrow(Database.DatabaseEntry.COLUMN_NAME_2));
        pass = cursor.getString(cursor.getColumnIndexOrThrow(Database.DatabaseEntry.COLUMN_NAME_3));
        cursor.close();

        username.setText(name);
        password.setText(pass);
    }

    public void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
