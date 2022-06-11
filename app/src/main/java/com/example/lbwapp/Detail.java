package com.example.lbwapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.JustifyContent;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.M)
public class Detail extends Activity {



    //testShow
//    private TextView textShow;

//    HashMap<String,String> Name2Code=new HashMap<>();
    Countries countries=null;

    private Activity mActivityContext=this;

    //日历：
    private CardView dayNumber;
    private MyTextView dayNumber_card;//照片距离现在天数
    // 动态生成CalendarView(子组件)->CardView(父组件)
    private CardView calendarCardView;
    private CalendarView calendarView;
    private final String curTime=new SimpleDateFormat("yyyy:MM:dd").format(Calendar.getInstance().getTime());//2022:03:28
    String dateTaken="0000:00:00";//照片创建时间
    boolean isDateEditable=false;
    boolean isDateEdited=false;
    boolean isCalendarOpen=false;//用来表示“日历”是否已经打开或关上
    long selectedDate=0;//1648637453769;
    int mYear=0;
    int mMonth=0;
    int mDayOfMonth=0;



    //照片标题
    String title;
    MyEditText title_card;
    Boolean isTitleChanged=false;

    //照片描述
    String description;
    MyEditText text_card;
    Boolean isTextChanged=false;

    //标签
    private CardView label;
    private ImageView label_color_card;
    private MyTextView label_text_card;

    //标签-- 动态生成动态生成CalendarView(子组件)->LinearLayout/CardView(父组件)
    private MyNewRadioGroup rg;
    private CardView labelCardViewContainer;
    boolean isLabelEdited=false;
    boolean isStoredLabelsEdited=false;
    boolean isLabelOpen=false;//用来表示“标签”是否已经打开或关上
    String labelChosen="empty";//照片标签
    private String selectedText="empty";//选择的标签

    //数据库
    private MyDatabaseHelper dbHelper;
    public Map<String,String> storedLabels=new LinkedHashMap<>();//相当于从数据库里面拿数据
    private boolean isGetLabelLibrary=false;
//    CardItemEntity item;

    //照片背景
    String imagePath;//照片绝对路径
    MaskImageView blur;//照片背景

    //照片拍摄地点
    String locationCompleteText;
    String newLocationCompleteText;
    CardView location_card;//原身：place_card
    MyTextView location_card_CountryCode;
    MyTextView location_card_CountryName;
    MyTextView location_card_Admin;
    MyTextView location_card_Feature;
    String location_CountryCode;
    String location_CountryName;
    String location_Admin;
    String location_Feature;
    String latitudeAndLongitude;
    boolean isCountryEditable=false;
    Boolean islocationInfoChanged=false;



    RelativeLayout parentRelative;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        countries=new Countries();

        //给三个变量赋初值
        title=getString(R.string.defaultTitle);
        description=getString(R.string.defaultDescription);
        locationCompleteText=getString(R.string.defaultLocation);
//        String dateTaken="0000:00:00";//今天
//        int isDateEditableInt=0;

        //浮在FrameLayout最上层：testShow<TextView>
//        textShow=findViewById(R.id.testShow);
//        textShow.setText("selectedDate:\n"+selectedDate);

        //获取今天
//        isDateEdited=false;//不知道需不需要，确定的一点：每次打开这个界面，就会走onCreate，但是不知道，变量是不是初始化，还是继续以前的

        //数据库
        dbHelper=new MyDatabaseHelper(this,MyDatabaseHelper.DATABASE_NAME,null, MyDatabaseHelper.DATABASE_VERSION);
        dbHelper.getWritableDatabase();
        if(isGetLabelLibrary==false){
            getStoredLabels();
            Log.d("labelTest",">>>>>getStoredLabels");
            isGetLabelLibrary=true;
        }

        isGetLabelLibrary=false;
        //<<<<<<<<<<<<<组件>>>>>>>>>>>>>>>
        //获取布局中的组件

        //背景
        blur = findViewById(R.id.blur);

        //标题和描述
        title_card=findViewById(R.id.title_card);
        text_card=findViewById(R.id.text_card);

        //日期
        calendarCardView=findViewById(R.id.calendarCardView);
        dayNumber=findViewById(R.id.dayNumber);
        dayNumber_card=findViewById(R.id.dayNumber_card);

        //标签
        label=findViewById(R.id.label);
        label_color_card=findViewById(R.id.label_color_card);
        label_text_card=findViewById(R.id.label_text_card);
        //展开的标签选择容器
        labelCardViewContainer =findViewById(R.id.labelCardViewContainer);
//        setLabel(selectedText);

        //地点
        location_card=findViewById(R.id.location_card);
        location_card_CountryCode=findViewById(R.id.location_card_CountryCode);
        location_card_CountryName=findViewById(R.id.location_card_CountryName);
        location_card_Admin=findViewById(R.id.location_card_Admin);
        location_card_Feature=findViewById(R.id.location_card_Feature);

        parentRelative=findViewById(R.id.parentRelative);

