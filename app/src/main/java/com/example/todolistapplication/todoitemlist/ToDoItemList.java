package com.example.todolistapplication.todoitemlist;

import com.example.todolistapplication.todoitem.ToDoItem;

import java.util.ArrayList;
import java.util.List;

public class ToDoItemList {

    private List<ToDoItem> items;

    public ToDoItemList() {
        this.items = new ArrayList<>();
    }

    public void addItem(ToDoItem item) {
        items.add(item);
    }

    public boolean removeItem(ToDoItem item) {
        return items.remove(item);
    }

    public List<ToDoItem> getItems() {
        return items;
    }

    public ToDoItem getItemById(long id) {
        for (ToDoItem item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public void clearAll() {
        items.clear();
    }

    public int countCompleted() {
        int count = 0;
        for (ToDoItem item : items) {
            if (item.isDone()) {
                count++;
            }
        }
        return count;
    }

    public int size() {
        return items.size();
    }
}

