package com.example.lbwapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.View;

import java.util.List;

import static androidx.recyclerview.widget.StaggeredGridLayoutManager.GAP_HANDLING_NONE;

public class ShowEachTagCollection extends Activity {

    RecyclerView rv_showEachTagCollection;
    List<CardItemEntity> eachTagList;
    StaggeredGridLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_each_tag_collection);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        eachTagList = (List<CardItemEntity>) bundle.getSerializable("eachTagList");

        CardView show_back=(CardView)findViewById(R.id.show_back);
        show_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ShowEachTagCollection.this,Sort.class);
                startActivity(intent);
            }
        });

        rv_showEachTagCollection=(RecyclerView)findViewById(R.id.rv_showEachTagCollection);
        layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(GAP_HANDLING_NONE);
//        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rv_showEachTagCollection.setLayoutManager(layoutManager);
        MyAdapterForEachTagCollection adpater=new MyAdapterForEachTagCollection(this,eachTagList);
        rv_showEachTagCollection.setAdapter(adpater);

//        adapter.setOnItemClickListener(new MySmallAdpater.OnItemClickListener(){
//            @Override
//            public void onItemClick(View view , int position){
//                MyApplication.STATE=4;
//                MyApplication.position=position;
//
//                CardItemEntity cardItem=eachTagList.get(position);
////                Toast.makeText(view.getContext(), "MySmallAdpater, onItemClick--name: " + cardItem.getTitle_card(), Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(ShowEachTagCollection.this,PhotoPortrait.class);
//                intent.putExtra("ImagePath",cardItem.getImagePath_card());
//                startActivity(intent);
//
//            }
//        });


    }

    @Override
    public void onResume() {
        super.onResume();
        int i=MyApplication.position;
        Log.d("ShowEachTagCollection: ","onResume, position:"+i);
//        myAdpater.notifyItemChanged(3);
        if(MyApplication.position!=0){
//            tagCollection.scrollToPosition(MyApplication.position);
            layoutManager.scrollToPositionWithOffset(MyApplication.position,600);
            MyApplication.position=0;
        }
//        Log.d("lifeCycle: ","list_position: "+list_position);
//        myAdpater.notifyDataSetChanged();
    }
}