        isTitleChanged=false;
        isTextChanged=false;
        //EditText是否变化
        title_card.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isTitleChanged=true;
                Log.d("MainActivity","title_card editText changed, content is "+title_card.getText().toString());
            }
        });
        text_card.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isTextChanged=true;
                Log.d("MainActivity","text_card editText changed, content is "+text_card.getText().toString());
            }
        });
        dayNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDateEditable){
//                    Toast.makeText(Detail.this,"可编辑📅 ✔ ",Toast.LENGTH_SHORT).show();
                    if(isCalendarOpen==false){
                        isCalendarOpen=true;
                        if(calendarCardView.indexOfChild(calendarView)==-1){
                            calendarView=new CalendarView(mActivityContext);
                            CalendarView.LayoutParams layoutParams=new CalendarView.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            );
                            layoutParams.setMargins(0,10,0,0);
                            calendarView.setLayoutParams(layoutParams);

                            //我放弃了... 下面这行以一运行就报错，找其他方式，比如动画
                            //想动态设置calendarCardView的marginTop，没事，总有解决办法的！
//                            CardView.LayoutParams lp=(CardView.LayoutParams) calendarCardView.getLayoutParams();
//                            lp.setMargins(0,10,0,0);
//                            calendarCardView.setLayoutParams(lp);


                            //year(-1900) month(0-11) date(1-31)
//                    Date maxDate=new Date(curDate[0]-1900,curDate[1],curDate[2]);
//                    calendarView.setMaxDate(maxDate.getTime());
                            calendarView.setMaxDate(calendarView.getDate());

                            //setDate
                            calendarView.setDate(selectedDate);
                            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                                @Override
                                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                                    isDateEdited=true;
                                    CalendarView changedCalendarView=new CalendarView(mActivityContext);
//                                    textShow.setText("onSelectedDayChange:\n"+dayOfMonth);
//                            String msg="选择了："+year+"年/"+(month+1)+"月/"+dayOfMonth+"日";
//                            Toast.makeText(calendarTest.this,msg,Toast.LENGTH_SHORT).show();
                                    int trueMonth=month+1;
                                    String monthString=String.valueOf(trueMonth);
                                    String dayString=String.valueOf(dayOfMonth);
                                    if(trueMonth<10){
                                        monthString="0"+trueMonth;
                                    }
                                    if(dayOfMonth<10){
                                        dayString="0"+dayOfMonth;
                                    }
                                    dateTaken=year+":"+monthString+":"+dayString;
                                    String daysText="";
                                    daysText=MyDeltaDate.getDaysTextFromDates(curTime,dateTaken);

                                    mYear=year;
                                    mMonth=trueMonth;
                                    mDayOfMonth=dayOfMonth;
//                                    textShow.setText("选择的日期月份: "+month);

                                    dayNumber_card.setText(daysText);//daysText/takenTime
                                }
                            });
                            calendarCardView.addView(calendarView);
                        }
                    }else {
                        Calendar selectedCalendar=Calendar.getInstance();
                        selectedCalendar.set(Calendar.YEAR,mYear);
                        selectedCalendar.set(Calendar.MONTH,mMonth-1);
                        selectedCalendar.set(Calendar.DAY_OF_MONTH,mDayOfMonth);
                        selectedDate=selectedCalendar.getTimeInMillis();

                        calendarCardView.removeAllViews();
                        isCalendarOpen=false;
                    }

                }else {
//                    Toast.makeText(Detail.this,"不可编辑📅 ❌",Toast.LENGTH_SHORT).show();
                    Toast.makeText(Detail.this,"❌ Not allowed to edit",Toast.LENGTH_SHORT).show();
                }
            }
        });

        label.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(isLabelOpen==false){
//                    Toast.makeText(labelTest.this,"可编辑🍭 ✔ ",Toast.LENGTH_SHORT).show();
                    isLabelOpen=true;
                    if(labelCardViewContainer.indexOfChild(rg)==-1){

//                        rg=new MyRadioGroup(mActivityContext);
                        rg=new MyNewRadioGroup(mActivityContext);
                        rg.setFlexWrap(FlexWrap.WRAP);
                        rg.setFlexDirection(FlexDirection.ROW);
                        rg.setAlignItems(AlignItems.FLEX_START);
                        rg.setJustifyContent(JustifyContent.SPACE_BETWEEN);
//                        if(isGetLabelLibrary==false){
//                            getStoredLabels();
//                            Log.d("labelTest",">>>>>getStoredLabels");
//                            isGetLabelLibrary=true;
//                        }
                        init();
                        setHighlightButton(selectedText);
                        MyNewRadioGroup.LayoutParams rg_layoutParams=new MyNewRadioGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        rg_layoutParams.setMargins(40,40,40,40);

                        labelCardViewContainer.addView(rg,rg_layoutParams);
                    }
                }else {
                    labelCardViewContainer.removeAllViews();
                    isLabelOpen=false;
                }
            }
        });

        location_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出拍摄地点“编辑窗口”
                AlertDialog.Builder alertBuilder=new AlertDialog.Builder(mActivityContext);
                LayoutInflater inflater=mActivityContext.getLayoutInflater();
                View dialogView=inflater.inflate(R.layout.location_display,null);
                alertBuilder.setView(dialogView);
                final MyEditText CountryName=(MyEditText)dialogView.findViewById(R.id.CountryName);
                final MyEditText AdministrativeName=(MyEditText)dialogView.findViewById(R.id.AdministrativeName);
                final MyEditText FeatureName=(MyEditText)dialogView.findViewById(R.id.FeatureName);
                CountryName.setText(location_CountryName.equals(" ")?"":location_CountryName);
                if(isCountryEditable==false){
                    CountryName.setEnabled(false);
//                    CountryName.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Toast.makeText(mActivityContext,"\""+location_CountryName+"\"不可更改",Toast.LENGTH_SHORT).show();
//                        }
//                    });
                }
                AdministrativeName.setText(location_Admin.equals(" ")?"":location_Admin);
                FeatureName.setText(location_Feature.equals(" ")?"":location_Feature);

