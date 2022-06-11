package com.example.lbwapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TestDetail extends AppCompatActivity {
Button button;
    TextView test_text;
    ImageView test_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_detail);
        test_text=findViewById(R.id.test_text);
        test_image=findViewById(R.id.test_image);
        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        ItemEntity item= (ItemEntity) bundle.getSerializable("ItemEntity");
        test_text.setText(item.getContent());
        test_image.setImageResource(item.getImg());
        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}