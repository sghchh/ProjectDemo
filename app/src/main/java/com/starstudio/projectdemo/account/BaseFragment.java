package com.starstudio.projectdemo.account;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.greenrobot.greendao.annotation.NotNull;

public abstract class BaseFragment extends Fragment {


    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        Log.e(getClass().getSimpleName(),
                getClass().getSimpleName() + ":->" + "onAttach()");
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(getClass().getSimpleName(),
                getClass().getSimpleName() + ":->" + "onCreat()");
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        Log.e(getClass().getSimpleName(),
                getClass().getSimpleName() + ":->" + "onCreatView()");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(getClass().getSimpleName(),
                getClass().getSimpleName() + ":->" + "onActivityCreated()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(getClass().getSimpleName(),
                getClass().getSimpleName() + ":->" + "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(getClass().getSimpleName(),
                getClass().getSimpleName() + ":->" + "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(getClass().getSimpleName(),
                getClass().getSimpleName() + ":->" + "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(getClass().getSimpleName(),
                getClass().getSimpleName() + ":->" + "onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(getClass().getSimpleName(),
                getClass().getSimpleName() + ":->" + "onDestoryView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(getClass().getSimpleName(),
                getClass().getSimpleName() + ":->" + "onDestory()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(getClass().getSimpleName(),
                getClass().getSimpleName() + ":->" + "onDetach()");
    }
}