//                String test=" , , , ";
//                String[] testSplits=test.split(",");
//                String tt="testSplits:"+testSplits.length+" m";
//                for (int i=0;i<testSplits.length;i++){
////                    tt=tt.(i+1)+": A"+testSplits[i]+"B";
//                    Log.d("testSplits",(i+1)+": A"+testSplits[i]+"B");
//                }
//                Toast.makeText(mActivityContext,tt+"\nnewLocationCompleteText:\n"+newLocationCompleteText+"\ncode: "+location_CountryCode,Toast.LENGTH_SHORT).show();

                alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String country=CountryName.getText().toString();
//                        country=(countries.isCorrectCountry(country))?country:" ";
                        country=countries.getCorrectCountryOrNull(country);
//                        country=getCorrectCountry(country);
                        String admin=AdministrativeName.getText().toString();
                        admin=admin.equals("")?" ":admin;
                        String feature=FeatureName.getText().toString();
                        feature=feature.equals("")?" ":feature;

                        newLocationCompleteText=country+","+admin+","+feature;
                        updateLocationCard();

                    }
                }).setNegativeButton("Cancel", null);
                AlertDialog alertDialog=alertBuilder.create();
                alertDialog.show();
            }
        });


        //拿数据
        Intent intent=this.getIntent();
        imagePath = (String)intent.getStringExtra("ImagePath");
        SQLiteDatabase db=dbHelper.getReadableDatabase();
//        String[] projection={
//                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
//                FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION,
//                FeedReaderContract.FeedEntry.COLUMN_NAME_DAYNUMBER
//        };
        String selection=FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGEPATH +"=?";
        String[] selectionArgs={imagePath};
        Cursor cursor=db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
//        Cursor cursor=db.query(
//                FeedReaderContract.FeedEntry.TABLE_NAME,
//                projection,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                null
//        );
        //按理来说，有且只有一个结果
        //但为了安全起见，还是按照标准做法：把结果（依次）放到一个arrayList里，最后把唯一一个结果拿出来

//        int dayNumber=0;
//        int isdayNumberAlreadyCalculated=0; //1:true, 0:false
        //好像不需要，因为每次都重新生成，因为照片距离今天的时间是每次/每天都更新的，所以不会有较稳定的固定值（像标题一样，用户想啥时候改就啥时候就改，而dayNumber是有固定更新的时间的，每一天，自动更新）
        //所以，提高效率方式：如果计算的是今天，就设置一个flag,isAlreadyCalculated=true，就用新的一天已经存好的（一次）计算结果，如果还没计算过，默认值是isAlreadyCalculated=false,然后重新计算，再设置成true,就不用再算啦，一天只用算一次，不管用户打开几次，界面更新速度又更快了，效率更高了。


