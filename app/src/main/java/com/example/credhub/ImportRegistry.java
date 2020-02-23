package com.example.credhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ImportRegistry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_register);
        setTitle("Importar Registro");

        // First, we need to know which values are available remotely
        String[] strings = new String[1];
        strings[0] = "retrieve_list";
        new RepositoryTask().execute(strings);

        // Retrieving the list view in order to show the items
        final ListView repository_list_view = findViewById(R.id.lista_repositorio);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, GlobalClass.repositoryItems);
        repository_list_view.setAdapter(arrayAdapter);

        // When user selects an option, we perform the import operation
        repository_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GlobalClass.selectedItem = (String) repository_list_view.getItemAtPosition(position);
                //Toast.makeText(NewRegister.this,clickedItem,Toast.LENGTH_LONG).show();
                String[] strings = new String[1];
                strings[0] = "import";
                new RepositoryTask().execute(strings);
                Intent intent = new Intent(ImportRegistry.this, MainMenu.class);
                startActivity(intent);
                finish();
            }
        });

        GlobalClass.repositoryItems.clear();
    }

    @Override
    public void onBackPressed() {
        // Used to make sure that database retrieved data is updated
        Intent intent = new Intent(ImportRegistry.this, MainMenu.class);
        startActivity(intent);
    }
}
