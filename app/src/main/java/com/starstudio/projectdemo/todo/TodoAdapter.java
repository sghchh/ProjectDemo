package com.starstudio.projectdemo.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.todo.database.TodoEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoHolder> {
    private List<TodoEntity> todoList;
    private OnTodoItemClickListener listener;

    public TodoAdapter(List<TodoEntity> list) {
        this.todoList = list;
    }

    public void setTodoList(List<TodoEntity> list) {
        this.todoList = list;
        notifyDataSetChanged();
    }

    public void setListener(OnTodoItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public TodoHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        TodoHolder holder = new TodoHolder(view);
        holder.setListener(this.listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TodoAdapter.TodoHolder holder, int position) {
        holder.loadData(todoList.get(position));
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    static class TodoHolder extends RecyclerView.ViewHolder {

        private OnTodoItemClickListener listener;
        private final TextView time, time2now, content;
        private final ImageView condition;

        public TodoHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.todo_time);
            time2now = itemView.findViewById(R.id.todo_time2now);
            content = itemView.findViewById(R.id.todo_content);
            condition = itemView.findViewById(R.id.todo_condition);
        }

        public void setListener(OnTodoItemClickListener listener) {
            this.listener = listener;
        }

        public void loadData(TodoEntity data) {
            time.setText(""+data.getTodoTime());
            content.setText(data.getContent());
        }
    }

    static interface OnTodoItemClickListener {
        void onTodoItemClick(View v, TodoEntity data);
    }
}
