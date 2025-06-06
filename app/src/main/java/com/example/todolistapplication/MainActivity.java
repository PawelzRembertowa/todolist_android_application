package com.example.todolistapplication;

import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapplication.todoitem.ToDoItem;
import com.example.todolistapplication.todoitemlist.ToDoAdapter;
import com.example.todolistapplication.todoitemlist.ToDoListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ToDoListener {

  private List<ToDoItem> itemList = new ArrayList<>();
  private ToDoAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    RecyclerView recyclerView = findViewById(R.id.todoRecyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    loadTasks();
    updateEmptyView();
    adapter = new ToDoAdapter(itemList, this);
    recyclerView.setAdapter(adapter);

    FloatingActionButton fab = findViewById(R.id.addToDoItem);
    fab.setOnClickListener(v -> showAddMainTaskDialog());
  }

  @Override
  public void onSubtaskStatusChanged(ToDoItem parentItem) {
    int index = itemList.indexOf(parentItem);
    if (index != -1) {
      adapter.notifyItemChanged(index);
    }
  }

  @Override
  public void onEdit(ToDoItem item, int position) {
    final EditText input = new EditText(this);
    input.setText(item.getText());
    new AlertDialog.Builder(this)
            .setTitle("Edytuj zadanie")
            .setView(input)
            .setPositiveButton("Zapisz", (dialog, which) -> {
              item.setText(input.getText().toString());
              adapter.notifyItemChanged(position);
              saveTasks();
            })
            .setNegativeButton("Anuluj", null)
            .show();
  }

  @Override
  public void onDelete(ToDoItem item, int position) {
    position = itemList.indexOf(item);
    if (position != -1) {
      itemList.remove(position);
      adapter.notifyDataSetChanged();
    }
    saveTasks();
    updateEmptyView();
  }

  @Override
  public void onAddSubtask(ToDoItem parentItem, int parentPosition) {
    final EditText input = new EditText(this);
    input.setHint("Wpisz subtask");

    new AlertDialog.Builder(this)
            .setTitle("Dodaj subtask")
            .setView(input)
            .setPositiveButton("Dodaj", (dialog, which) -> {
              String subText = input.getText().toString().trim();
              if (!subText.isEmpty()) {
                ToDoItem subtask = new ToDoItem(subText);
                subtask.setSubtask(true);
                parentItem.addSubItem(subtask);
                int newIndex = itemList.indexOf(parentItem);
                if (newIndex != -1) {
                  adapter.notifyItemChanged(newIndex);
                }
                saveTasks();
                updateEmptyView();
                parentItem.setExpanded(true);
                adapter.notifyItemChanged(parentPosition);
              }
            })
            .setNegativeButton("Anuluj", null)
            .show();
  }

  @Override
  public void onSubtaskDeleted(ToDoItem parentItem, ToDoItem subtask) {
    parentItem.getSubItems().remove(subtask);
    saveTasks();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.toolbar_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.action_show_dialog) {
      showMyDialog();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onSaveTasks() {
    saveTasks();
  }

  private void showAddMainTaskDialog() {
    final EditText input = new EditText(this);
    new AlertDialog.Builder(this)
            .setTitle("Dodaj zadanie")
            .setView(input)
            .setPositiveButton("Dodaj", (dialog, which) -> {
              String text = input.getText().toString().trim();
              if (!text.isEmpty()) {
                ToDoItem item = new ToDoItem(text);
                itemList.add(item);
                adapter.notifyItemInserted(itemList.size() - 1);
                saveTasks();
                updateEmptyView();
              }
            })
            .setNegativeButton("Anuluj", null)
            .show();
  }

  private void showMyDialog() {
    new AlertDialog.Builder(this)
            .setTitle("Usuń wszystkie zadania")
            .setPositiveButton("OK", (dialog, which) -> {
              itemList.clear();
              saveTasks();
              adapter.notifyDataSetChanged();
              updateEmptyView();
            })
            .setNegativeButton("Anuluj", null)
            .show();
  }

  private void saveTasks() {
    SharedPreferences prefs = getSharedPreferences("todo_prefs", MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();
    Gson gson = new Gson();
    String json = gson.toJson(itemList); // items to Twoja główna lista zadań
    editor.putString("todo_list", json);
    editor.apply();
  }

  private void loadTasks() {
    SharedPreferences prefs = getSharedPreferences("todo_prefs", MODE_PRIVATE);
    String json = prefs.getString("todo_list", null);
    if (json != null) {
      Gson gson = new Gson();
      Type type = new TypeToken<List<ToDoItem>>(){}.getType();
      itemList = gson.fromJson(json, type);
    } else {
      itemList = new ArrayList<>();
    }
  }

  private void updateEmptyView() {
    View emptyView = findViewById(R.id.emptyView);
    RecyclerView recyclerView = findViewById(R.id.todoRecyclerView);

    if (itemList.isEmpty()) {
      emptyView.setVisibility(View.VISIBLE);
      recyclerView.setVisibility(View.GONE);
    } else {
      emptyView.setVisibility(View.GONE);
      recyclerView.setVisibility(View.VISIBLE);
    }
  }

}
