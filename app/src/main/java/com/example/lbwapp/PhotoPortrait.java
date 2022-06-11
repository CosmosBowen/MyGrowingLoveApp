package com.example.lbwapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.Serializable;

public class PhotoPortrait extends Activity {
//    String fromActivity="";
    private MyDatabaseHelper dbHelper;
    MaskImageView blur;
    ImageView photo;
    String imagePath;//传item方式1（对象的一个属性）；传item方式2（整个对象 CardItemEntity item）;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_portrait);
        blur = findViewById(R.id.blur);
        photo =findViewById(R.id.photo);

        dbHelper=new MyDatabaseHelper(this,MyDatabaseHelper.DATABASE_NAME,null,MyDatabaseHelper.DATABASE_VERSION);

        //拿到传过来的CardItemEntity数据,在主界面点击了一张图片，到这里来放大显示
        Intent intent=this.getIntent();
//        Bundle bundle=intent.getExtras();
//        item = (CardItemEntity) bundle.getSerializable("CardItemEntity");
//        String imagePath=item.getImagePath_card();

        imagePath=(String)intent.getStringExtra("ImagePath");
//        fromActivity=(String)intent.getStringExtra("fromActivity");

        RequestOptions requestOptions1 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
        RequestOptions requestOptions2 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
        //好像是因为两段代码先后执行，所以才会先后显示，有delay
        Glide.with(this).load(imagePath)
                .apply(requestOptions1)
                .into(photo);
        Glide.with(this).load(imagePath)
                .apply(requestOptions2)
                .apply(blur.setGaussBlur())
                .into(blur);


// ***********************For test**************************
//        if(item.isUserPhoto()==true){
//            RequestOptions requestOptions1 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
//            RequestOptions requestOptions2 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
//            //将照片显示在 ivImage上
//            String imagePath=item.getImagePath_card();
//            Glide.with(this).load(imagePath)
//                    .apply(requestOptions2)
//                    .apply(blur.setGaussBlur())
//                    .into(blur);
//            Glide.with(this).load(imagePath)
//                    .apply(requestOptions1)
//                    .into(photo);
//            //error-->Glide.with(context).load(cardItemEntity.getImage_card()).error( R.drawable.add_button ).into(myHolder.image_card);
//        }else {
//            int imageId=item.getImage_card();
//            photo.setImageResource(imageId);
//            //blur.showMask();
//            Glide.with(this).load(imageId)
//                    .apply(blur.setGaussBlur())//这是重点
//                    .into(blur);
//        }

//        photo.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Toast.makeText(PhotoPortrait.this,"删除该照片",Toast.LENGTH_LONG).show();
//                //删除
//                String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGEPATH+" = ?";
//                String[] selectionArgs = {imagePath};
//                SQLiteDatabase db=dbHelper.getWritableDatabase();
//                //从数据库删除的行数
//                int deletedRows = db.delete(
//                        FeedReaderContract.FeedEntry.TABLE_NAME,
//                        selection,
//                        selectionArgs);
//                Intent intent=new Intent(PhotoPortrait.this,PhotoList.class);
//                startActivity(intent);
//                return true;
//            }
//        });

        //设置返回的按钮事件
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                //退回主界面
                Intent intent=null;
                //通过fromActivity来决定，但是，还有从Detail来的，所以用全局来判断
//                if(fromActivity.equals("PhotoList")){
//                    intent=new Intent(PhotoPortrait.this,PhotoList.class);
//                }else if(fromActivity.equals("MyFlexboxAdapter")){
//                    intent=new Intent(PhotoPortrait.this,MyFlexboxAdapter.class);
//                }
                Log.d("MyApplication"," \n"+MyApplication.STATE);
                if(MyApplication.STATE==3){
                    intent=new Intent(PhotoPortrait.this,ShowEachTagCollection.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("eachTagList", (Serializable) MyApplication.showEachTagList);
                    intent.putExtras(bundle);
                    MyApplication.STATE=0;
                }else if((MyApplication.STATE==4)) {
                    intent=new Intent(PhotoPortrait.this,Sort.class);
                }else if((MyApplication.STATE==1 || MyApplication.STATE==2)) {
                    intent=new Intent(PhotoPortrait.this,PhotoList.class);
                    MyApplication.STATE=0;
                }
//                MyApplication.STATE=0;
                startActivity(intent);
                //跳回卡片所在位置!!!!!!
            }
        });

        findViewById(R.id.detail).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PhotoPortrait.this,Detail.class);
                //传item方式1：整个对象
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("CardItemEntity",item);//将CardItemEntity类通过Bundle传递给Detail界面
//                intent.putExtras(bundle);
                //传item方式2：对象的一个属性，接到后，从数据库中找到匹配，取出这一条的全部信息
                intent.putExtra("ImagePath",imagePath);
                startActivity(intent);
            }
        });

    }

    protected void onDestroy(){
        dbHelper.close();
        super.onDestroy();
    }
}