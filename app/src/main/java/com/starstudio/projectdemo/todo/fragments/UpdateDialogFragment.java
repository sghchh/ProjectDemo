package com.starstudio.projectdemo.todo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.starstudio.projectdemo.databinding.FragmentDialogAddTodoBinding;
import com.starstudio.projectdemo.databinding.FragmentDialogUpdateTodoBinding;
import com.starstudio.projectdemo.todo.database.TodoDaoService;
import com.starstudio.projectdemo.todo.database.TodoEntity;

import org.jetbrains.annotations.NotNull;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;

/**
 * created by sgh
 * 2021-8-12
 * 修改待办事项的弹窗
 */
public class UpdateDialogFragment extends DialogFragment {
    private TodoDaoService todoDaoService;
    private FragmentDialogUpdateTodoBinding binding;
    private TodoEntity originTodoEntity;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        assert getArguments() != null;
        originTodoEntity = (TodoEntity) getArguments().getBundle("TodoEntity").get("originTodoEntity");
        todoDaoService = TodoDaoService.getInstance();
        binding = FragmentDialogUpdateTodoBinding.inflate(inflater, container, false);
        configView();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void configView() {
        // 执行关闭窗口操作
        binding.dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        // 执行删除操作
        binding.dialogTodoUpdateDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoDaoService.delte(originTodoEntity)
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(@NotNull Disposable d) {

                            }

                            @Override
                            public void onComplete() {

                            }

                            @Override
                            public void onError(@NotNull Throwable e) {
                                Toast.makeText(getContext(), "添加失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                // 关闭页面
                closeDialog();
            }
        });

        // 执行添加动作
        binding.dialogTodoUpdateSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoEntity todoEntity = new TodoEntity();
                todoEntity.setCondition("未完成");
                todoEntity.setContent(binding.dialogTodoNameEdit.getText().toString());
                todoEntity.setTodoTime(System.currentTimeMillis());
                todoDaoService.insert(todoEntity)
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(@NotNull Disposable d) {
                            }

                            @Override
                            public void onComplete() {
                            }

                            @Override
                            public void onError(@NotNull Throwable e) {
                                Toast.makeText(getContext(), "添加失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                // 关闭页面
                closeDialog();
            }
        });
    }

    /**
     * 关闭该弹窗
     */
    private void closeDialog() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.remove(this);
        transaction.commit();
    }
}
