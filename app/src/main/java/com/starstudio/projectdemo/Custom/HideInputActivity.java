package com.starstudio.projectdemo.Custom;

import android.content.Context;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.starstudio.projectdemo.utils.SharedPreferencesUtils;

/**
 * EditText失去焦点后，光标消失
 */
public class HideInputActivity extends AppCompatActivity {
    protected SharedPreferencesUtils sharedPreferencesUtils;

    /**
     * 获取点击事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            //如果点击在非EditView位置，隐藏光标和键盘，同时保存修改后的内容
            if (isHideInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
                view.clearFocus();
                sharedPreferencesUtils.putString(view.getId() + "",((EditText)view).getText().toString());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判定是否需要隐藏
     */
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {     //判断当前处于焦点的v控件(v布局)是否是 EditText 控件
            int[] l = {0, 0};
            v.getLocationInWindow(l);   //获得 v 的坐标（控件左上角的点）
            //通过获得的 v 的左上角点坐标以及 v 的长宽，计算出 v 的上下左右的坐标
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            //判断手指点击点的坐标是否在 EditText控件内部，是则返回false，否则返回true
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 隐藏软键盘
     */
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
