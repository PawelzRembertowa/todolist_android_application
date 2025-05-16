package com.example.todolistapplication.todoitem;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class ToDoItem {

    private long id;
    private String text;
    private boolean isDone;
    private List<ToDoItem> subItems;
    private Date createdAt;
    private Date finishedAt; // null dopóki nie zakończone

    public ToDoItem(String text) {
        this.id = System.currentTimeMillis();
        this.text = text;
        this.isDone = false;
        this.subItems = new ArrayList<>();
    }

    // --- Gettery i settery ---

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public List<ToDoItem> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<ToDoItem> subItems) {
        this.subItems = subItems;
    }

    // --- Dodawanie sub-itemu ---
    public void addSubItem(ToDoItem item) {
        subItems.add(item);
    }

    // --- Usuwanie sub-itemu ---
    public void removeSubItem(ToDoItem item) {
        subItems.remove(item);
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getFinishedAt() {
        return finishedAt;
    }
}
