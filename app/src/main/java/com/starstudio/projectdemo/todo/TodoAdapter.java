package com.starstudio.projectdemo.todo;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.todo.database.TodoEntity;
import com.starstudio.projectdemo.utils.OtherUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoHolder> {
    private List<TodoEntity> todoList;
    private OnTodoItemClickListener listener;

    public TodoAdapter() {
        todoList = new ArrayList<>();
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
        private final Button condition;

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

        @SuppressLint("ResourceAsColor")
        public void loadData(TodoEntity data) {
            time.setText(OtherUtil.getClockTime(data.getTodoTime()));
            content.setText(data.getContent());

            long now = System.currentTimeMillis();
            long todotime = Long.parseLong(data.getTodoTime());
            switch (OtherUtil.checkTimeLength(todotime, now)) {
                case 0:
                    time2now.setVisibility(View.GONE);
                    break;
                case 2:
                    time2now.setText(OtherUtil.getYearMonth(data.getTodoTime()));
                    break;
                default:
                    if (todotime > now)
                        time2now.setText("Tomorrow");
                    else
                        time2now.setText("Yesterday");
            }

            // 设置状态
            if (data.getCondition().equals("已完成")) {
                condition.setText("已完成");
                condition.setTextColor(R.color.my_primary);
                condition.setEnabled(false);
            } else {
                condition.setText("完成");
                condition.setTextColor(Color.WHITE);
                condition.setEnabled(true);
            }

        }
    }

    public static interface OnTodoItemClickListener {
        void onTodoItemClick(View v, TodoEntity data);
    }
}
