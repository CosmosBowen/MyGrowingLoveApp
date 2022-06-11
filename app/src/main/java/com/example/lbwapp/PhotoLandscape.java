package com.example.lbwapp;

        import androidx.appcompat.app.AppCompatActivity;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.Display;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.ImageView;

        import com.bumptech.glide.Glide;
        import com.bumptech.glide.load.engine.DiskCacheStrategy;
        import com.bumptech.glide.request.RequestOptions;

public class PhotoLandscape extends Activity {
    MaskImageView blur;
    ImageView photo;
    String imagePath;
//    CardItemEntity item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_landscape);
        blur = findViewById(R.id.blur);
        photo=findViewById(R.id.photo);

        //拿到传过来的CardItemEntity数据
        Intent intent=this.getIntent();
//        Bundle bundle=intent.getExtras();
//        item= (CardItemEntity) bundle.getSerializable("CardItemEntity");
//        int imageId=item.getImage_card();
        imagePath=(String)intent.getStringExtra("ImagePath");
        RequestOptions requestOptions1 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this).load(imagePath)
                .apply(requestOptions1)
                .into(photo);
        RequestOptions requestOptions2 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this).load(imagePath)
                .apply(requestOptions2)
                .apply(blur.setGaussBlur())
                .into(blur);

//        photo.setImageResource(imageId);
//        blur.setImageResource(imageId);
//        //blur.showMask();
//        Glide.with(this).load(item.getImage_card())
//                .apply(blur.setGaussBlur())//这是重点
//                .into(blur);

        //设置返回的按钮事件
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PhotoLandscape.this,Detail.class);
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("CardItemEntity",item);//将CardItemEntity类通过Bundle传递给Detail界面
//                intent.putExtras(bundle);
//                startActivity(intent);
                intent.putExtra("ImagePath",imagePath);
                startActivity(intent);
            }
        });
    }
}