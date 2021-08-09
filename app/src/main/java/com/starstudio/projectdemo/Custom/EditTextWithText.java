package com.starstudio.projectdemo.Custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

import com.starstudio.projectdemo.utils.OtherUtil;

public class EditTextWithText extends androidx.appcompat.widget.AppCompatEditText {
    private String leadText = null;
    private String leadTextColor = "#FFFFFFFF";
    private int leadTextSize = 1;
    public EditTextWithText(Context context) {
        super(context);
    }

    public EditTextWithText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextWithText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTextSize(OtherUtil.sp2px(this.getContext(),leadTextSize));//自定义字大小
        paint.setColor(Color.parseColor(leadTextColor));//自定义字颜色
        canvas.drawText("  "+getLeadText(),2,getHeight()/2+20,paint);
        int paddingLeft = (int) paint.measureText(getLeadText());
        //设置距离光标距离左侧的距离
        setPadding(paddingLeft * 2, getPaddingTop(), getPaddingRight(), getPaddingBottom());
        super.onDraw(canvas);
    }
    public void setLeadText(String s){
        leadText = s;
    }
    private String getLeadText(){
        return leadText;
    }

    public String getLeadTextColor() {
        return leadTextColor;
    }

    public void setLeadTextColor(String leadTextColor) {
        this.leadTextColor = leadTextColor;
    }

    public int getLeadTextSize() {
        return leadTextSize;
    }

    public void setLeadTextSize(int leadTextSize) {
        this.leadTextSize = leadTextSize;
    }
}
