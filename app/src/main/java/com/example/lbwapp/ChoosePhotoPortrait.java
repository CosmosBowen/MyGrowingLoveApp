package com.example.lbwapp;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ChoosePhotoPortrait extends Activity {
    private MyDatabaseHelper dbHelper;
    ImageView imageView;
    String imagePath;
    boolean isCountryEditable=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_photo_portrait);

        //赋值dbHelper先！
        dbHelper=new MyDatabaseHelper(this,MyDatabaseHelper.DATABASE_NAME,null,MyDatabaseHelper.DATABASE_VERSION);
        imageView=findViewById(R.id.photo);
        final Intent intent=getIntent();
        imagePath = (String) intent.getStringExtra("imagePath");

        RequestOptions requestOptions1 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
        //将照片显示在 ivImage上
        Glide.with(this).load(imagePath).apply(requestOptions1).into(imageView);

        //重新选择
        CardView cardViewChoose=findViewById(R.id.refresh);
        cardViewChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从相册中获取图片 startIntent
                Intent intentAgain=new Intent(ChoosePhotoPortrait.this,TakePhotos.class);
                startActivity(intentAgain);
            }
        });

        //选择完成
        CardView cardViewDone=findViewById(R.id.ok);
        cardViewDone.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                //全局！
                MyApplication.STATE=1;




                //test Activity<EditPhotoInfo>
//                Intent intent=new Intent(ChoosePhotoPortrait.this,EditPhotoInfo.class);
//                intent.putExtra("filePath",filePath);
//                startActivity(intent);

                //************ExifInterface*******************
                ExifInterface exifInterface=null;
                try {
                    exifInterface=new ExifInterface(imagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //*****拍摄日期*******
                boolean isDateEditable=false;
                String takenTime=exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
                if(takenTime!=null){
                    //有初始创建时间
                    takenTime=takenTime.substring(0,10);
//                    Toast.makeText(ChoosePhotoPortrait.this,"有创建时间"+takenTime,Toast.LENGTH_LONG).show();
                }else {
                    //无初始创建时间，默认为今天，并允许用户从日历选择;
                    String curTime = new SimpleDateFormat("yyyy:MM:dd").format(Calendar.getInstance().getTime());
                    takenTime=curTime;
                    isDateEditable=true;
//                    Toast.makeText(ChoosePhotoPortrait.this,"无创建时间"+takenTime,Toast.LENGTH_LONG).show();
                }

                //*****拍摄地点*******
                String[] locationInfo=getLocationInfo(exifInterface);
                String locationText=locationInfo[0];
                String latitudeAndLongitude=locationInfo[1];
                //要么是“无地点”，要么是“一长串,,,,”
                //修改：都是“XX,XX,XX”


                //简化版，debug未成功
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGEPATH, imagePath);
                //***********
                CardItemEntity item=new CardItemEntity(imagePath);
                String title=item.getTitle_card();
                String description=item.getText_card();
                String label=item.getLabel_card();
//                String location=item.getPlace_card();
                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, title);
                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION, description);
                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DATETAKEN, takenTime);
                if(isDateEditable){
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ISDATEEDITABLE, 1);
                }else {
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ISDATEEDITABLE, 0);
                }
                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL, label);

                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION, locationText);
                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LATITUDEANDLONGITUDE, latitudeAndLongitude);
                if(isCountryEditable){
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ISCOUNTRYEDITABLE, 1);
                }else {
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ISCOUNTRYEDITABLE, 0);
                }
                //***********
                db.insert(FeedReaderContract.FeedEntry.TABLE_NAME,null,values);
                Log.d("MainActivity","Add data, imagePath is "+imagePath);

                Intent intent=new Intent(ChoosePhotoPortrait.this,Detail.class);
                intent.putExtra("ImagePath",imagePath);
                startActivity(intent);

                //******************** for test ********************
                //CardItemEntity
//                CardItemEntity item=new CardItemEntity(imagePath);
//
//                //数据库添加
//                String filePath=item.getImagePath_card();
//                String title=item.getTitle_card();
//                String description=item.getText_card();
//                int dayNumber=item.getDayNumber_card();
//                int isdayNumberAlreadyCalculated=item.getIsdayNumberAlreadyCalculated();
//                String label=item.getLabel_card();
//                String location=item.getPlace_card();
                    //v1.0
////                int fileId=item.getImage_card();
////                String isUserPhoto=String.valueOf(item.isUserPhoto());
                    //v2.0
