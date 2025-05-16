package com.example.todolistapplication.todoitemlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapplication.MainActivity;
import com.example.todolistapplication.R;
import com.example.todolistapplication.todoitem.ToDoItem;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_MAIN_TASK = 0;
    private static final int TYPE_SUB_TASK = 1;

    private List<ToDoItem> items;
    private final OnItemActionListener listener;

    ImageButton moreButton;

    public interface OnItemActionListener {
        void onEdit(ToDoItem item, int position);
        void onDelete(ToDoItem item, int position);

        void addSubtask(ToDoItem parentItem, ToDoItem subtask);
    }

    public ToDoAdapter(List<ToDoItem> items, OnItemActionListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        ToDoItem item = items.get(position);
        return item.isSubtask() ? 1 : 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == 0) {
            // Główne zadanie
            View view = inflater.inflate(R.layout.todo_item, parent, false);
            return new MainTaskViewHolder(view);
        } else {
            // Subtask
            View view = inflater.inflate(R.layout.todo_subtask_item, parent, false);
            return new SubtaskViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ToDoItem item = items.get(position);

        if (holder instanceof MainTaskViewHolder) {
            MainTaskViewHolder mainHolder = (MainTaskViewHolder) holder;

            // Dane głównego zadania
            mainHolder.todoText.setText(item.getText());
            mainHolder.checkBox.setChecked(item.isDone());
            mainHolder.numberText.setText((position + 1) + ".");

            // Edycja tekstu
            mainHolder.todoText.setOnClickListener(v -> {
                mainHolder.todoText.setFocusable(true);
                mainHolder.todoText.setFocusableInTouchMode(true);
                mainHolder.todoText.setCursorVisible(true);
                mainHolder.todoText.requestFocus();

                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mainHolder.todoText, InputMethodManager.SHOW_IMPLICIT);
            });

            mainHolder.todoText.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    item.setText(mainHolder.todoText.getText().toString());
                    mainHolder.todoText.setFocusable(false);
                    mainHolder.todoText.setFocusableInTouchMode(false);
                    mainHolder.todoText.setCursorVisible(false);
                }
            });

            // Checkbox
            mainHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.setDone(isChecked);
            });

            // Menu (edycja / usuwanie / dodaj subtask)
            mainHolder.moreButton.setOnClickListener(view -> {
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
                        if (view.getContext() instanceof MainActivity) {
                            MainActivity mainActivity = (MainActivity) view.getContext();
                            mainActivity.showAddSubtaskDialog(item, position);
                        }
                        return true;
                    }
                    return false;
                });

                popupMenu.show();
            });

        } else if (holder instanceof SubtaskViewHolder) {
            SubtaskViewHolder subtaskHolder = (SubtaskViewHolder) holder;

            // Dane subtaska
            subtaskHolder.todoText.setText(item.getText());
            subtaskHolder.checkBox.setChecked(item.isDone());

            // Można dodać obsługę kliknięcia czy checkboxa, jeśli chcesz
            subtaskHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.setDone(isChecked);
            });
        }
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    // Widok dla głównego zadania
    public static class MainTaskViewHolder extends RecyclerView.ViewHolder {
        TextView todoText;
        CheckBox checkBox;
        TextView numberText;
        ImageButton moreButton;

        public MainTaskViewHolder(View itemView) {
            super(itemView);
            todoText = itemView.findViewById(R.id.todo_text_main);
            checkBox = itemView.findViewById(R.id.todo_checkbox_main);
            numberText = itemView.findViewById(R.id.todo_number);
            moreButton = itemView.findViewById(R.id.moreButton_main);
        }
    }
    // Widok dla podzadań (subtasków)
    public static class SubtaskViewHolder extends RecyclerView.ViewHolder {
        TextView todoText;
        CheckBox checkBox;

        public SubtaskViewHolder(View itemView) {
            super(itemView);
            todoText = itemView.findViewById(R.id.todo_text_subtask);
            checkBox = itemView.findViewById(R.id.todo_checkbox_subtask);
        }
    }

    public void addSubtask(ToDoItem parentItem, ToDoItem subtask) {
        int parentIndex = items.indexOf(parentItem);
        if (parentIndex != -1) {
            parentItem.addSubItem(subtask);  // dodajemy do modelu
            items.add(parentIndex + 1, subtask);  // dodajemy do listy adaptera (pod spodem)
            notifyItemInserted(parentIndex + 1);
        }
    }

}