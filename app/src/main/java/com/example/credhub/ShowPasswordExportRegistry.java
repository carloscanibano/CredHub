package com.example.credhub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ShowPasswordExportRegistry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_password_export_registry);

        Bundle bundle = getIntent().getExtras();

        TextView app_id = findViewById(R.id.identificador_app);
        String clicked_item = null;
        if (bundle != null) {
            clicked_item = bundle.getString("clicked_item");
        }
        app_id.setText(clicked_item);
    }
}
