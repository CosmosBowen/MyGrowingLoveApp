package com.example.lbwapp;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.JustifyContent;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlexBoxTest extends Activity {

    private RecyclerView recyclerView;
    private Activity mActivityContext=this;
    boolean isOpen=false;
//    private TextView eachTagName;
//    private CardView eachTagGroup;

//    RelativeLayout tagCollection;
    LinearLayout tagCollection;
//    private RecyclerView recyclerView;
    List<CardItemEntity> cardList;
    private MyDatabaseHelper dbHelper;

    String tag="label";
//    String tagName="empty";
    List<String> tagList;
    Map<String,List<CardItemEntity>> tagGroup=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sort_background);

        Log.d("flexbox_lifeCycle: ","onCreate");
//        tagCollection=(RelativeLayout)findViewById(R.id.tagCollection);
        tagCollection=(LinearLayout) findViewById(R.id.tagCollection);

        //********************创建数据库********************
        dbHelper=new MyDatabaseHelper(this,MyDatabaseHelper.DATABASE_NAME,null,MyDatabaseHelper.DATABASE_VERSION);
        dbHelper.getWritableDatabase();

//        recyclerView=(RecyclerView) findViewById(R.id.recycler_view_flexbox);
        getTagList(tag);
//        for (String tag:tagList){
//            Log.d("groupByTest","each tag: "+tag);
//        }
        getTagGroup(tag);

//        List<String> list=new ArrayList<>();
//        list.add("empty");
//        list.add("food");

        for(String name: tagList){//list测试用
            final String tagName=name;
            //-------------------TextView---------------------
//            eachTagName=new TextView(mActivityContext);
            final TextView eachTagName=new TextView(mActivityContext);
            eachTagName.setText(tagName);
            eachTagName.setTextSize(30);
            eachTagName.setBackgroundColor(Color.parseColor("#3359EDED"));
            ViewGroup.LayoutParams tv_layoutParams=new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            eachTagName.setLayoutParams(tv_layoutParams);
            //margin(top:80dp)
            //width/height(match,wrap)

            //-------------------CardView---------------------
//            eachTagGroup=new CardView(mActivityContext);
            final CardView eachTagGroup=new CardView(mActivityContext);
            eachTagGroup.setCardBackgroundColor(Color.parseColor("#ffffff"));
            eachTagGroup.setRadius(20);
            eachTagGroup.setCardElevation(0);
//            CardView.LayoutParams cv_layoutParams=new CardView.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//            );
            CardView.LayoutParams cv_layoutParams=new CardView.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            cv_layoutParams.setMargins(0,50,0,200);//l,t,r,b
            eachTagGroup.setLayoutParams(cv_layoutParams);

            tagCollection.addView(eachTagName);
            tagCollection.addView(eachTagGroup);


            //-------------------set click textView---------------------
            eachTagName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isOpen ==false){
//                    Toast.makeText(labelTest.this,"可编辑🍭 ✔ ",Toast.LENGTH_SHORT).show();
                        isOpen =true;
                        if(eachTagGroup.indexOfChild(recyclerView)==-1){

                            recyclerView=new RecyclerView(mActivityContext);

                            StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(layoutManager);

                            MyFlexboxAdapter myAdpater=new MyFlexboxAdapter(mActivityContext, tagGroup.get(tagName));
                            recyclerView.setAdapter(myAdpater);

                            RecyclerView.LayoutParams rv_layoutParams=new RecyclerView.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            );
//                        rv_layoutParams.setMargins(40,40,40,40);

                            eachTagGroup.addView(recyclerView,rv_layoutParams);
                        }
                    }else {
                        eachTagGroup.removeAllViews();
                        isOpen =false;
                    }
                }
            });
        }





        //margin(top:5dp,bottom:80dp)
        //width/heighht(wrap,wrap)


        //设置layoutManager
//        getCardData();

//        //StaggeredGridLayoutManager
//        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);

        //LinearLayoutManager(this)
//        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);


        //FlexboxLayoutManager
