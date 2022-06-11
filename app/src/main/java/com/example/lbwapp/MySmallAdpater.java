package com.example.lbwapp;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class MySmallAdpater extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private List<CardItemEntity> list;
    private Context context;
    private LayoutInflater inflater;

    public MySmallAdpater(Context context, List<CardItemEntity> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);

    }

    //自定义的MyViewHolder，持有每个Item的的所有界面元素
    private class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView small_card_image;
        private MyTextView small_card_title;
        private MyTextView small_card_label;

        public MyViewHolder(View itemView) {
            super(itemView);
            small_card_image=(ImageView)itemView.findViewById(R.id.small_card_image);
            small_card_title=(MyTextView)itemView.findViewById(R.id.small_card_title);
            small_card_label=(MyTextView)itemView.findViewById(R.id.small_card_label);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.small_card, null);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final MyViewHolder myHolder = (MyViewHolder) holder;

        final CardItemEntity cardItemEntity=list.get(position);
//        final int stored_position=position;

        //用户照片
        ImageView mImageView=myHolder.small_card_image;

        RequestOptions requestOptions = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(context).load(cardItemEntity.getImagePath_card()).apply(requestOptions).into(mImageView);

        myHolder.small_card_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //全局！
//                MyApplication.STATE=3;
//                MyApplication.position=stored_position;

//                Toast.makeText(view.getContext(), "name: " + cardItemEntity.getTitle_card(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context,PhotoPortrait.class);
                intent.putExtra("ImagePath",cardItemEntity.getImagePath_card());
                context.startActivity(intent);
            }
        });
        myHolder.small_card_image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "name: " + cardItemEntity.getTitle_card(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        String label=cardItemEntity.getLabel_card();
        myHolder.small_card_label.setText(label);

        String title=cardItemEntity.getTitle_card();
        if(title.equals("")){
            myHolder.small_card_title.setVisibility(View.INVISIBLE);
            myHolder.small_card_title.setClickable(false);
//            myHolder.title_card.setText("[无标题]");
        }else {
            myHolder.small_card_title.setVisibility(View.VISIBLE);
            myHolder.small_card_title.setClickable(true);
            myHolder.small_card_title.setText(title);
        }
        myHolder.small_card_title.setTag(position);
        /////////////////////////////////

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    private MySmallAdpater.OnItemClickListener mOnItemClickListener = null;

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            int tag= (int) ((TextView) v.findViewById(R.id.small_card_title)).getTag();
            mOnItemClickListener.onItemClick(v,tag);
        }
    }
    public void setOnItemClickListener(MySmallAdpater.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

}