//        //若有多条语句，说明，这个照片对应的有多个，把情况显式告诉用户
//        int times=0;
//        if(cursor.moveToFirst()){
//            do{
//                times=times+1;
//                if(times>1){//这个照片对应的有多个
//                    title=this.getString(R.string.defaultTitle);
//                    description=this.getString(R.string.defaultDescription);
//                    label=this.getString(R.string.empty);
//                    location=this.getString(R.string.defaultLocation);
//                    dateTaken="0000:00:00";
////                    dayNumber=0;
////                    isdayNumberAlreadyCalculated=0;
//                    //把情况显式告诉用户
//                    Toast.makeText(Detail.this,"相同地址的对应照片有多张，我分不清",Toast.LENGTH_LONG).show();
//                    break;
//                }
//                title = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE));
//                description = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION));
//                dateTaken = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_DATETAKEN));
////                dayNumber = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_DAYNUMBER));
////                isdayNumberAlreadyCalculated = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_ISDAYNUMBERALREADYCALCULATED));
//                label = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL));
//                location = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION));
//            }while (cursor.moveToNext());
//        }

        if(cursor.moveToFirst()){
            do{
                title = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE));
                description = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION));
                dateTaken = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_DATETAKEN));
                if(cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_ISDATEEDITABLE))==1){
                    isDateEditable=true;
                    int[] Date=MyDeltaDate.getIntDateFromString(dateTaken);
                    mYear=Date[0];
                    mMonth=Date[1];
                    mDayOfMonth=Date[2];
                    Calendar selectedCalendar=Calendar.getInstance();
                    selectedCalendar.set(Calendar.YEAR,mYear);
                    selectedCalendar.set(Calendar.MONTH,mMonth-1);
                    selectedCalendar.set(Calendar.DAY_OF_MONTH,mDayOfMonth);
                    selectedDate=selectedCalendar.getTimeInMillis();

//                    textShow.setText("选择的日期: "+dateTaken);
                }
                labelChosen = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL));
                if(cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_ISCOUNTRYEDITABLE))==1){
                    isCountryEditable=true;//可改，无名国家，无经纬度
                }else {
                    isCountryEditable=false;//不可改，确定国家，有经纬度，isCountryEditable=false,与默认值相同
                }
                locationCompleteText = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION));
                latitudeAndLongitude = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LATITUDEANDLONGITUDE));

            }while (cursor.moveToNext());
        }
        cursor.close();

        /*
        // V0.0
        Bundle bundle=intent.getExtras();
        item = (CardItemEntity) bundle.getSerializable("CardItemEntity");
        */

        //<<<<<<<<<<<<<放（数据放到组件中）>>>>>>>>>>>>>>>

        //照片背景+模糊
        RequestOptions requestOptions1 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this).load(imagePath)
                .apply(requestOptions1)
                .apply(blur.setGaussBlur())
                .into(blur);

        //照片标题
        title_card.setText(title);

        //照片记录
        text_card.setText(description);

        //V1.0
        /*
//        ************ExifInterface*******************
        ExifInterface exifInterface=null;

        int deltaDays;//用来显示“距离今天的天数”，如果已经计算，就直接用当天计算后存储的dayNumber赋值，否则有且只有一次地计算当天的dayNumber后，把flag设成1，并赋值
        if(isdayNumberAlreadyCalculated==1){//不需要再次计算
            deltaDays=dayNumber;
        }else {//计算后，更新数据库里的dayNumber和isCalculated标签
            ////**************获取图片的拍摄日期****************
            try {
                exifInterface=new ExifInterface(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String takenTime=exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
            if(takenTime!=null){
                String curTime = new SimpleDateFormat("yyyy:MM:dd").format(Calendar.getInstance().getTime());
                deltaDays=MyApplication.calculateDaysBetweenFromDates(curTime,takenTime.substring(0,10));
            }else {
//                showText="从日历选择";
                deltaDays=0;
            }
//        item.setDayNumber_card(deltaDays);

            //更新数据库日期和“是否已计算”标签：
            ContentValues values=new ContentValues();
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DAYNUMBER,deltaDays);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ISDAYNUMBERALREADYCALCULATED,1);//已经更新，标记改为1
            String selectionUpdate = FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGEPATH +" = ?";
            String[] selectionArgsUpdate={imagePath};
            //受影响的行数
            int count = db.update(
                    FeedReaderContract.FeedEntry.TABLE_NAME,
                    values,
                    selectionUpdate,
                    selectionArgsUpdate);
        }

         */

        //照片天数
        String daysText="";
//        String curTime = new SimpleDateFormat("yyyy:MM:dd").format(Calendar.getInstance().getTime());
//        boolean isDateEdited=false;
//        if(isDateEditable==false){
//            //Enable从日历选择日期
//            isDateEdited=true;
//            //每次更改了日期(flag==1)，得到新的dateTaken后，都要重新计算并显示“距离天数”，
//            //计算：daysText=MyDeltaDate.getDaysTextFromDates(curTime,dateTaken);
//            //显示：dayNumber_card.setText(daysText);
//            //返回的时候，才保存到数据库(因为isDateEdited=1，表示用户修改了日期)
////            Toast.makeText(Detail.this,"可编辑照片拍摄日期",Toast.LENGTH_LONG).show();
//        }
        daysText=MyDeltaDate.getDaysTextFromDates(curTime,dateTaken);
        dayNumber_card.setText(daysText);

        //照片标签
        setLabel(labelChosen);
        selectedText=labelChosen;

        /*
        item.getLabel_card()
        //理想的做法
        label_card.setText("标签："+item.getLabel_card());

        //获取图片经纬度
        String latitudeValue=exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        String longitudeValue=exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);

 ***********************For test**************************
        if(item.isUserPhoto()==true){
            //拿到照片
            filePath = item.getImagePath_card();
            RequestOptions requestOptions1 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
            //模糊照片，当背景
            Glide.with(this).load(filePath)
                    .apply(requestOptions1)
                    .apply(blur.setGaussBlur())
                    .into(blur);

            ////**************获取图片的拍摄日期****************
            ExifInterface exifInterface=null;
            try {
                exifInterface=new ExifInterface(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String takenTime=exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
//            String showText="";
            int deltaDays;
            if(takenTime!=null){
                String curTime = new SimpleDateFormat("yyyy:MM:dd").format(Calendar.getInstance().getTime());
//                showText=calculateDaysBetweenFromDates(curTime,takenTime.substring(0,10));
                deltaDays=calculateDaysBetweenFromDates(curTime,takenTime.substring(0,10));
            }else {
//                showText="从日历选择";
                deltaDays=0;
            }
            item.setDayNumber_card(deltaDays);
            //更新数据库日期：
            SQLiteDatabase db=dbHelper.getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DAYNUMBER,deltaDays);
            String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_FILEPATH+" = ?";
            String[] selectionArgs={item.getImagePath_card()};
            //受影响的行数
            int count = db.update(
                    FeedReaderContract.FeedEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

//            dayNumber_card.setText(showText);
            dayNumber_card.setText(deltaDays+"天前");

            //获取图片经纬度
            String latitudeValue=exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String longitudeValue=exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            ////**********************************************
        }else {
            ////模糊照片，当背景
            Glide.with(this).load(item.getImage_card())
                    .apply(blur.setGaussBlur())//这是重点
                    .into(blur);
            //blur.setImageResource(item.getImage_card());
            //距离天数
            dayNumber_card.setText(String.valueOf(item.getDayNumber_card())+"天前");
        }

         */

        //拍摄地点
        //要么是“无地点”，要么是“一长串,,,,”
        newLocationCompleteText=locationCompleteText;
        updateLocationCard();

