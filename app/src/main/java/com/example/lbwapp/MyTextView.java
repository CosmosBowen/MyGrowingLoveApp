package com.example.lbwapp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class MyTextView extends androidx.appcompat.widget.AppCompatTextView {//定义自定义的TextView，这样每次使用MyTextView就能使用我的自定义字体
    public MyTextView(Context context) {//定义四种构造方法
        super(context);
        //设置字体
        setTypeface(MyApplication.getInstance().getTypeface());
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //设置字体
        setTypeface(MyApplication.getInstance().getTypeface());
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置字体
        setTypeface(MyApplication.getInstance().getTypeface());
    }
}