////                int dayNumber=item.getDayNumber_card();
////                int isdayNumberAlreadyCalculated=item.getIsdayNumberAlreadyCalculated();
//
//                SQLiteDatabase db=dbHelper.getWritableDatabase();
//                ContentValues values=new ContentValues();
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGEPATH, imagePath);
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, title);
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION, description);
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DAYNUMBER, dayNumber);
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ISDAYNUMBERALREADYCALCULATED, isdayNumberAlreadyCalculated);
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL, label);
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION, location);
////                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FILEID, fileId);
////                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ISUSERPHOTO, isUserPhoto);
//                db.insert(FeedReaderContract.FeedEntry.TABLE_NAME,null,values);
////
//                Log.d("MainActivity","Add data, imagePath is "+imagePath);
//                Intent intent=new Intent(ChoosePhotoPortrait.this,Detail.class);
//                intent.putExtra("ImagePath",imagePath);
//                startActivity(intent);

                //跳转到编辑界面
//              //Intent intent=new Intent(ChoosePhotoPortrait.this,EditPhotoInfo.class);

                //Bundle方式传递对象item
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("CardItemEntity",item);//将CardItemEntity类通过Bundle传递给Detail界面
//                intent.putExtras(bundle);
//                startActivity(intent);
                //******************** for test ********************

            }
        });
    }

    protected void onDestroy(){
        dbHelper.close();
        super.onDestroy();
    }

    public String[] getLocationInfo(ExifInterface exifInterface){
        String text=" , , ";//getString(R.string.DefaultLocationText)
        String latitudeAndLongitude="";
        String latitudeRef=exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
        String longitudeRef=exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
        String latitudeValue=exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        String longitudeValue=exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
        if(latitudeValue==null||longitudeValue==null){//无经纬度
            Toast.makeText(this,"无经纬度",Toast.LENGTH_SHORT).show();
//            text=" , , ";
        }else {
            //否则,有经纬信息
            double latitudeDouble=convertDegreesToDouble(latitudeValue,latitudeRef);;
            double longitudeDouble=convertDegreesToDouble(longitudeValue,longitudeRef);
            latitudeAndLongitude=String.valueOf(latitudeDouble)+","+String.valueOf(longitudeDouble);
            Geocoder myLocation = new Geocoder(ChoosePhotoPortrait.this, Locale.getDefault());
            StringBuilder sb = new StringBuilder();
            try {
                List<Address> myList = myLocation.getFromLocation(latitudeDouble,longitudeDouble, 1);
                if(myList.size()!=0){
                    Address address = (Address) myList.get(0);
                    String CountryName=address.getCountryName();
                    if(!CountryName.equals("null")){
                        Countries countries=new Countries();
                        CountryName=countries.getCorrectCountryOrNull(CountryName);
                        isCountryEditable=false;//国家名称不可更改，同时也满足了前面的经纬度信息一定有！
                        sb.append(CountryName);
                    }else {
                        sb.append(" ");
                    }
//                    sb.append(CountryName.equals("null")?" ":CountryName);
                    sb.append(",");
                    String AdminArea=address.getAdminArea();
                    sb.append(AdminArea.equals("null")?" ":AdminArea);
                    sb.append(",");
                    String FeatureName=address.getFeatureName();
                    sb.append(FeatureName.equals("null")?" ":FeatureName);
                    text=sb.toString();
                }else {
                    Toast.makeText(this,"有经纬信息，但未获得地点信息",Toast.LENGTH_SHORT).show();
                }

            }catch (IOException e){
                e.printStackTrace();
                Toast.makeText(this,"无法通过经纬信息获得地点信息",Toast.LENGTH_SHORT).show();
//            text=" , , ";//无法通过经纬信息获得地点信息
            }
        }

        return new String[]{text,latitudeAndLongitude};
    }

    private String modifiedLocationText(String locationText){
        String[] textSplits=locationText.split(",");
        String location=textSplits[0];
        for(int i=1;i<textSplits.length;i++){
            location+="\n"+textSplits[i];
        }
        return location;

    }
    private double convertDegreesToDouble(String degreeString, String ref){
        //"39/1, 52/1, 19302062/1000000"
        String[] degreeList=degreeString.split(",");//{"39/1", "52/1", "19302062/1000000"}
        double result=0;
        for(int i=1;i<4;i++){
            //i=1: "39/1"->{"39", "1"}
            String[] degreeSplits=degreeList[i-1].split("/");
            //"39"
            double degreeNumerator=Double.parseDouble(degreeSplits[0]);
            //"1"
            double degreeDenominator=Double.parseDouble(degreeSplits[1]);
            //1 或 60 或 3600
            double multiplyFactor=1;
            if(i==2){
                multiplyFactor=60;
            }else if(i==3){
                multiplyFactor=3600;
            }
            result=result+(degreeNumerator/degreeDenominator)/multiplyFactor;

        }

        //ref: N/E:+
        //ref: S/W:-
        if(ref.equals("S")||ref.equals("W")){
            result=-result;
        }
        return result;

    }
}