//        place_card.setText(location);
//        place_card.setText("🌎 "+String.valueOf(item.getPlace_card()));




        //<<<<<<<<<<<<<<<<<<<存数据>>>>>>>>>>>>>>>>>>>>>
        // 返回按钮事件,点击返回，
        // 但是，因为这是编辑信息，第一次来，就是确定了要保存这张图片，所以肯定已经放到数据库了，就不再需要返回“是否选择照片”的界面
        // 而是“看照片”的界面，所以不是“finish”，而应该是intent到PhotoPortrait.class
        // 且点击时，要自动保存/更新EditText的所有信息，图片绝对路径，图片标题和描述 and so forth
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();

                //“标签数据库”更新
                SQLiteDatabase db=dbHelper.getWritableDatabase();//局部变量（在定义返回按钮"back"的触发事件里）db覆盖上面已经声明赋值的“SQLiteDatabase db=dbHelper.getReadableDatabase();”

                if(isStoredLabelsEdited){
                    String SQL_ClearLabelLibrary="DELETE FROM "+ FeedReaderContract.FeedEntry.LABEL_TABLE_NAME;
                    db.execSQL(SQL_ClearLabelLibrary);
//                    Toast.makeText(mActivityContext,"deleted",Toast.LENGTH_SHORT).show();


                    ContentValues values=new ContentValues();
                    for (Map.Entry<String, String> entry : storedLabels.entrySet()) {
                        String text = entry.getKey();
                        String color = entry.getValue();
                        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LABELTEXT, text);
                        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LABELCOLOR, color);
                        db.insert(FeedReaderContract.FeedEntry.LABEL_TABLE_NAME,null,values);
                        values.clear();
                    }
//                    Toast.makeText(mActivityContext,"added end..",Toast.LENGTH_SHORT).show();


                }



                ContentValues values=new ContentValues();
                String changeThings="";
                if(isTitleChanged==true){
                    changeThings=changeThings+"title";
                    String changedTitle=title_card.getText().toString();//item.setTitle_card(changedTitle);
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,changedTitle);
                }
                if(isTextChanged==true){
                    changeThings=changeThings+", text";
                    String changedText=text_card.getText().toString();//item.setText_card(changedText);
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION,changedText);
                }
                boolean isDateChanged=isDateEditable&&isDateEdited;
                if(isDateChanged){
                    changeThings=changeThings+", date";
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DATETAKEN,dateTaken);
                }
                if(labelChosen.equals(selectedText)==false){
                    isLabelEdited=true;
                }
//                Log.d("labelTest: ","labelChosen: "+labelChosen+",selectedText: "+selectedText);
                if(isLabelEdited){
                    changeThings=changeThings+", label";
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL,selectedText);
                }

                if(!newLocationCompleteText.equals(locationCompleteText)){
                    islocationInfoChanged=true;
                }
                if(islocationInfoChanged){
                    changeThings=changeThings+", location";
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION,newLocationCompleteText);
                }
                if(isCountryEditable && !location_CountryName.equals(" ")) {
                    //根据“新的”国家名字，决定是否要更新经纬度信息
                    String newLatitudeAndLongitude=new Countries().getlatitudeAndLongitudeFromCountryName(location_CountryName);
                    if(latitudeAndLongitude.equals("")||!latitudeAndLongitude.equals(newLatitudeAndLongitude)){
                        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LATITUDEANDLONGITUDE, newLatitudeAndLongitude);
                    }

                }

                if(isTitleChanged || isTextChanged || isDateChanged || isLabelEdited || islocationInfoChanged){
                    //数据库更新
                    db.update(
                            FeedReaderContract.FeedEntry.TABLE_NAME,
                            values,
                            FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGEPATH +" = ?",
                            new String[]{imagePath});//new String[]{item.getImagePath_card()}

// ***********************For test->从资源库里放的样本图片**************************
//                    if(item.isUserPhoto()==true){
//                        db.update(
//                                FeedReaderContract.FeedEntry.TABLE_NAME,
//                                values,
//                                FeedReaderContract.FeedEntry.COLUMN_NAME_FILEPATH+" = ?",
//                                new String[]{item.getImagePath_card()});
//                    }else {
//                        db.update(
//                                FeedReaderContract.FeedEntry.TABLE_NAME,
//                                values,
//                                FeedReaderContract.FeedEntry.COLUMN_NAME_FILEID+" = ?",
//                                new String[]{String.valueOf(item.getImage_card())});
//                    }
                    Log.d("MainActivity","Update data: "+changeThings+"\n" +", imagePath is "+ imagePath);
                }

                Intent intentToPhotoPortrait=new Intent(Detail.this,PhotoPortrait.class);
                intentToPhotoPortrait.putExtra("ImagePath",imagePath);
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("CardItemEntity",item);//将CardItemEntity类通过Bundle传递给Detail界面
//                intentToPhotoPortrait.putExtras(bundle);
                startActivity(intentToPhotoPortrait);
