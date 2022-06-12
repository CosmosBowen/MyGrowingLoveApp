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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private List<CardItemEntity> list;
    private Context context;
    private LayoutInflater inflater;
    private MyDatabaseHelper dbHelper;
    public Map<String,String> storedLabels=new LinkedHashMap<>();//相当于从数据库里面拿数据

    public MyAdapter(Context context, List<CardItemEntity> list) {
        this.context = context;
        this.list = list;
        //加载布局管理
        //从一个Context中，获得一个布局填充器，这样你就可以使用这个填充器来把xml布局文件转为View对象了
        inflater = LayoutInflater.from(context);

        dbHelper=new MyDatabaseHelper(context,MyDatabaseHelper.DATABASE_NAME,null,MyDatabaseHelper.DATABASE_VERSION);
        getStoredLabels();
    }

    private void getStoredLabels(){

//        storedLabels.put("travel","#F892B5");
//        storedLabels.put("home","#7BD1F8");
//        storedLabels.put("visit","#B3E47A");
//        storedLabels.put("abroad","#FFEB3F");
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=db.query(
                FeedReaderContract.FeedEntry.LABEL_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        if(cursor.moveToFirst()){
            do{
                String text = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LABELTEXT));
                String color = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LABELCOLOR));
                storedLabels.put(text,color);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_item, null);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int myPosition=position;
        MyViewHolder myHolder = (MyViewHolder) holder;
        final CardItemEntity cardItemEntity = list.get(position);

//        if(position==0){
//            ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) myHolder.cardView.getLayoutParams();
//            cardViewMarginParams.setMargins(20, 500, 20, 20);
////            myHolder.cardView.requestLayout();  //Dont forget this line
//        }

        //******照片*******
        RequestOptions requestOptions = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
        //将照片显示在 ivImage上
        Glide.with(context).load(cardItemEntity.getImagePath_card()).apply(requestOptions).into(myHolder.image_card);
        //Glide.with(context).load(cardItemEntity.getImage_card()).error( R.drawable.add_button ).into(myHolder.image_card);

        //***********************For test***********************
//        if(cardItemEntity.isUserPhoto()==true){
//            RequestOptions requestOptions1 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
//            //将照片显示在 ivImage上
//            Glide.with(context).load(cardItemEntity.getImagePath_card())
//                    .apply(requestOptions1)
//                    .into(myHolder.image_card);
//            //Glide.with(context).load(cardItemEntity.getImage_card()).error( R.drawable.add_button ).into(myHolder.image_card);
//        }else {
//            myHolder.image_card.setImageResource(cardItemEntity.getImage_card());
//        }
//
        //************获取照片拍摄时间，并计算和显示“距离天数”*******************

        //******描述和标题*******
        String title=cardItemEntity.getTitle_card();
        if(title.equals("")){
            myHolder.title_card.setText(context.getString(R.string.card_NoTitle_Eng));
        }else {
            myHolder.title_card.setText(title);
        }
//        myHolder.title_card.setText(cardItemEntity.getTitle_card());
        String text=cardItemEntity.getText_card();
        if(text.equals("")){
            myHolder.text_card.setText(context.getString(R.string.card_NoInfo_Eng));
        }else {
            myHolder.text_card.setText(text);
        }
//        myHolder.text_card.setText(cardItemEntity.getText_card());

        //******天数*******
        String takenTime=cardItemEntity.getDateTaken_card();
        String curTime = new SimpleDateFormat("yyyy:MM:dd").format(Calendar.getInstance().getTime());
        String daysText=MyDeltaDate.getDaysTextFromDates(curTime,takenTime);
        myHolder.dayNumber_card.setText(daysText);

        //******标签*******
        String label=cardItemEntity.getLabel_card();
        myHolder.label_text_card.setText(label);
        if(label.equals("empty")){//getString(R.string.empty)
            myHolder.label_color_card.setBackgroundColor(context.getColor(R.color.empty));
        }else {
            String color=storedLabels.get(label);
            myHolder.label_color_card.setBackgroundColor(Color.parseColor(color));
        }
