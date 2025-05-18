package com.example.todolistapplication.todoitem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ToDoItem {

  private long id;
  private String text;
  private boolean isDone;
  private List<ToDoItem> subItems;
  private Date createdAt;
  private Date finishedAt;
  private boolean isSubtask = false;

  // Constructor for the maintask
  public ToDoItem(String text) {
    this.id = System.currentTimeMillis();
    this.text = text;
    this.isDone = false;
    this.subItems = new ArrayList<>();
  }

  // Getters & Setters
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

  public void addSubItem(ToDoItem item) {
    subItems.add(item);
  }

  public void removeSubItem(ToDoItem item) {
    subItems.remove(item);
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public Date getFinishedAt() {
    return finishedAt;
  }

  public void setFinishedAt(Date finishedAt) {
    this.finishedAt = finishedAt;
  }

  public boolean isSubtask() {
    return isSubtask;
  }

  public void setSubtask(boolean subtask) {
    isSubtask = subtask;
  }
}