//                finish();
            }
        });
    }

//    public String getCorrectCountry(String name){
////        String code=Name2Code.get(name);
////        if(!code.equals("")){
////            return name;
////        }else {
////            return "";
////        }
//
//        return (Name2Code.get(name).equals(""))? "":name;
//    }


//    public String getCountryCodeFromName(String countryName){
//        return Name2Code.get(countryName);
//    }

    public void updateLocationCard(){
        //"XX,XX,XX"-->Detail里的框，而不是弹出的东西
        //所以，而且好死不死，我发现，就算feature/admin从弹窗中取出的未“”空，再这些东西都没被Detail里放的时候被清空，所以，仍然占着位置，导致，那么最上面“无地点”那么老长...

        //咋这么复杂，我自己都懵了，写论文就一笔带过了...
        //真是像秋园里说的，如果我不记录下来，这件事就过去了，被抹掉了，但我又刚好不想记录...
        //噢！还可以传github，我一直以为这是不能传的...
        //一直以为只有vscode才可以传...一直以来，我多么无知
        String[] splits=newLocationCompleteText.split(",");
        location_CountryName=splits[0];
        location_Admin=splits[1];
        location_Feature=splits[2];

        LinearLayout.LayoutParams heightWrapContent=new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams heightZero=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0);

