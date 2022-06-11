package com.example.lbwapp;

        import android.content.Context;
        import android.util.AttributeSet;
        import android.widget.EditText;

        import androidx.annotation.Nullable;

public class MyEditText extends androidx.appcompat.widget.AppCompatEditText {//定义自定义的EditText，这样每次使用MyEditText就能使用我的自定义字体
    public MyEditText(Context context) {//定义四种构造方法
        super(context);
        //设置字体
        setTypeface(MyApplication.getInstance().getTypeface());
    }

    public MyEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //设置字体
        setTypeface(MyApplication.getInstance().getTypeface());
    }

    public MyEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置字体
        setTypeface(MyApplication.getInstance().getTypeface());
    }
}
