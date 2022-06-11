package com.example.lbwapp;

        import androidx.appcompat.app.AppCompatActivity;

        import android.app.Activity;
        import android.os.Bundle;
        import android.view.Display;
        import android.view.WindowManager;
        import android.widget.ImageView;

        import com.bumptech.glide.Glide;

public class PhotoLandscape2 extends Activity {
    MaskImageView blur;
    ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_landscape2);

        blur = findViewById(R.id.blur);
        photo=findViewById(R.id.photo);
        //blur.showMask();
        Glide.with(this).load(R.drawable.family5)
                .apply(blur.setGaussBlur())//这是重点
                .into(blur);

    }
}