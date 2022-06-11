package com.example.lbwapp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.print.PrinterId;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyFloatingButton extends FloatingActionButton {

    private int imageResourceId;
    private String name="";
//    private SortBackground fragment=null;

    public MyFloatingButton(Context context, String buttonName) {
        super(context);
        name=buttonName;
//        fragment=new SortBackground();
        switch (buttonName){
            case "day":
                imageResourceId=R.drawable.ic_day;
                break;
            case "location":
                imageResourceId=R.drawable.footprint;
                break;
            case "label":
                imageResourceId=R.drawable.ic_label;
                break;
            case "map":
                imageResourceId=R.drawable.earth;
                break;
            case "back":
                imageResourceId=R.drawable.ic_back;
                break;
            default:
                imageResourceId=0;
        }
//        fragment.setSortTag(buttonName);
        setImageResource(imageResourceId);
        if(buttonName.equals("back")){
            setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FB8C00")));
        }else {
            setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFCA28")));
        }
    }

    public String getName() {
        return name;
    }
//    public SortBackground getFragment() {
//        return fragment;
//    }
}
