package com.example.todolistapplication.todoitemlist;

import com.example.todolistapplication.todoitem.ToDoItem;

import java.util.ArrayList;
import java.util.List;

public class ToDoItemList {

    private List<ToDoItem> items;

    public ToDoItemList() {
        this.items = new ArrayList<>();
    }

    // --- Dodawanie zadania ---
    public void addItem(ToDoItem item) {
        items.add(item);
    }

    // --- Usuwanie zadania ---
    public boolean removeItem(ToDoItem item) {
        return items.remove(item);
    }

    // --- Pobieranie wszystkich zadań ---
    public List<ToDoItem> getItems() {
        return items;
    }

    // --- Wyszukiwanie zadania po ID ---
    public ToDoItem getItemById(long id) {
        for (ToDoItem item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    // --- Czyszczenie listy ---
    public void clearAll() {
        items.clear();
    }

    // --- Liczenie zadań zakończonych ---
    public int countCompleted() {
        int count = 0;
        for (ToDoItem item : items) {
            if (item.isDone()) {
                count++;
            }
        }
        return count;
    }

    // --- Liczenie zadań ogólnie ---
    public int size() {
        return items.size();
    }
}

