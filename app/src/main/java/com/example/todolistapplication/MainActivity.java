package com.example.todolistapplication;

import android.os.Bundle;

import com.example.todolistapplication.todoitem.ToDoItem;
import com.example.todolistapplication.todoitemlist.ToDoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapplication.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    private AppBarConfiguration appBarConfiguration;
//    private ActivityMainBinding binding;

    private List<ToDoItem> itemList = new ArrayList<>();
    private ToDoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.todoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ToDoAdapter(itemList, new ToDoAdapter.OnItemActionListener() {
            @Override
            public void onEdit(ToDoItem item, int position) {
                // Tworzymy EditText do edytowania tekstu
                final EditText input = new EditText(MainActivity.this);
                input.setText(item.getText());  // Ustawiamy obecny tekst

                // Tworzymy dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Edytuj zadanie");
                builder.setView(input);  // Ustawiamy EditText w dialogu

                // Akcja po kliknięciu "OK"
                builder.setPositiveButton("OK", (dialog, which) -> {
                    // Zaktualizuj tekst zadania
                    String newText = input.getText().toString();
                    item.setText(newText);  // Zaktualizowany tekst
                    adapter.notifyItemChanged(position);  // Zaktualizuj widok
                });

                // Akcja po kliknięciu "Cancel"
                builder.setNegativeButton("Anuluj", (dialog, which) -> {
                    dialog.dismiss();  // Zamknij dialog, nic nie zmieniamy
                });

                // Opcja, aby dialog zamknął się po kliknięciu poza nim
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);  // Zamknie dialog po kliknięciu poza nim
                dialog.show();
            }

            @Override
            public void onDelete(ToDoItem item, int position) {
                itemList.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });

        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.addToDoItem);
        fab.setOnClickListener(v -> {
            // przygotowanie dodwania zadania w FAB
//            EditText input = new EditText(context);
//            input.setHint("Wprowadź zadanie");


//            ToDoItem newItem = new ToDoItem("Nowe zadanie");
//            itemList.add(newItem);
//            adapter.notifyItemInserted(itemList.size() - 1);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Dodaj zadanie");

            final EditText input = new EditText(MainActivity.this);
            input.setHint("Wprowadź zadanie");
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("Dodaj", (dialog, which) -> {
                String text = input.getText().toString().trim();
                if (!text.isEmpty()) {
                    ToDoItem newItem = new ToDoItem(text);
                    itemList.add(newItem);
                    adapter.notifyItemInserted(itemList.size() - 1);
                } else {
                    Toast.makeText(MainActivity.this, "Nie wpisano treści zadania", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Anuluj", (dialog, which) -> dialog.cancel());

            builder.show();
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}