package com.example.todolistapplication;

import android.os.Bundle;

import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    RecyclerView recyclerView = findViewById(R.id.todoRecyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    adapter = new ToDoAdapter(itemList, this);
    recyclerView.setAdapter(adapter);

    FloatingActionButton fab = findViewById(R.id.addToDoItem);
    fab.setOnClickListener(v -> showAddMainTaskDialog());
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
              }
            })
            .setNegativeButton("Anuluj", null)
            .show();
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
              }
            })
            .setNegativeButton("Anuluj", null)
            .show();
  }

  @Override
  public void onSubtaskDeleted(ToDoItem parentItem, ToDoItem subtask) {
    parentItem.getSubItems().remove(subtask);
  }
}