//        switch (cardItemEntity.getLabel_card()){
//            case "travel":
//                myHolder.label_text_card.setText(context.getString(R.string.travel));//用CardItem类的label属性设置标签的文字和背景色
//                myHolder.label_color_card.setBackgroundColor(context.getColor(R.color.travel));
//                break;
//            case "aboard":
//                myHolder.label_text_card.setText(context.getString(R.string.aboard));
//                myHolder.label_color_card.setBackgroundColor(context.getColor(R.color.aboard));
//                break;
//            case "visit":
//                myHolder.label_text_card.setText(context.getString(R.string.visit));
//                myHolder.label_color_card.setBackgroundColor(context.getColor(R.color.visit));
//                break;
//            case "home":
//                myHolder.label_text_card.setText(context.getString(R.string.home));
//                myHolder.label_color_card.setBackgroundColor(context.getColor(R.color.home));
//                break;
//            case "empty":
//                myHolder.label_text_card.setText(context.getString(R.string.empty));
//                myHolder.label_color_card.setBackgroundColor(context.getColor(R.color.empty));
//                break;
//            default:
//                break;
//        }
        //将position保存在itemView的Tag中，以便点击时进行获取
        myHolder.title_card.setTag(position);

        myHolder.delete_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder=new AlertDialog.Builder(context);
                String msg= context.getString(R.string.deletePopup_Eng);
                if(!cardItemEntity.getTitle_card().equals("")){
                    msg+="\n\n" +"🖼️ "+String.valueOf(cardItemEntity.getTitle_card());
                }else {
                    msg+=" 🥰";
                }
                alertBuilder.setMessage(msg);
//                alertBuilder.setMessage("Are you sure to DELETE this photo ?");
                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除卡片
                        //全局！
//                         MyApplication.STATE=4;
                        //删除后回到原来的位置，用被删除的照片下面的照片来替代，显示照片已删除
                         MyApplication.position=myPosition;

                        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGEPATH+" = ?";
                        String[] selectionArgs = {cardItemEntity.getImagePath_card()};
                        SQLiteDatabase db=dbHelper.getWritableDatabase();//！！！！！因为没有onDestroy,所以不能：dbHelper.close();
                        //从数据库删除的行数
                        int deletedRows = db.delete(
                                FeedReaderContract.FeedEntry.TABLE_NAME,
                                selection,
                                selectionArgs);
                        Intent intent=new Intent(context,PhotoList.class);
                        context.startActivity(intent);
                    }
                }).setNegativeButton("No", null);
                final AlertDialog alertDialog=alertBuilder.create();
                alertDialog.show();
            }
        });

        /*
        myHolder.image_card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                Toast.makeText(context,"删除"+String.valueOf(cardItemEntity.getTitle_card()),Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertBuilder=new AlertDialog.Builder(context);
                alertBuilder.setMessage("Are you sure to DELETE this photo ?\n\n"+"\""+String.valueOf(cardItemEntity.getTitle_card())+"\"");
//                alertBuilder.setMessage("Are you sure to DELETE this photo ?");
                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除卡片
                        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGEPATH+" = ?";
                        String[] selectionArgs = {cardItemEntity.getImagePath_card()};
                        SQLiteDatabase db=dbHelper.getWritableDatabase();//！！！！！因为没有onDestroy,所以不能：dbHelper.close();
                        //从数据库删除的行数
                        int deletedRows = db.delete(
                                FeedReaderContract.FeedEntry.TABLE_NAME,
                                selection,
                                selectionArgs);
                        Intent intent=new Intent(context,PhotoList.class);
                        context.startActivity(intent);
                    }
                }).setNegativeButton("No", null);
                final AlertDialog alertDialog=alertBuilder.create();
                alertDialog.show();

                return true;
            }
        });
        */

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    private MyAdapter.OnItemClickListener mOnItemClickListener = null;
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            MyTextView tv=v.findViewById(R.id.title_card);
            int tag= (int) ((TextView)tv).getTag();
            mOnItemClickListener.onItemClick(v,tag);
        }
    }
    public void setOnItemClickListener(MyAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }
    //自定义的MyViewHolder，持有每个Item的的所有界面元素
    private class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView image_card;
        private MyTextView title_card;
        private MyTextView dayNumber_card;
        private MyTextView text_card;
        private ImageView label_color_card;
        private MyTextView label_text_card;
        private ImageView delete_card;

        private CardView cardView;
        private LinearLayout card_linearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            image_card = (ImageView) itemView.findViewById(R.id.image_card);
            title_card = (MyTextView) itemView.findViewById(R.id.title_card);
            dayNumber_card = (MyTextView) itemView.findViewById(R.id.dayNumber_card);
            text_card = (MyTextView) itemView.findViewById(R.id.text_card);
            label_color_card = (ImageView) itemView.findViewById(R.id.label_color_card);
            label_text_card = (MyTextView) itemView.findViewById(R.id.label_text_card);
            delete_card=(ImageView)itemView.findViewById(R.id.delete_card);

            cardView=(CardView) itemView.findViewById(R.id.cardView);
            card_linearLayout=(LinearLayout) itemView.findViewById(R.id.card_linearLayout);
        }
    }

}