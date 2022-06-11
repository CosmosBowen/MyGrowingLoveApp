package com.example.lbwapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class LibraryPhoto extends AppCompatActivity {
    public static final int RC_CHOOSE_PHOTO = 2;//相册
    ImageView imageView;
    String filePath;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_photo);

        txt=findViewById(R.id.txt);
        imageView=findViewById(R.id.imageView);
        final Intent intent=getIntent();
        filePath = (String) intent.getStringExtra("filePath");
        RequestOptions requestOptions1 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
        //将照片显示在 ivImage上
        Glide.with(this).load(filePath).apply(requestOptions1).into(imageView);
        txt.setText("图片路径："+filePath);
        //重新选择
        Button buttonChoose=findViewById(R.id.btn_chooseAgain);
        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从相册中获取图片 startIntent
                //choosePhoto();
                Intent intentAgain=new Intent(LibraryPhoto.this,TakePhotos.class);
                startActivity(intentAgain);
            }
        });

        //选择完成
        Button buttonDone=findViewById(R.id.btn_chooseDone);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到编辑界面
                Intent intentEdit=new Intent(LibraryPhoto.this,EditPhotoInfo.class);
                intentEdit.putExtra("filePath",filePath);
                startActivity(intentEdit);
            }
        });
    }

//    //返回的处理resultIntent
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case RC_CHOOSE_PHOTO:
//                Uri uri = data.getData();
//                filePath = FileUtil.getFilePathByUri(this, uri);
//
//                if (!TextUtils.isEmpty(filePath)) {
//                    RequestOptions requestOptions1 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
//                    //将照片显示在 ivImage上
//                    Glide.with(this).load(filePath).apply(requestOptions1).into(imageView);
//                    txt.setText("图片路径："+filePath);
//                }else{
//                    Toast.makeText(LibraryPhoto.this,"未成功获取图片",Toast.LENGTH_SHORT);
//                }
//                break;
//        }
//    }

//    private void choosePhoto() {//相册
//
//        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
//        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO);
//
//    }
}