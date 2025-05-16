package com.example.todolistapplication;

import android.os.Bundle;

import com.example.todolistapplication.todoitem.ToDoItem;
import com.example.todolistapplication.todoitemlist.ToDoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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

            @Override
            public void addSubtask(ToDoItem parentItem, ToDoItem subtask) {

            }
        });

        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.addToDoItem);
        fab.setOnClickListener(v -> {

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

    public void showAddSubtaskDialog(ToDoItem parentItem, int parentPosition) {
        final EditText input = new EditText(MainActivity.this);
        input.setHint("Wpisz subtask");

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Dodaj subtask");
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String subtaskText = input.getText().toString().trim();

            if (!subtaskText.isEmpty()) {
                ToDoItem subtask = new ToDoItem(subtaskText);
                subtask.setSubtask(true);

                // 1. Dodaj do listy subtasków rodzica
                parentItem.addSubItem(subtask);

                // 2. Dodaj subtask do listy wyświetlanych elementów (zaraz pod rodzicem)
                int insertPosition = parentPosition + 1;
                itemList.add(insertPosition, subtask);

                // 3. Powiadom adapter
                adapter.notifyItemInserted(insertPosition);
            } else {
                Toast.makeText(MainActivity.this, "Subtask nie może być pusty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Anuluj", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

}