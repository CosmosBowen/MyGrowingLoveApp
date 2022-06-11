package com.example.lbwapp;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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


public class MyAdpaterForSort extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private List<InfoForEachGroup> list;
//    private List<EachTagCollection> list;
//    MySmallAdpater myAdpater;

    public MyViewHolder myViewHolder;

    private Context context;
    private LayoutInflater inflater;
//    RecyclerView recyclerView;

    public MyAdpaterForSort(Context context, List<InfoForEachGroup> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);

    }

    //自定义的MyViewHolder，持有每个Item的的所有界面元素
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private MyTextView eachTagName;
        private MyTextView eachTagCount;
//        private CardView eachTagGroup;
        private ImageView eachTagFirstImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            eachTagName = (MyTextView) itemView.findViewById(R.id.eachTagName);
            eachTagCount = (MyTextView) itemView.findViewById(R.id.eachTagCount);
//            eachTagGroup = (CardView) itemView.findViewById(R.id.eachTagGroup);
            eachTagFirstImage=(ImageView)itemView.findViewById(R.id.eachTagFirstImage);
        }
//        public CardView getEachTagGroup() {
//            return eachTagGroup;
//        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.each_tag_collection, null);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

//        final boolean[] isOpen = {false};

        final MyViewHolder myHolder = (MyViewHolder) holder;
//        final EachTagCollection eachTagCollection = list.get(position);
        final InfoForEachGroup infoForEachGroup = list.get(position);

        myHolder.eachTagName.setTag(position);
        myHolder.eachTagName.setText(infoForEachGroup.getEachTagName());
        myHolder.eachTagCount.setText(String.valueOf(infoForEachGroup.getEachTagCount())+"张");

        RequestOptions requestOptions = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
        String imagePath=infoForEachGroup.getEachTagFirstImagePath();
        //不用缩略图
        /*
        Glide.with(context)
                .load(imagePath)
                .apply(requestOptions)
                .into(myHolder.eachTagFirstImage);

         */
        ExifInterface exif = null;
        int width=0;
        int height=0;
        boolean isDealedWithThumbnail=true;
        try {
            exif = new ExifInterface(imagePath);
            width=exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH,0);
            height=exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH,0);
            if(width>height){
                isDealedWithThumbnail=false;
            }

        } catch (IOException e) {
            Log.d("thumbnail","不能获取缩略图");
            isDealedWithThumbnail=false;
//            e.printStackTrace();
        }
        String text="";
        byte[] imageData=exif.getThumbnail();
        if(imageData!=null&&isDealedWithThumbnail==true){
            text=":Yes 缩略图！"+"\n竖";

            int small_width = (int)context.getResources().getDimension(R.dimen.small_width);
            FrameLayout.LayoutParams widthWrap = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    small_width);
            myHolder.eachTagFirstImage.setLayoutParams(widthWrap);
            myHolder.eachTagFirstImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Bitmap  thumbnail = BitmapFactory.decodeByteArray(imageData,0,imageData.length);
            Glide.with(context)
                    .load(thumbnail)
                    .into(myHolder.eachTagFirstImage);
        }else {
            text=":No 缩略图！\n";
            if(width>=height){
                text+="横！";
//                myHolder.eachTagFirstImage.setScaleType(ImageView.ScaleType.CENTER);
            }else {
                text+="竖！";
            }
            Glide.with(context)
                    .load(imagePath)
                    .apply(requestOptions)
                    .into(myHolder.eachTagFirstImage);
            Log.d("thumbnail",infoForEachGroup.getEachTagName()+text);
//            Log.d("thumbnail",name+text);
        }
//        myHolder.eachTagCount.setText(text);


        /*
        myHolder.eachTagName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                RecyclerView recyclerView=null;
                if (isOpen[0] == false) {
                    isOpen[0] = true;
                    if (myHolder.eachTagGroup.indexOfChild(recyclerView) == -1) {

                        recyclerView = new RecyclerView(context);

                        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
                                1, StaggeredGridLayoutManager.VERTICAL);
                        if(MyApplication.position!=0){
                            layoutManager.scrollToPositionWithOffset(MyApplication.position,600);
                            MyApplication.position=0;
                        }
                        recyclerView.setLayoutManager(layoutManager);


//                        MyFlexboxAdapter myAdpater = new MyFlexboxAdapter(context, eachTagCollection.getEachTagList());
                        final List<CardItemEntity> eachTagList=eachTagCollection.getEachTagList();
                        myAdpater = new MySmallAdpater(context, eachTagList);
                        recyclerView.setAdapter(myAdpater);
                        myAdpater.setOnItemClickListener(new MySmallAdpater.OnItemClickListener(){
                            @Override
                            public void onItemClick(View view , int position){
                                MyApplication.STATE=3;
                                MyApplication.position=position;

                                CardItemEntity cardItem=eachTagList.get(position);
                                Toast.makeText(view.getContext(), "MySmallAdpater, onItemClick--name: " + cardItem.getTitle_card(), Toast.LENGTH_SHORT).show();


//                                Intent intent=new Intent(context,PhotoPortrait.class);
//                                intent.putExtra("ImagePath",cardItem.getImagePath_card());
//                                context.startActivity(intent);

                            }
                        });

                        RecyclerView.LayoutParams rv_layoutParams = new RecyclerView.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                        myHolder.eachTagGroup.addView(recyclerView, rv_layoutParams);
                    }
                } else {
                    myHolder.eachTagGroup.removeAllViews();
                    isOpen[0] = false;
                }
                return true;
            }
        });
        */

        /*
        myHolder.eachTagName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView recyclerView=null;
                if (isOpen[0] == false) {
                    isOpen[0] = true;
                    if (myHolder.eachTagGroup.indexOfChild(recyclerView) == -1) {
                        recyclerView = new RecyclerView(context);
                        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(layoutManager);
                        final List<CardItemEntity> eachTagList=eachTagCollection.getEachTagList();
                        myAdpater = new MySmallAdpater(context, eachTagList);
                        recyclerView.setAdapter(myAdpater);
                        myAdpater.setOnItemClickListener(new MySmallAdpater.OnItemClickListener(){
                            @Override
                            public void onItemClick(View view , int position){
                                CardItemEntity cardItem=eachTagList.get(position);
                                Intent intent=new Intent(context,PhotoPortrait.class);
                                intent.putExtra("ImagePath",cardItem.getImagePath_card());
                                context.startActivity(intent);
                            }
                        });
                        RecyclerView.LayoutParams rv_layoutParams = new RecyclerView.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        myHolder.eachTagGroup.addView(recyclerView, rv_layoutParams);
                    }
                } else {
                    myHolder.eachTagGroup.removeAllViews();
                    isOpen[0] = false;
                }
            }
        });
        */

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    private MyAdpaterForSort.OnItemClickListener mOnItemClickListener = null;
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            TextView tv=(TextView) v.findViewById(R.id.eachTagName);
            int tag= (int) tv.getTag();
            mOnItemClickListener.onItemClick(v,tag);
        }
    }
    public void setOnItemClickListener(MyAdpaterForSort.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }


}