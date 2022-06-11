package com.example.lbwapp;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationTest extends AppCompatActivity {

//    TextView latitude;
//    TextView longitude;
    TextView address;
    Button back;
//    Activity mActivity=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_test);
//
//        latitude=(TextView)findViewById(R.id.latitude);
//        longitude=(TextView)findViewById(R.id.longitude);
        address=(TextView)findViewById(R.id.address);
        back=(Button)findViewById(R.id.location_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LocationTest.this,PhotoList.class);
                startActivity(intent);
            }
        });

        //传来的imagePath
        Intent intent=this.getIntent();
        String imagePath=(String)intent.getStringExtra("ImagePath");

        //获取图片经纬度
        ExifInterface exifInterface=null;
        try {
            exifInterface=new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String locationText=getLocationText(exifInterface);
        address.setText(locationText);


        /*
        String latitudeRef=exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
        String longitudeRef=exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
        String latitudeValue=exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        String longitudeValue=exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);

        double latitudeDouble=0;
        double longitudeDouble=0;

//        String countryName="";
//        String adminAreaName="";

        if(latitudeValue!=null){
//            latitudeDouble=convertDegreesToDouble("38/1,53/1,55/1","N");
//            longitudeDouble=convertDegreesToDouble("77/1,2/1,16/1","W");

            //"39/1, 52/1, 19302062/1000000"
//            latitudeDouble=convertDegreesToDouble("39/1,52/1,19302062/1000000","N");
//            longitudeDouble=convertDegreesToDouble("116/1,28/1,55180664/1000000","E");


            latitudeDouble=convertDegreesToDouble(latitudeValue,latitudeRef);
            longitudeDouble=convertDegreesToDouble(longitudeValue,longitudeRef);

            this.latitude.setText(String.valueOf(latitudeDouble));
            this.longitude.setText(String.valueOf(longitudeDouble));


            Geocoder myLocation = new Geocoder(LocationTest.this, Locale.getDefault());
            try {
                List<Address> myList = myLocation.getFromLocation(latitudeDouble,longitudeDouble, 1);
                Address address = (Address) myList.get(0);

                StringBuilder sb = new StringBuilder();
                sb.append(address.getCountryName());
                sb.append(address.getAdminArea());
                sb.append(address.getFeatureName());

                //测试各种address.getXXX();
//                sb.append("feature=");
//                sb.append(address.getFeatureName());
//                sb.append("\nadmin=");
//                sb.append(address.getAdminArea());
//                sb.append("\nsub-admin=");
//                sb.append(address.getSubAdminArea());
//                sb.append("\nlocality=");
//                sb.append(address.getLocality());
//                sb.append("\nthoroughfare=");
//                sb.append(address.getThoroughfare());
//                sb.append("\npostalCode=");
//                sb.append(address.getPostalCode());
//                sb.append("\ncountryCode=");
//                sb.append(address.getCountryCode());
//                sb.append("\ncountryName=");
//                sb.append(address.getCountryName());
//                sb.append("\nhasLatitude=");
//                sb.append(address.hasLatitude());
//                sb.append("\nlatitude=");
//                sb.append(address.getLatitude());
//                sb.append("\nhasLongitude=");
//                sb.append(address.hasLongitude());
//                sb.append("\nlongitude=");
//                sb.append(address.getLongitude());
//                sb.append("\nphone=");
//                sb.append(address.getPhone());
//                sb.append("\nurl=");
//                sb.append(address.getUrl());
//                sb.append("\nextras=");
//                sb.append(address.getExtras());

                this.address.setText(sb.toString());




//                latitude.setText("latitudeDouble:\n"+latitudeDouble+"\nlongitudeDouble:\n"+longitudeDouble);
//                longitude.setText("countryName:\n"+countryName+"\nadminAreaName:\n"+adminAreaName);
            }catch (IOException e){
                e.printStackTrace();
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this,"未获取到经纬度",Toast.LENGTH_SHORT).show();
        }
        */


    }

    public String getLocationText(ExifInterface exifInterface){
        String latitudeRef=exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
        String longitudeRef=exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
        String latitudeValue=exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        String longitudeValue=exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);

        if(latitudeValue==null){
            return "无地点";//无经纬信息
        }

        //否则有经纬信息
        double latitudeDouble=convertDegreesToDouble(latitudeValue,latitudeRef);;
        double longitudeDouble=convertDegreesToDouble(longitudeValue,longitudeRef);

        Geocoder myLocation = new Geocoder(LocationTest.this, Locale.getDefault());
        StringBuilder sb = new StringBuilder();
        try {
            List<Address> myList = myLocation.getFromLocation(latitudeDouble,longitudeDouble, 1);
            Address address = (Address) myList.get(0);

            sb.append(address.getCountryCode());
            sb.append(",");
            sb.append(address.getCountryName());
            sb.append(",");
            sb.append(address.getAdminArea());
            sb.append(",");
            sb.append(address.getFeatureName());

            return sb.toString();

        }catch (IOException e){
            e.printStackTrace();
            return "无地点";//无法通过经纬信息获得地点信息
//            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
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