//        FlexboxLayoutManager layoutManager=new FlexboxLayoutManager(this);
//        layoutManager.setFlexWrap(FlexWrap.WRAP);
//        layoutManager.setFlexDirection(FlexDirection.ROW);
//
//        layoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);
//        layoutManager.setAlignItems(AlignItems.FLEX_START);
////        layoutManager.setAlignContent(AlignContent.FLEX_START);
//        recyclerView.setLayoutManager(layoutManager);


        //设置adapter
//        MyFlexboxAdapter myAdpater=new MyFlexboxAdapter(this, cardList);
//        recyclerView.setAdapter(myAdpater);
//
//        if(MyApplication.position!=0){
////            recyclerView.scrollToPosition(MyApplication.position);
//            layoutManager.scrollToPositionWithOffset(MyApplication.position,600);
//
//            MyApplication.position=0;
//        }

    }

    private void getCardData(){
        int i=1;
        cardList = new ArrayList<>();
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String groupBy=FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL;
        String[] columns={
                FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGEPATH};
//        String selection=FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL +"=?";
//        String[] selectionArgs={"empty"};
        Cursor cursor=db.query(FeedReaderContract.FeedEntry.TABLE_NAME, new String[]{"label"},null,null,"label",null,null);
        //新加的置顶，所以应该倒过来，一直网上叠加，就像instagram或者微信朋友圈，最新的一定放在最上面
        Log.d("groupByTest","cursor.getCount: "+cursor.getCount());
        if(cursor.moveToFirst()){//moveToFirst
            do{
//                String imagePath = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGEPATH));
//                String title = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE));
                String label = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL));
//                cardList.add(new CardItemEntity(imagePath,title,label));
//                Log.d("groupByTest","i: "+i);
                Log.d("groupByTest","label: "+label);
//                Log.d("groupByTest","title: "+title);
//                Log.d("groupByTest","imagePath: "+imagePath);
                i++;
            }while (cursor.moveToNext());//moveToNext
        }
        cursor.close();
    }

    private void getTagList(String tag){
        int i=1;
        tagList = new ArrayList<>();
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=db.query(FeedReaderContract.FeedEntry.TABLE_NAME, new String[]{tag},
                null,null,tag,null,null);
        Log.d("groupByTest","cursor.getCount: "+cursor.getCount());
        if(cursor.moveToFirst()){//moveToFirst
            do{
                String eachTag = cursor.getString(cursor.getColumnIndex(tag));
                tagList.add(eachTag);
                Log.d("groupByTest",i+": "+eachTag);
                i++;
            }while (cursor.moveToNext());//moveToNext
        }
        cursor.close();
    }

    private void getTagGroup(String tag){
        int i=1;
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String[] columns={ FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGEPATH};
        for(String eachTag:tagList){
            //eachTag:'empty'
            int j=1;
            List<CardItemEntity> eachTagGroupList=new ArrayList<>();
            String selection=tag +"=?";//empty
            String[] selectionArgs={eachTag};
            //cursor: all 'empty' photo
            Cursor cursor=db.query(FeedReaderContract.FeedEntry.TABLE_NAME, columns,selection,selectionArgs,null,null,null);
            Log.d("groupByTest","--------"+i+" "+eachTag+", count:"+cursor.getCount());
            if(cursor.moveToFirst()){//moveToFirst
                do{
                    //every 'empty' photo
                    String imagePath = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGEPATH));
                    String title = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE));
                    eachTagGroupList.add(new CardItemEntity(imagePath,title));
                    Log.d("groupByTest",j+": "+title);
                    j++;
                }while (cursor.moveToNext());//moveToNext
            }
            //'empty' group collected done
            tagGroup.put(eachTag,eachTagGroupList);
            i++;
            cursor.close();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("flexbox_lifeCycle: ","onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("flexbox_lifeCycle: ","onResume");
//        myAdpater.notifyItemChanged(3);
//        if(MyApplication.position!=0){
//            recyclerView.scrollToPosition(MyApplication.position);
//            MyApplication.position=0;
//        }
//        Log.d("lifeCycle: ","list_position: "+list_position);
//        myAdpater.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("flexbox_lifeCycle: ","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("flexbox_lifeCycle: ","onStop");
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
        Log.d("flexbox_lifeCycle: ","onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("flexbox_lifeCycle: ","onRestart");
    }
}