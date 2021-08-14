package com.starstudio.projectdemo.todo.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * created by sgh
 * 2021-8-12
 * "待办事项“对应的数据表类
 */
@Entity(tableName = "todo_table")
public class TodoEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "todo_time")
    private String todoTime;        // 所选择的待办时间

    private String content;        // 待办的内容

    private String condition;        // 待办项的状态

    @Override
    public String toString() {
        return "TodoEntity{" +
                "todoTime=" + todoTime +
                ", content='" + content + '\'' +
                ", condition='" + condition + '\'' +
                '}';
    }

    public String getTodoTime() {
        return todoTime;
    }

    public void setTodoTime(String todoTime) {
        this.todoTime = todoTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
