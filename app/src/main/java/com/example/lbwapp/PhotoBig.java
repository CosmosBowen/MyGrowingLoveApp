package com.example.lbwapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoBig extends AppCompatActivity {
    //屏幕宽高
    int width;
    int height;
    Button button;
    TextView test_imgInfo;
    TextView test_text;
    ImageView test_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_big);
        test_text=findViewById(R.id.test_text);
        test_image=findViewById(R.id.test_image);
        test_imgInfo=findViewById(R.id.test_imgInfo);//图片横竖情况
        //获取传递过来的CardItemEntity类对象
        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        CardItemEntity item= (CardItemEntity) bundle.getSerializable("CardItemEntity");
        test_text.setText(item.getTitle_card());
//        test_image.setImageResource(item.getImage_card());
        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //判断照片是竖的还是横的（通过比较长宽）
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(getResources(),item.getImage_card() , opts);
        int width=opts.outWidth;
        int height=opts.outHeight;
        String imgInfo = "";
        if(width>=height){
            imgInfo+="横: ";
        }else {
            imgInfo+="竖: ";
        }
        test_imgInfo.setText(imgInfo+=("宽"+String.valueOf(width)+"，高"+String.valueOf(height)));

        //通过WindowManager获取屏幕的宽高
        //context的方法，获取windowManager
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        //获取屏幕对象
        Display defaultDisplay = windowManager.getDefaultDisplay();
        //获取屏幕的宽、高，单位是像素
        width = defaultDisplay.getWidth();
        height = defaultDisplay.getHeight();
    }
}