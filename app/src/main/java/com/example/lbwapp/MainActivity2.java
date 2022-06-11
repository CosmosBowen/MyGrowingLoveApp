package com.example.lbwapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {
private RecyclerView recyclerView;
    private List<ItemEntity> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        recyclerView=(RecyclerView) findViewById(R.id.recycler_view);
        getData();
        MyAda myAdpater=new MyAda(this,list);
        //new LinearLayoutManager() 参数为上下文环境，实现的是默认的垂直布局
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdpater);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myAdpater.setOnItemClickListener(new MyAda.OnItemClickListener(){
            @Override
            public void onItemClick(View view , int position){
                ItemEntity item=list.get(position);
                //Toast.makeText(MainActivity2.this, (CharSequence) list.get(position).getContent(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity2.this,TestDetail.class);
                Bundle bundle=new Bundle();
//                bundle.putInt("img_id",item.getImg());
//                bundle.putString("text_id",item.getContent());
                bundle.putSerializable("ItemEntity",item);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void getData() {
        list = new ArrayList<>();
        int[] imgs = new int[]{R.drawable.family1, R.drawable.family2, R.drawable.family3, R.drawable.family4, R.drawable.family5, R.drawable.family6, R.drawable.family7, R.drawable.family8, R.drawable.add_button};
        for (int i : imgs) {
            ItemEntity itemEntity = new ItemEntity();
            itemEntity.setContent("测试" + i);
            itemEntity.setImg(i);
            list.add(itemEntity);
        }
    }
}