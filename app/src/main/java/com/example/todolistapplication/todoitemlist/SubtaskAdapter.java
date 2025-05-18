package com.example.todolistapplication.todoitemlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapplication.R;
import com.example.todolistapplication.todoitem.ToDoItem;
import com.example.todolistapplication.todoitemlist.ToDoListener;

import java.util.List;

public class SubtaskAdapter extends RecyclerView.Adapter<SubtaskAdapter.SubtaskViewHolder> {

  private final List<ToDoItem> subtasks;
  private final ToDoItem parentItem;
  private final ToDoListener listener;

  public SubtaskAdapter(List<ToDoItem> subtasks, ToDoItem parentItem, ToDoListener listener) {
    this.subtasks = subtasks;
    this.parentItem = parentItem;
    this.listener = listener;
  }

  @NonNull
  @Override
  public SubtaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_subtask_item, parent, false);
    return new SubtaskViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull SubtaskViewHolder holder, int position) {
    ToDoItem subtask = subtasks.get(position);
    holder.todoText.setText(subtask.getText());
    holder.checkBox.setChecked(subtask.isDone());

    holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> subtask.setDone(isChecked));

    holder.todoText.setOnClickListener(view -> {
      new AlertDialog.Builder(view.getContext())
              .setTitle("Usuń subtask")
              .setMessage("Czy na pewno chcesz usunąć ten subtask?")
              .setPositiveButton("Usuń", (dialog, which) -> {
                listener.onSubtaskDeleted(parentItem, subtask);
                notifyItemRemoved(position);
              })
              .setNegativeButton("Anuluj", null)
              .show();
    });
  }

  @Override
  public int getItemCount() {
    return subtasks.size();
  }

  static class SubtaskViewHolder extends RecyclerView.ViewHolder {
    TextView todoText;
    CheckBox checkBox;

    SubtaskViewHolder(View itemView) {
      super(itemView);
      todoText = itemView.findViewById(R.id.todo_text_subtask);
      checkBox = itemView.findViewById(R.id.todo_checkbox_subtask);
    }
  }
}
