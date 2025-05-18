package com.example.todolistapplication.todoitemlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapplication.R;
import com.example.todolistapplication.todoitem.ToDoItem;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MainTaskViewHolder> {

    private final List<ToDoItem> items;
    private final ToDoListener listener;

    public ToDoAdapter(List<ToDoItem> items, ToDoListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MainTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
        return new MainTaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainTaskViewHolder holder, int position) {
        ToDoItem item = items.get(position);
        holder.todoText.setText(item.getText());
        holder.checkBox.setChecked(item.isDone());

        int number = 1;
        for (int i = 0; i < position; i++) {
            if (!items.get(i).isSubtask()) {
                number++;
            }
        }
        holder.numberText.setText(number + ".");

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> item.setDone(isChecked));
        holder.todoText.setOnClickListener(view -> listener.onEdit(item, position));
        holder.moreButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.todoitem_menu);

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                int id = menuItem.getItemId();
                if (id == R.id.edit) {
                    listener.onEdit(item, position);
                    return true;
                } else if (id == R.id.delete) {
                    listener.onDelete(item, position);
                    return true;
                } else if (id == R.id.add_subtask) {
                    listener.onAddSubtask(item, position);
                    return true;
                }
                return false;
            });

            popupMenu.show();
        });

        holder.subtaskRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.subtaskRecyclerView.setAdapter(new SubtaskAdapter(item.getSubItems(), item, listener));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class MainTaskViewHolder extends RecyclerView.ViewHolder {
        TextView todoText, numberText;
        CheckBox checkBox;
        ImageButton moreButton;
        RecyclerView subtaskRecyclerView;

        MainTaskViewHolder(View itemView) {
            super(itemView);
            todoText = itemView.findViewById(R.id.todo_text_main);
            checkBox = itemView.findViewById(R.id.todo_checkbox_main);
            numberText = itemView.findViewById(R.id.todo_number);
            moreButton = itemView.findViewById(R.id.moreButton_main);
            subtaskRecyclerView = itemView.findViewById(R.id.subtask_recyclerview);
        }
    }
}
