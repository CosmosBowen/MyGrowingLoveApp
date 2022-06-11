package com.example.lbwapp;

import android.app.Activity;
import android.content.Intent;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditPhotoInfo extends Activity {
    CardItemEntity item;
    String filePath;
    TextView tv_path;
    TextView tv_time;
    RelativeLayout parentRelative;
    MaskImageView blur;
    MyEditText editText_title_card;
    MyTextView tv_dayNumber_card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_edit_photo_info);
        //可模糊了！！

//        ImageView iv=findViewById(R.id.iv);
        Button buttonChooseAgain=findViewById(R.id.btn_chooseAgain);
        tv_path =findViewById(R.id.tv_path);
        tv_time = findViewById(R.id.tv_time);
        tv_dayNumber_card =findViewById(R.id.dayNumber_card);
        //******************
        blur=findViewById(R.id.blur);
        parentRelative=findViewById(R.id.parentRelative);

        ////*******************对父组件设置“隐藏键盘”的监听事件*******************
//        blur.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
//            }
//        });
//        parentRelative.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
//            }
//        });

        //对输入框直接监听“获取焦点”的事件
//        editText_title_card=findViewById(R.id.title_card);
//        editText_title_card.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                    //获得焦点
//                    editText_title_card.setCursorVisible(true);
//                    try {
//                        Thread.sleep(3000);
//                        hasFocus=false;
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                }else {
//                    //失去焦点
//                    InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
//                    editText_title_card.setCursorVisible(false);
//                }
//            }
//        });
        ////***********************************************************

        //********<EditPhotoInfo>升级了，和<Detail>同等级别，都通过Bundle传递获取数据item->图片路径filePath
        final Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        item = (CardItemEntity) bundle.getSerializable("CardItemEntity");
        filePath=item.getImagePath_card();
//        filePath = intent.getStringExtra("filePath");
        tv_path.setText("图片绝对路径："+filePath);
        RequestOptions requestOptions1 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
        //将照片显示在 ivImage上
        Glide.with(this).load(filePath)
                .apply(requestOptions1)
                .apply(blur.setGaussBlur())
                .into(blur);

        buttonChooseAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePath="";
                //finish();
                Intent intentChooseAgain=new Intent(EditPhotoInfo.this,TakePhotos.class);
                startActivity(intentChooseAgain);
            }
        });
        ////**************获取图片的拍摄日期****************
        ExifInterface exifInterface=null;
        try {
            exifInterface=new ExifInterface(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String takenTime=exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
        String curTime = new SimpleDateFormat("yyyy:MM:dd").format(Calendar.getInstance().getTime());
        String showText=calculateDaysBetweenFromDates(curTime,takenTime.substring(0,10));

        String latitudeValue=exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        String longitudeValue=exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
//        tv_time.setText("图片拍摄日期："+takenTime+"\n"+curTime+"\n"+showText);
        tv_time.setText("图片拍摄位置："+"\n"+"latitude: "+"\n"+latitudeValue+"\n"+"longitude:"+"\n"+longitudeValue);
        tv_dayNumber_card.setText("📅 "+showText);
        ////**********************************************
    }
    protected String calculateDaysBetweenFromDates(String curTime,String takenTime){
        //<string>“yy:MM:dd” -> <int>Y/M/D
        int curYear=Integer.parseInt(curTime.substring(0,4));
        int curMonth=Integer.parseInt(curTime.substring(5,7));
        int curDay=Integer.parseInt(curTime.substring(8,10));

        int takenYear=Integer.parseInt(takenTime.substring(0,4));
        int takenMonth=Integer.parseInt(takenTime.substring(5,7));
        int takenDay=Integer.parseInt(takenTime.substring(8,10));

        //calculate deltaY/M/D
        int deltaYear=curYear-takenYear;
        int deltaMonth=curMonth-takenMonth;
        if(deltaMonth<0){
            deltaYear=deltaYear-1;
            deltaMonth=deltaMonth+12;
        }
        int deltaDay=curDay-takenDay;
        if(deltaDay<0){
            deltaMonth=deltaMonth-1;
            deltaDay=deltaDay+getDayNumberFromMonth(takenMonth);
        }

//        //计算天数，粗略地
//        int dayNumber=deltaYear*365+deltaMonth*30+deltaDay;
//        item.setDayNumber_card(dayNumber);

        //showText
        String showText="";
        if(deltaYear>0){
            showText=deltaYear+"年前";
        }else if (deltaMonth>0){
            showText=deltaMonth+"个月前";
        }else if(deltaDay>0){
            showText=deltaDay+"天前";
        }else {
            showText="今天";
        }

        //测试
        //return "showText: "+showText+" ,年: "+deltaYear+" ,月: "+deltaMonth+" ,日: "+deltaDay;
        return showText;
    }

    protected int getDayNumberFromMonth(int month){
        int dayNumber=0;
        switch (month){
            case 4:
            case 6:
            case 9:
            case 11:
                dayNumber=30;
                break;
            case 2:
                if(month%4==0){
                    dayNumber=29;
                }else {
                    dayNumber=28;
                }
                break;
            default:
                dayNumber=31;//1,3,5,7,8,10,12
                break;
        }
        return dayNumber;
    }
}