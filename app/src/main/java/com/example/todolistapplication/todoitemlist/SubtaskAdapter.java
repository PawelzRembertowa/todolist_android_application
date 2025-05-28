package com.example.todolistapplication.todoitemlist;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapplication.R;
import com.example.todolistapplication.todoitem.ToDoItem;

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
  public int getItemCount() {
    return subtasks.size();
  }

  @Override
  public void onBindViewHolder(@NonNull SubtaskViewHolder holder, int position) {
    ToDoItem subtask = subtasks.get(position);
    holder.todoText.setText(subtask.getText());
    holder.checkBox.setChecked(subtask.isDone());

    if (subtask.isDone()) {
      holder.todoText.setPaintFlags(holder.todoText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
      holder.todoText.setTextColor(Color.GRAY);
      holder.cardContainerSubtask.setBackgroundResource(R.drawable.card_background_done);
      holder.checkBox.setBackgroundColor(Color.parseColor("#008000"));
      holder.checkBox.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#008000")));
    } else {
      holder.todoText.setPaintFlags(holder.todoText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
      holder.todoText.setTextColor(Color.BLACK);
      holder.cardContainerSubtask.setBackgroundResource(R.drawable.card_background_pending);
      holder.checkBox.setBackgroundColor(Color.parseColor("#fbc31d"));
      holder.checkBox.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
    }

    holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
      subtask.setDone(isChecked);
      listener.onSaveTasks();
      if (isChecked) {
        holder.todoText.setPaintFlags(holder.todoText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.todoText.setTextColor(Color.GRAY);
        holder.cardContainerSubtask.setBackgroundResource(R.drawable.card_background_done);
        holder.checkBox.setBackgroundColor(Color.parseColor("#008000"));
        holder.checkBox.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#008000")));
      } else {
        holder.todoText.setPaintFlags(holder.todoText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        holder.todoText.setTextColor(Color.BLACK);
        holder.cardContainerSubtask.setBackgroundResource(R.drawable.card_background_pending);
        holder.checkBox.setBackgroundColor(Color.parseColor("#fbc31d"));
        holder.checkBox.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
      }

      parentItem.updateDoneStatusBasedOnSubtasks();

      // ✅Update Maintask if it is needed
      if (listener != null) {
        listener.onSubtaskStatusChanged(parentItem);
      }
    });

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

  static class SubtaskViewHolder extends RecyclerView.ViewHolder {
    TextView todoText;
    CheckBox checkBox;
    LinearLayout cardContainerSubtask;

    SubtaskViewHolder(View itemView) {
      super(itemView);
      todoText = itemView.findViewById(R.id.todo_text_subtask);
      checkBox = itemView.findViewById(R.id.todo_checkbox_subtask);
      cardContainerSubtask = itemView.findViewById(R.id.card_container_subtask);
    }
  }

}
