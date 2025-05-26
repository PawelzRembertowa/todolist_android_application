package com.example.todolistapplication.todoitemlist;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
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


    // Hide and display subtasks
    boolean isExpanded = item.isExpanded();
    holder.subtaskRecyclerView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    holder.expandCollapseButton.setImageResource(
            isExpanded ? R.drawable.ic_expand_less : R.drawable.ic_expand_more
    );

    // Expanding button
    holder.expandCollapseButton.setOnClickListener(v -> {
      item.setExpanded(!item.isExpanded());
      notifyItemChanged(position);
    });


    if (item.isDone()) {
      holder.todoText.setPaintFlags(holder.todoText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
      holder.todoText.setTextColor(Color.GRAY);
      holder.cardContainer.setBackgroundResource(R.drawable.card_background_done);
      holder.checkBox.setBackgroundColor(Color.parseColor("#008000"));
      holder.checkBox.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#008000")));
    } else {
      holder.todoText.setPaintFlags(holder.todoText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
      holder.todoText.setTextColor(Color.BLACK);
      holder.cardContainer.setBackgroundResource(R.drawable.card_background_pending);
      holder.checkBox.setBackgroundColor(Color.parseColor("#fbc31d"));
      holder.checkBox.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
    }

    holder.checkBox.setOnCheckedChangeListener(
            (buttonView, isChecked) -> {
              item.setDone(isChecked);
              if (isChecked) {
                holder.todoText.setPaintFlags(holder.todoText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.todoText.setTextColor(Color.GRAY);
                holder.cardContainer.setBackgroundResource(R.drawable.card_background_done);
                holder.checkBox.setBackgroundColor(Color.parseColor("#008000"));
                holder.checkBox.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#008000")));
              } else {
                holder.todoText.setPaintFlags(holder.todoText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.todoText.setTextColor(Color.BLACK);
                holder.cardContainer.setBackgroundResource(R.drawable.card_background_pending);
                holder.checkBox.setBackgroundColor(Color.parseColor("#fbc31d"));
                holder.checkBox.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
              }
            }
    );
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
    ImageButton expandCollapseButton;
    RecyclerView subtaskRecyclerView;
    LinearLayout cardContainer;

    MainTaskViewHolder(View itemView) {
      super(itemView);
      todoText = itemView.findViewById(R.id.todo_text_main);
      checkBox = itemView.findViewById(R.id.todo_checkbox_main);
      numberText = itemView.findViewById(R.id.todo_number);
      moreButton = itemView.findViewById(R.id.moreButton_main);
      expandCollapseButton = itemView.findViewById(R.id.expandCollapseButton);
      subtaskRecyclerView = itemView.findViewById(R.id.subtask_recyclerview);
      cardContainer = itemView.findViewById(R.id.card_container);
    }
  }
}
