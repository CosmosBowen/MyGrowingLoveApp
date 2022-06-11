package com.example.lbwapp;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.bumptech.glide.signature.AndroidResourceSignature;
import com.google.android.material.drawable.DrawableUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.print.PrinterId;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;

import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

public class FloatingButtonTest extends Activity {
    private boolean isFloatingMenuOpen=false;
    private int chosenIndex=0;
    private List<MyFloatingButton> floatingFABs=new ArrayList<>();
    private Animation openAnimation, closeAnimation;
    private RelativeLayout floatingMenuContainer;
    private String DAY="day";
    private String PLACE="location";
    private String LABEL="label";
    private String BACK="back";
    private String[] buttonNameList={BACK,PLACE,LABEL,DAY};
    private Activity mActivityContext=this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating_button_test);

        //animation
        openAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.open_animation);
        closeAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.close_animation);
        //linear_interpolator
        openAnimation.setDuration(300);
        closeAnimation.setDuration(300);

        floatingMenuContainer=(RelativeLayout) findViewById(R.id.floatingMenuContainer);
        MyFloatingButton firstChosenButton=null;
        for(String buttonName: buttonNameList){
            MyFloatingButton fab=new MyFloatingButton(mActivityContext,buttonName);
            if(buttonName.equals(DAY)){
                firstChosenButton=fab;
            }

//            fab.setBackgroundColor(Color.parseColor("#FFCA28"));
//            fab.setBackgroundResource(imageResourceId[i]);
//            RelativeLayout.LayoutParams fabLayoutParams=new RelativeLayout.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//            );
//            fabLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//            fabLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
//            fabLayoutParams.setMargins(0,0,20,20);
//            fab.setLayoutParams(fabLayoutParams);

//            floatingMenuContainer.addView(fab);
            floatingFABs.add(fab);
            fab.animate()
                    .translationX(-30)
                    .translationY(-30);
        }
//        firstChosenButton.setSize(FloatingActionButton.SIZE_NORMAL);
        setMainButton(firstChosenButton);


//        //FloatingActionButton
//        FloatingActionButton fab_day=(FloatingActionButton) findViewById(R.id.ic_day);
//        FloatingActionButton fab_back=(FloatingActionButton) findViewById(R.id.ic_back);
//        FloatingActionButton fab_place=(FloatingActionButton) findViewById(R.id.ic_place);
//        FloatingActionButton fab_label=(FloatingActionButton) findViewById(R.id.ic_label);
//        //childCount & floatingChildren
//        floatingChildren.add(fab_day);
//        floatingChildren.add(fab_back);
//        floatingChildren.add(fab_place);
//        floatingChildren.add(fab_label);

//        floatingFABs.get(3).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isFloatingMenuOpen){
//                    closeFloatingMenu();
//                }else {
//                    openFloatingMenu();
//                }
//            }
//        });
//
//        for(int index=0;index<3;index++){
//            chosenIndex = index;
//            final FloatingActionButton chosenFAB= floatingFABs.get(index);
//            chosenFAB.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //先关掉原来的界面
//                    closeFloatingMenu();
//                    //更新floatingChildren两者顺序
//                    floatingFABs.remove(chosenIndex);
//                    floatingFABs.add(chosenFAB);
//                    //更新界面
////                    floatingMenuContainer.removeViewAt(chosenIndex);
////                    floatingMenuContainer.addView(chosenFAB);
//                    floatingMenuContainer.removeAllViews();
//
//                }
//            });
//        }
//        updateMainFABClickEvent();

    }

//    private void updateMainFABClickEvent(){
//        floatingChildren.get(3).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isFloatingMenuOpen){
//                    closeFloatingMenu();
//                }else {
//                    openFloatingMenu();
//                }
//            }
//        });
//    }

    private void setMainButton(final MyFloatingButton chosenButton){
        if(floatingMenuContainer.getChildCount()>0){
            //真的执行吗？
            floatingMenuContainer.removeAllViews();
        }
//        chosenButton.setSize(FloatingActionButton.SIZE_NORMAL);
        chosenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFloatingMenuOpen){
                    closeFloatingMenu(chosenButton);
                }else {
                    openFloatingMenu(chosenButton);
                }
            }
        });
//        chosenButton.setSize(FloatingActionButton.SIZE_NORMAL);
        for(final MyFloatingButton otherButton:floatingFABs){
            if(!otherButton.equals(chosenButton)){
//                otherButton.setSize(FloatingActionButton.SIZE_MINI);
                otherButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeFloatingMenu(chosenButton);
                        floatingMenuContainer.removeAllViews();
                        //change page
                        Toast.makeText(mActivityContext,otherButton.getName(),Toast.LENGTH_SHORT).show();
//                        Fragment chosenFragment=chosenButton.getFragment();
                        //Test作为menu_Fragment，与背景sort_Fragment一起同在一个Activity里，所以，这里menu传给sort,让sort换fragment--update sort_fragment

                        setMainButton(otherButton);
                    }
                });
                floatingMenuContainer.addView(otherButton);
            }
        }
        floatingMenuContainer.addView(chosenButton);
    }

    private void openFloatingMenu(MyFloatingButton chosenButton){
        isFloatingMenuOpen=true;
        int radius=250;
        int count_outbtns=4;
        double theta=Math.PI*0.5/2;//(count_outbtns-1)
        int i=0;
        for(FloatingActionButton fab: floatingFABs){
            if(!fab.equals(chosenButton)){
                float x= (float) (30-(radius*Math.cos(theta*i))-50);
                float y= (float) (30-(radius*Math.sin(theta*i))-50);
                i=i+1;
                fab.animate()
                        .translationX(x)
                        .translationY(y)
                        .rotation(0)
                        .setDuration(300);
            }
        }
    }

    private void closeFloatingMenu(MyFloatingButton chosenButton){
        isFloatingMenuOpen=false;
        for(FloatingActionButton fab: floatingFABs){
            if(!fab.equals(chosenButton)){
                fab.animate()
                        .translationX(-30)
                        .translationY(-30)
                        .rotation(360)
                        .setDuration(300);
            }
        }
    }
}