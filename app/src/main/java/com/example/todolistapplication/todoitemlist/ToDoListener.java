package com.example.todolistapplication.todoitemlist;

import com.example.todolistapplication.todoitem.ToDoItem;

public interface ToDoListener {
  void onEdit(ToDoItem item, int position);
  void onDelete(ToDoItem item, int position);
  void onAddSubtask(ToDoItem parentItem, int parentPosition);
  void onSubtaskDeleted(ToDoItem parentItem, ToDoItem subtask);

  void onSubtaskStatusChanged(ToDoItem parentItem);

  void onSaveTasks();

}
