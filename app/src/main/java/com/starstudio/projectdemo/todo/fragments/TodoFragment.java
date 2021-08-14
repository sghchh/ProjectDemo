package com.starstudio.projectdemo.todo.fragments;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.starstudio.projectdemo.MainActivity;
import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.databinding.FragmentTodoBinding;
import com.starstudio.projectdemo.todo.TodoAdapter;
import com.starstudio.projectdemo.todo.TodoService;
import com.starstudio.projectdemo.todo.database.TodoDaoService;
import com.starstudio.projectdemo.todo.database.TodoEntity;

import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Subscription;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import io.reactivex.CompletableObserver;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;

import static com.starstudio.projectdemo.todo.TODONotification.CHANNEL_ID;
import static com.starstudio.projectdemo.todo.TODONotification.NOTIFICATION_ID;

public class TodoFragment extends Fragment implements TodoAdapter.OnTodoItemClickListener {
    private TodoDaoService daoService;
    private FragmentTodoBinding binding;
    private TodoAdapter adapter;
    private Context context;
    private Intent intentService;
    private int allNum = 0;
    private int doneNum = 0;
    // 传递给后台Service的数据
    private List<Long> serviceData = new ArrayList<>();

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        daoService = TodoDaoService.getInstance();
        setHasOptionsMenu(true);
        intentService = new Intent(getContext(), TodoService.class);
        context = getContext();
        binding = FragmentTodoBinding.inflate(inflater, container, false);
        configView();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // 用来构建appbar最右侧的按钮菜单
    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_todo, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == R.id.todo_chart) {
            Bundle bundle = new Bundle();
            bundle.putInt("allNum", allNum);
            bundle.putInt("doneNum", doneNum);
            // 跳转到完成度分析页面
            NavHostFragment navHost =(NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
            navHost.getNavController().navigate(R.id.action_TodoFragment_to_TodoAnalyseFragment, bundle);
        }
        return super.onOptionsItemSelected(item);
    }

    // 该方法用来配置toolbar
    private void configView() {
        binding.toolbarTodo.titleText.setText(R.string.toolbar_todo);
        // 将Fragment的toolbar添加上
        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.toolbarTodo.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        binding.todoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDialogFragment dialogFragment = new AddDialogFragment();
                dialogFragment.show(getActivity().getSupportFragmentManager(), "addTodo");
            }
        });

        adapter = new TodoAdapter();
        adapter.setListener(this);
        binding.todoRecyler.setAdapter(adapter);
        binding.todoRecyler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        daoService.loadAllTodo()
                .subscribe(new FlowableSubscriber<List<TodoEntity>>() {
                    @Override
                    public void onSubscribe(@NotNull Subscription s) {
                        s.request(Integer.MAX_VALUE);
                    }

                    @Override
                    public void onNext(List<TodoEntity> todoEntities) {
                        Collections.sort(todoEntities, new Comparator<TodoEntity>() {
                            @Override
                            public int compare(TodoEntity todoEntity, TodoEntity t1) {
                                if (todoEntity.getCondition().equals("已完成") && t1.getCondition().equals("未完成"))
                                    return 1;
                                if (todoEntity.getCondition().equals("未完成") && t1.getCondition().equals("已完成"))
                                    return -1;
                                if (todoEntity.getTodoTime() < t1.getTodoTime())
                                    return -1;
                                if (todoEntity.getTodoTime() > t1.getTodoTime())
                                    return 1;
                                return 0;
                            }
                        });
                        adapter.setTodoList(todoEntities);
                        allNum = todoEntities.size();
                        doneNum = 0;
                        for (int i = 0; i < todoEntities.size(); i ++) {
                            if (todoEntities.get(i).getCondition().equals("已完成"))
                                doneNum ++;
                            else
                                serviceData.add(todoEntities.get(i).getTodoTime());
                        }

                        // 开启后台服务
                        long[] serviceDataArray = new long[allNum - doneNum];
                        for (int i = 0; i < serviceDataArray.length; i ++)
                            serviceDataArray[i] = serviceData.get(i);
                        intentService.putExtra("serviceData", serviceDataArray);
                        context.startService(intentService);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(getContext(), "获取待办事项失败：" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onTodoItemClick(View v, TodoEntity data) {
        DialogFragment todoUpdate = new UpdateDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("todoEntity", data);
        todoUpdate.setArguments(bundle);
        todoUpdate.show(getActivity().getSupportFragmentManager(), "updateTodo");
    }

    @Override
    public void onContidionChange(View view, TodoEntity data) {
        data.setCondition("已完成");
        daoService.update(data)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Toast.makeText(getContext(), "修改待办事项失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