//        if(isCountryEditable==false){
//            location_card_CountryName.setEnabled(false);
//        }

        location_card_CountryName.setLayoutParams(location_CountryName.equals(" ")?heightZero:heightWrapContent);
        location_card_Admin.setLayoutParams(location_Admin.equals(" ")?heightZero:heightWrapContent);
        location_card_Feature.setLayoutParams(location_Feature.equals(" ")?heightZero:heightWrapContent);

        location_card_CountryName.setText(location_CountryName);
        location_card_Admin.setText(location_Admin);
        location_card_Feature.setText(location_Feature);

        if(location_CountryName.equals(" ")){
            if(location_Admin.equals(" ")&&location_Feature.equals(" ")){
                location_CountryCode=getString(R.string.defaultLocation);
            }else {
                location_CountryCode=getString(R.string.defaultCountryCodeToBeFilled);
            }
        }else {
            location_CountryCode=countries.getCode(location_CountryName);
        }
        location_card_CountryCode.setText(location_CountryCode);

        Log.d("testSplits","newLocationCompleteText:\n"+newLocationCompleteText+"\ncode: "+location_CountryCode);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void init(){
        if(rg!=null){
            //放真正数据的标签按钮
            putRealBtns();
            //放功能按钮
            rg.putFunctionalBtns();
            //设置孩子的点击事件
            setChildOnClickListener();
        }else {
            Log.d("labelTest","ERROR: null rg");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void putRealBtns() {
        for (Map.Entry<String, String> entry : storedLabels.entrySet()) {
            String text = entry.getKey();
            String color = entry.getValue();
//            myGroupAddBtn(text, color);
            rg.myGroupAddBtn(text, color,true,false);

        }
    }



    private void getStoredLabels(){

//        storedLabels.put("travel","#F892B5");
//        storedLabels.put("home","#7BD1F8");
//        storedLabels.put("visit","#B3E47A");
//        storedLabels.put("abroad","#FFEB3F");
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=db.query(
                FeedReaderContract.FeedEntry.LABEL_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        if(cursor.moveToFirst()){
            do{
                String text = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LABELTEXT));
                String color = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LABELCOLOR));
                storedLabels.put(text,color);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setHighlightButton(String labelText){
        selectedText=labelText;
        setLabel(selectedText);
        for(MyRadioButton button: rg.btnList){
            String buttonText=button.getMyTextView();
            if(buttonText.equals(labelText)){//buttonText==labelText 行不通，神奇噢【可以好好想想】
                Log.d("labelTest","setHighlightButton: "+labelText);
                button.startMyAlphaAnimation();
                return;
//                button.setMyAlpha(1);
//                selectedBtn=button;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setLabel(String label){
        Log.d("labelTest","setLabel!!!!!!!!!! "+label);
        label_text_card.setText(label);
        if(label.equals("empty")){//getString(R.string.empty)
            label_color_card.setBackgroundColor(this.getColor(R.color.empty));
        }else {
            String color=storedLabels.get(label);
            label_color_card.setBackgroundColor(Color.parseColor(color));
        }

        //理想的做法
        //label_card.setText("标签："+item.getLabel_card());
        //照片标签 item.getLabel_card()
//        switch (label){
//            case "travel":
//                label_text_card.setText("travel");//用CardItem类的label属性设置标签的文字和背景色
//                label_color_card.setBackgroundColor(this.getColor(R.color.travel));
//                break;
//            case "abroad":
//                label_text_card.setText("abroad");
//                label_color_card.setBackgroundColor(this.getColor(R.color.aboard));
//                break;
//            case "visit":
//                label_text_card.setText("visit");
//                label_color_card.setBackgroundColor(this.getColor(R.color.visit));
//                break;
//            case "home":
//                label_text_card.setText("home");
//                label_color_card.setBackgroundColor(this.getColor(R.color.home));
//                break;
//            case "pets":
//                label_text_card.setText("pets");
//                label_color_card.setBackgroundColor(this.getColor(R.color.pets));
//                break;
//            case "empty":
//                label_text_card.setText("empty");
//                label_color_card.setBackgroundColor(this.getColor(R.color.empty));
//                break;
//            default:
//                break;
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setChildOnClickListener(){
        for(final MyRadioButton button:rg.btnList){
            button.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    if(button.isRealLabel()){
                        button.startMyAlphaAnimation();
                        selectedText=button.getMyTextView();
                        setLabel(selectedText);
//                        label_text_card.setText(selectedText);
//                        label_color_card.setBackgroundColor(Color.parseColor(storedLabels.get(selectedText)));

//                        Toast.makeText(mActivityContext,"click: "+button.getMyTextView(),Toast.LENGTH_SHORT).show();
                        //其他隐身(好好琢磨)
                        for(MyRadioButton iterateBtn: rg.btnList){
                            if(iterateBtn.getMyTextView()!=selectedText&&iterateBtn.isRealLabel()==true){
                                iterateBtn.setMyDefaultRealBtnAlhpa();
                            }
                        }
                    }else {
                        //添加+新的标签
                        final String newColor=getNewColorFromCollection();
                        if(newColor.equals("")){
                            //若已经满了10个，就返回“”；否则，是有颜色的，就继续完成“添加”的进度
                            Toast.makeText(mActivityContext,"标签不允许超过30个",Toast.LENGTH_SHORT).show();
                        }else {
                            //有弹窗
                            AlertDialog.Builder alertBuilder=new AlertDialog.Builder(mActivityContext);
                            LayoutInflater inflater=mActivityContext.getLayoutInflater();
                            View dialogView=inflater.inflate(R.layout.create_custom_label,null);
                            alertBuilder.setView(dialogView);
                            final MyEditText customLabelName=(MyEditText)dialogView.findViewById(R.id.customLabelName);
                            alertBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //添加标签
//                                String newText="pets";
//                                String newColor="#6200EE";

                                    String newText=customLabelName.getText().toString();
                                    //有弹窗后，创建的三种情况，分别是一下：
                                    //没标签、创建了已经有的、成功添加崭新的
                                    if(newText.isEmpty()){
                                        //没填"标签名"，就不能创建新的标签
                                        Toast.makeText(mActivityContext,"Name cannot be empty, please enter again",Toast.LENGTH_SHORT);
                                        Log.d("labelTest","name cannot be empty, please enter again");
//                                    return;
                                    }else if(storedLabels.containsKey(newText)){
                                        Log.d("labelTest","already have one");
//                                    selectedText=newText;
                                        setHighlightButton(newText);
                                        for(MyRadioButton iterateBtn: rg.btnList){
                                            if(iterateBtn.getMyTextView()!=selectedText&&iterateBtn.isRealLabel()==true){
                                                iterateBtn.setMyDefaultRealBtnAlhpa();
                                            }
                                        }
                                    } else {
                                        //添加了新的！
                                        storedLabels.put(newText,newColor);
                                        isStoredLabelsEdited=true;
                                        rg.removeAllViews();
                                        init();
                                        setHighlightButton(newText);
                                        setLabel(selectedText);
                                    }
                                }
                            }).setNegativeButton("Cancel", null);
                            AlertDialog alertDialog=alertBuilder.create();
                            alertDialog.show();
                        }
                    }
                    Log.d("labelTest","short click："+selectedText);
                }
            });

            button.setOnLongClickListener(new View.OnLongClickListener(){
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public boolean onLongClick(View v) {

                    if(button.isFunctionalLabel()){
                        //do nothing
//                        Toast.makeText(mActivityContext,"do nothing",Toast.LENGTH_SHORT).show();
                    }else {
                        //是否删除、删除
                        final String deletedText=button.getMyTextView();
                        AlertDialog.Builder alertBuilder=new AlertDialog.Builder(mActivityContext);
//                        alertBuilder.setMessage("是否要删除： \""+deletedText+"\" ?");
                        alertBuilder.setMessage("You sure to delete label \""+deletedText+"\" ?");
                        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //删除标签
                                //看相片数据库里有没有用这个label作为标签的，有的话，就不能删掉；没有的话，才能删

                                final String deletedText = button.getMyTextView();
                                String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL + "=?";
                                String[] selectionArgs = {deletedText};
                                SQLiteDatabase db = dbHelper.getReadableDatabase();
                                Cursor cursor = db.query(
                                        FeedReaderContract.FeedEntry.TABLE_NAME,
                                        new String[]{FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGEPATH},
                                        selection, selectionArgs, null, null, null
                                );
                                if (cursor.getCount() <= 1) {//还有0的可能
                                    //==1或0
                                    if (cursor.moveToFirst()) {//就是cursor.getCount() == 1
                                        String usedImagePath = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGEPATH));
                                        if (usedImagePath.equals(imagePath)) {
                                            //可删
                                            Log.d("labelTest", "So let's delete it!");
//                                            Toast.makeText(mActivityContext, "1--So let's delete it!", Toast.LENGTH_SHORT).show();
                                            isStoredLabelsEdited = true;
                                            storedLabels.remove(deletedText);
                                            rg.removeAllViews();
                                            init();
                                            if (deletedText.equals(selectedText) ) {
                                                setHighlightButton("empty");
                                            }else {
                                                setHighlightButton(selectedText);
                                            }
                                        } else {
                                            //那个1不是这张照片，其他唯一的照片用了这个标签
                                            Toast.makeText(mActivityContext, "标签<"+deletedText+">不能删，另1张照片正在使用", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        //==0 本来想，没有被用上，可以删（因为photo数据库里没有用它的）
                                        Log.d("labelTest", "So let's delete it!");
                                        //两种情况
//                                        Toast.makeText(mActivityContext, "0--So let's delete it!", Toast.LENGTH_SHORT).show();
                                        isStoredLabelsEdited = true;
                                        storedLabels.remove(deletedText);
                                        rg.removeAllViews();
                                        init();
                                        if (deletedText.equals(selectedText) ) {
                                            setHighlightButton("empty");
                                        }else {
                                            setHighlightButton(selectedText);
                                        }
                                    }
                                } else {
                                    //2/3/4不删
                                    Toast.makeText(mActivityContext,
                                            "标签<"+deletedText+">不能删，"+cursor.getCount()+"张照片正在使用", Toast.LENGTH_SHORT).show();
                                }
                                cursor.close();
                            }
                        }).setNegativeButton("No", null);
                        final AlertDialog alertDialog=alertBuilder.create();
                        alertDialog.show();
                    }
                    Log.d("labelTest","long click："+selectedText);
                    return true;
                }
            });
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getNewColorFromCollection(){
        //颜色collection
        String[] myColorCollection = {
                getString(R.string.travelColor),
                getString(R.string.homeColor),
                getString(R.string.visitColor),
                getString(R.string.aboardColor),
                getString(R.string.petsColor),
                getString(R.string.c1),
                getString(R.string.c2),
                getString(R.string.c3),
                getString(R.string.c4),
                getString(R.string.c5),
                getString(R.string.c6),
                getString(R.string.c7),
                getString(R.string.c8),
                getString(R.string.c9),
                getString(R.string.c10),
                getString(R.string.c11),
                getString(R.string.c12),
                getString(R.string.c13),
                getString(R.string.c14),
                getString(R.string.c15),
                getString(R.string.c16),
                getString(R.string.c17),
                getString(R.string.c18),
                getString(R.string.c19),
                getString(R.string.c20),
                getString(R.string.c21),
                getString(R.string.c22),
                getString(R.string.c23),
                getString(R.string.c24),
                getString(R.string.c25)
        };
        Collection<String> usedColors=storedLabels.values();
        for(String colorInCollection:myColorCollection){
            if(usedColors.contains(colorInCollection)==false){
                return colorInCollection;
            }
        }
        //否则超过规定的10个标签数目
//        Toast.makeText(mActivityContext,"标签不允许超过10个",Toast.LENGTH_SHORT).show();
//        Toast.makeText(mActivityContext,"Only allow up to 10 tags",Toast.LENGTH_SHORT).show();
        return "";
    }

    //dayNumber_card.setText(daysText);
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void setLabel(String label){
//        //理想的做法
//        //label_card.setText("标签："+item.getLabel_card());
//        //照片标签 item.getLabel_card()
//        switch (label){
//            case "travel":
//                label_text_card.setText(this.getString(R.string.travel));//用CardItem类的label属性设置标签的文字和背景色
//                label_color_card.setBackgroundColor(this.getColor(R.color.travel));
//                break;
//            case "aboard":
//                label_text_card.setText(this.getString(R.string.aboard));
//                label_color_card.setBackgroundColor(this.getColor(R.color.aboard));
//                break;
//            case "visit":
//                label_text_card.setText(this.getString(R.string.visit));
//                label_color_card.setBackgroundColor(this.getColor(R.color.visit));
//                break;
//            case "home":
//                label_text_card.setText(this.getString(R.string.home));
//                label_color_card.setBackgroundColor(this.getColor(R.color.home));
//                break;
//            case "empty":
//                label_text_card.setText(this.getString(R.string.empty));
//                label_color_card.setBackgroundColor(this.getColor(R.color.empty));
//                break;
//            default:
//                break;
//        }
//    }



}