package com.starstudio.projectdemo.todo.fragments;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.starstudio.projectdemo.R;
import com.starstudio.projectdemo.databinding.FragmentDialogAddTodoBinding;
import com.starstudio.projectdemo.todo.database.TodoDaoService;
import com.starstudio.projectdemo.todo.database.TodoEntity;
import com.starstudio.projectdemo.utils.DisplayMetricsUtil;
import com.starstudio.projectdemo.utils.OtherUtil;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;

import static android.app.AlertDialog.THEME_HOLO_LIGHT;

/**
 * created by sgh
 * 2021-8-12
 * 待办页面中“添加”待办事项的弹窗布局
 */
public class AddDialogFragment extends DialogFragment {
    private TodoDaoService todoDaoService;
    private FragmentDialogAddTodoBinding binding;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        todoDaoService = TodoDaoService.getInstance();

        binding = FragmentDialogAddTodoBinding.inflate(inflater, container, false);
        configView();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        // 设置点击外部不取消
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        //去掉dialog的标题，需要在setContentView()之前
        //this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = this.getDialog().getWindow();
        //这步是必须的
        window.setBackgroundDrawableResource(R.color.transparent);
        //去掉dialog默认的padding
        //window.getDecorView().setPadding(20, 20, 20, 20);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = DisplayMetricsUtil.getDisplayWidthPxiels(getActivity()) * 8 / 9;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void configView() {
        binding.dialogTodoTimeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        // 执行取消
        binding.dialogTodoAddCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        // 执行添加动作
        binding.dialogTodoAddSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoEntity todoEntity = new TodoEntity();
                if (binding.dialogTodoNameEdit.getText() == null || binding.dialogTodoNameEdit.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "请输入待办事项~", Toast.LENGTH_SHORT).show();
                    return;
                }
                todoEntity.setCondition("未完成");
                todoEntity.setContent(binding.dialogTodoNameEdit.getText().toString());
                if (binding.dialogTodoTimeEdit.getText() == null || binding.dialogTodoTimeEdit.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "请选择完成时间~", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 将选择的标准日期格式，转化为时间戳形式进行存储
                todoEntity.setTodoTime(OtherUtil.generateTimestamp(binding.dialogTodoTimeEdit.getText().toString()));
                todoDaoService.insert(todoEntity)
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(@NotNull Disposable d) {}

                            @Override
                            public void onComplete() {}

                            @Override
                            public void onError(@NotNull Throwable e) {
                                Toast.makeText(getActivity(), "添加失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(getActivity()
                // 设置主题
                , TimePickerDialog.THEME_HOLO_LIGHT
                // 绑定监听器
                , new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view,
                                          int hourOfDay, int minute) {
                        binding.dialogTodoTimeEdit.setText(hourOfDay + ":" + minute);
                    }
                }
                // 设置初始时间
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                ,true).show();
    }
}
