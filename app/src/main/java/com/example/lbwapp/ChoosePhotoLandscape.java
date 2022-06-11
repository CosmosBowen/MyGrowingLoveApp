package com.example.lbwapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class ChoosePhotoLandscape extends Activity {

    ImageView imageView;
    String filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_photo_landscape);

        imageView=findViewById(R.id.photo);
        final Intent intent=getIntent();
        filePath = (String) intent.getStringExtra("filePath");
        RequestOptions requestOptions1 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
        //将照片显示在 ivImage上
        Glide.with(this).load(filePath).apply(requestOptions1).into(imageView);
        //重新选择
        CardView cardViewChoose=findViewById(R.id.refresh);
        cardViewChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从相册中获取图片 startIntent
                //choosePhoto();
                Intent intentAgain=new Intent(ChoosePhotoLandscape.this,TakePhotos.class);
                startActivity(intentAgain);
            }
        });

        //选择完成
        CardView cardViewDone=findViewById(R.id.ok);
        cardViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到编辑界面
                Intent intentEdit=new Intent(ChoosePhotoLandscape.this,EditPhotoInfo.class);
                intentEdit.putExtra("filePath",filePath);
                startActivity(intentEdit);
            }
        });
    }
}