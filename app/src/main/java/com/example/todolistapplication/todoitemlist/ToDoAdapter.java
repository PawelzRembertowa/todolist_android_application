package com.example.todolistapplication.todoitemlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapplication.R;
import com.example.todolistapplication.todoitem.ToDoItem;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    private List<ToDoItem> items;
    private final OnItemActionListener listener;

    public interface OnItemActionListener {
        void onEdit(ToDoItem item, int position);
        void onDelete(ToDoItem item, int position);
    }

    public ToDoAdapter(List<ToDoItem> items, OnItemActionListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_item, parent, false);
        return new ToDoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        ToDoItem item = items.get(position);
        holder.todoText.setText(item.getText());
        holder.checkBox.setChecked(item.isDone());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setDone(isChecked);
        });

        holder.editButton.setOnClickListener(v -> listener.onEdit(item, position));
        holder.deleteButton.setOnClickListener(v -> listener.onDelete(item, position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ToDoViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView todoText;
        ImageButton editButton, deleteButton;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.todo_checkbox);
            todoText = itemView.findViewById(R.id.todo_text);
            editButton = itemView.findViewById(R.id.todo_edit);
            deleteButton = itemView.findViewById(R.id.todo_delete);
        }
    }
}

