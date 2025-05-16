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

import com.example.todolistapplication.R;
import com.example.todolistapplication.todoitem.ToDoItem;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    private List<ToDoItem> items;
    private final OnItemActionListener listener;

    ImageButton moreButton;

    static class ToDoViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView todoText;
        TextView numberText;
        ImageButton moreButton;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.todo_checkbox);
            todoText = itemView.findViewById(R.id.todo_text);
            numberText = itemView.findViewById(R.id.todo_number);
            moreButton = itemView.findViewById(R.id.moreButton);
        }
    }

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

        // Ustawiamy tekst na podstawie modelu ToDoItem
        holder.todoText.setText(item.getText());
        holder.checkBox.setChecked(item.isDone());
        holder.numberText.setText((position + 1) + ".");

        // Po kliknięciu w EditText pozwalamy na edycję
        holder.todoText.setOnClickListener(v -> {
            // Umożliwiamy edycję tekstu
            holder.todoText.setFocusable(true);
            holder.todoText.setFocusableInTouchMode(true);
            holder.todoText.setCursorVisible(true);
            holder.todoText.requestFocus();

            // Pokazujemy klawiaturę
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(holder.todoText, InputMethodManager.SHOW_IMPLICIT);
        });

        // Zmieniamy tekst ToDoItem po utracie fokusu (np. po kliknięciu poza polem)
        holder.todoText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                item.setText(holder.todoText.getText().toString()); // Aktualizujemy model
                holder.todoText.setFocusable(false); // Wyłączamy możliwość edycji
                holder.todoText.setFocusableInTouchMode(false);
                holder.todoText.setCursorVisible(false);
            }
        });

        // Obsługa checkboxa
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setDone(isChecked);
        });

        // Obsługa menu (np. edycja, usuwanie) - Twój istniejący kod
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
                } else {
                    return false;
                }
            });

            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

