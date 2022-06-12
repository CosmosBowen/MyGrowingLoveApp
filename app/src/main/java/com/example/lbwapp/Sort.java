package com.example.lbwapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Sort extends FragmentActivity {

    private boolean isFloatingMenuOpen=false;
    private int chosenIndex=0;
    private List<MyFloatingButton> floatingFABs=new ArrayList<>();
    private Animation openAnimation, closeAnimation;
    private RelativeLayout floatingMenuContainer;
    private String DAY="day";
    private String PLACE="location";
    private String LABEL="label";
    private String MAP="map";
    private String BACK="back";
    private String[] buttonNameList={BACK,MAP,PLACE,LABEL,DAY};
    private Activity mActivityContext=this;
    MyFloatingButton firstChosenButton;
    int translationX=-30;//越-，越往左移（屏幕内），因为是右下角
    int translationY=-180;//越-，越往上移（屏幕内），因为是右下角
    long latestTime=0;

    public SortBackground currentFragment;
    public String sortTag=LABEL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_sort);
        Log.d("flexbox_lifeCycle: ","Sort: onCreate");

        sortTag=MyApplication.FRAGMENT_STATE;

        //animation
        openAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.open_animation);
        closeAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.close_animation);
        //linear_interpolator
        openAnimation.setDuration(300);
        closeAnimation.setDuration(300);

        floatingMenuContainer=(RelativeLayout) findViewById(R.id.floatingMenuContainer);
        firstChosenButton=null;
        for(String buttonName: buttonNameList){
            MyFloatingButton fab=new MyFloatingButton(mActivityContext,buttonName);
            if(sortTag.equals(buttonName)){
                firstChosenButton=fab;
            }
            floatingFABs.add(fab);
            fab.animate()
                    .translationX(translationX)//-30
                    .translationY(translationY);//-50-130=-180
        }
//        firstChosenButton.setSize(FloatingActionButton.SIZE_NORMAL);
//        setMainButton(firstChosenButton);
//        replaceFragment(firstChosenButton.getName());

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("flexbox_lifeCycle: ","Sort: onResume");
        setMainButton(firstChosenButton);
        replaceFragment();
//        updateDisplay();

    }

    /*
    private void updateDisplay() {
        Timer timer = new Timer();
        Log.d("Timer",": "+timer.toString());
        timer.schedule(
                new TimerTask() {public void run() {updateDisplay();}},
                0,
                1000000
        );//Update text every secondE
    }

     */

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
//                        Toast.makeText(mActivityContext,otherButton.getName(),Toast.LENGTH_SHORT).show();
                        if(otherButton.getName().equals("back")){
                            //应退回到主页，不是finish
                            //因为上个页面除了主页外，还可能是photoPortrait啊，finish就回不到主页了
//                            finish();
                            Intent intent=new Intent(Sort.this,PhotoList.class);
                            startActivity(intent);
                        }else {
//                            SortBackground chosenFragment=chosenButton.getFragment();
                            //Test作为menu_Fragment，与背景sort_Fragment一起同在一个Activity里，所以，这里menu传给sort,让sort换fragment--update sort_fragment
                            sortTag=otherButton.getName();
                            MyApplication.FRAGMENT_STATE=sortTag;
                            replaceFragment();
                            setMainButton(otherButton);
                        }
                    }
                });
                floatingMenuContainer.addView(otherButton);
            }
        }
        floatingMenuContainer.addView(chosenButton);
    }

    private void openFloatingMenu(MyFloatingButton chosenButton){
        isFloatingMenuOpen=true;
        int radius=300;//radius=250 if count=3
        int count_outbtns=4;
        double theta=Math.PI*2/3/(count_outbtns-1);
        double changeTheta=Math.PI/6;
        int i=0;
        for(FloatingActionButton fab: floatingFABs){
            if(!fab.equals(chosenButton)){
//                float x= (float) (30-(radius*Math.cos(theta*i))-50);
//                float y= (float) (30-(radius*Math.sin(theta*i))-70);
                float x= (float) (30-(radius*Math.cos(theta*i-changeTheta))-20+translationX);//-20-30=-50
                float y= (float) (30-(radius*Math.sin(theta*i-changeTheta))-20+translationY);//-20-180=-200
                i=i+1;
                fab.animate()
                        .translationX(x)
                        .translationY(y)
                        .rotation(0)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(300);
            }
        }
    }

    private void closeFloatingMenu(MyFloatingButton chosenButton){
        isFloatingMenuOpen=false;
        for(FloatingActionButton fab: floatingFABs){
            if(!fab.equals(chosenButton)){
                fab.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .translationX(translationX)//-30
                        .translationY(translationY)//-50-130=-180
                        .rotation(360)
                        .setDuration(300);
            }
        }
    }

    private void replaceFragment(){
        Log.d("flexbox_lifeCycle: ","Sort: onResume里replaceFragment");
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        //从sort background拿到这个currentFragment，
        //然后，在onAttach、onCreateView、onActivityCreated对currentFragment.setSortTag
        if(!sortTag.equals(MAP)){
            currentFragment=new SortBackground();
            transaction.replace(R.id.sort_backgroud_layout,currentFragment);
        }else {
            MapFragment mapFragment=new MapFragment();
            transaction.replace(R.id.sort_backgroud_layout,mapFragment);
        }
//        newFragment.setSortTag(sortTag);//这时才createPage
//        transaction.replace(R.id.sort_backgroud_layout,currentFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}