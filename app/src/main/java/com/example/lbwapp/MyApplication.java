package com.example.lbwapp;
import android.app.Activity;
import android.app.Application;
import android.graphics.Typeface;
import android.widget.ListView;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyApplication extends Application {//设置自定义的字体——康华娃娃体
    private Typeface typeface;
    private static MyApplication instance;
    private List<Activity> activityList=new LinkedList<Activity>();
    public static String FRAGMENT_STATE="label";//0:默认label，1:day，2:location
    public static float ZOOM=4;
    public static LatLng MapLocation=new LatLng(35.86166,104.195397);//初始聚焦点：中国
//    public static String latitudeAndLongtitudeString="37.09024,-95.712891";//""39.87234878527778,116.47469329833334";
    public static int STATE=0;//0:默认无事，1:Choose->Portrait点击确认该照片，2:PhotoList点击"主页"某张照片，3:从具体一堆"分类"里面点击的某张照片 ,4:Map地点
    public static int position=0;//“主页”或“分类”里被点击照片的位置
    public static int anotherPosition=0;//只是分类里面的第几个
    public static List<CardItemEntity> showEachTagList=new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance= (MyApplication) getApplicationContext();
        typeface=Typeface.createFromAsset(instance.getAssets(),"fonts/huakang.ttc");//下载的字体

        SDKInitializer.setAgreePrivacy(getApplicationContext(), true);

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);


        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

    }
    public static MyApplication getInstance(){
        if(null==instance){
            instance=new MyApplication();
        }
        return instance;
    }

    //V2.0
//    private static String theLastOpennedDate;
//    private static int addDays;
//    public static String getTheLastOpennedDate() {
//        return theLastOpennedDate;
//    }
//
//    public static void setTheLastOpennedDate(String date){
//        theLastOpennedDate=date;
//    }
//
//    public static void setAddDays(int days){
//        addDays=days;
//    }

    public Typeface getTypeface(){
        return typeface;
    }
    public void setTypeface(Typeface typeface){
        this.typeface=typeface;
    }

    public void addActivity(Activity activity){
        activityList.add(activity);
    }

    public void exit(){
        for(Activity activity:activityList){
            activity.finish();
        }
        System.exit(0);
    }
}
