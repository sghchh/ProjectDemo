package com.starstudio.projectdemo.todo.fragments;

import android.os.Bundle;
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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.hms.image.vision.B;
import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.databinding.FragmentTodoBinding;
import com.starstudio.projectdemo.journal.adapter.RecyclerGridDivider;
import com.starstudio.projectdemo.todo.TodoAdapter;
import com.starstudio.projectdemo.todo.database.TodoDaoService;
import com.starstudio.projectdemo.todo.database.TodoEntity;

import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;

public class TodoFragment extends Fragment implements TodoAdapter.OnTodoItemClickListener {
    private TodoDaoService daoService;
    private FragmentTodoBinding binding;
    private List<TodoEntity> todoList;
    private TodoAdapter adapter;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        daoService = TodoDaoService.getInstance();
        setHasOptionsMenu(true);
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
            bundle.putInt("allNum", 20);
            bundle.putInt("doneNum", 12);
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
                        adapter.setTodoList(todoEntities);
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
