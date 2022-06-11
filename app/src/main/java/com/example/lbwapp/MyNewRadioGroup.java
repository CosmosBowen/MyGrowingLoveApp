package com.example.lbwapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MyNewRadioGroup extends FlexboxLayout {


    public List<MyRadioButton> btnList=new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public MyNewRadioGroup(Context context) {
        super(context);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void myGroupAddBtn(String text, String color, boolean isRealLabel, boolean isFunctionalLabel){
        final MyRadioButton button=new MyRadioButton(getContext(),isRealLabel,isFunctionalLabel);
        MyRadioButton.LayoutParams rb_LayoutParams=new MyRadioButton.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

//        ViewGroup.LayoutParams lp = button.getLayoutParams();
//        if (lp instanceof FlexboxLayoutManager.LayoutParams) {
//            Log.d("test-flexboxLp","flexboxLp");
//            FlexboxLayoutManager.LayoutParams flexboxLp = (FlexboxLayoutManager.LayoutParams) lp;
//            flexboxLp.setFlexGrow(100.0f);
//        }

        rb_LayoutParams.setMargins(200,200,200,200);
        rb_LayoutParams.setLayoutDirection(LAYOUT_DIRECTION_LTR);
        button.setLayoutParams(rb_LayoutParams);
        button.setMyTextView(text);
        button.setImageView(Color.parseColor(color));
        addView(button);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void myGroupAddBtn(String text, String color){
        myGroupAddBtn(text, color,true,false);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void putFunctionalBtns(){
        myGroupAddBtn("empty","#ABACAC",true,true);
        myGroupAddBtn("+","#FB8C00",false,true);
    }

    @Override
    public void addView(final View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof MyRadioButton) {
            final MyRadioButton button=(MyRadioButton)child;
            btnList.add(button);
        }
        super.addView(child, index, params);
    }

    @Override
    public void removeAllViews(){
        btnList.clear();
        super.removeAllViews();
    }


}













