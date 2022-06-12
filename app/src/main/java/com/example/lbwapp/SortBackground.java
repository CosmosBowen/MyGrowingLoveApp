package com.example.lbwapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SortBackground extends Fragment {

    //---------------------------------------------------
//    private RecyclerView recyclerView;
    MyAdpaterForSort myAdpater;

//    boolean isOpen=false;
//    private TextView eachTagName;
//    private CardView eachTagGroup;

    //    RelativeLayout tagCollection;
    RecyclerView tagCollection;
//    LinearLayout tagCollection;
    //    private RecyclerView recyclerView;
//    List<CardItemEntity> cardList;
    private MyDatabaseHelper dbHelper;

    String tag="label";
    //    String tagName="empty";
    List<String> tagList;
//    Map<String,List<CardItemEntity>> tagGroup=new HashMap<>();
    LinkedHashMap<String,List<CardItemEntity>> eachTagCollectionList;
//    List<EachTagCollection> eachTagCollectionList;
    List<InfoForEachGroup> InfoForEachGroupList;
    FlexboxLayoutManager  layoutManager;
//    StaggeredGridLayoutManager  layoutManager;
//    LinearLayoutManager layoutManager;
    //--------------------------------------------------------



    private Activity mActivityContext;
//    private Activity mActivityContext=(Sort)getActivity();
//    private Activity mActivityContext=getActivity();
//    private TextView textView;
//    private String sortTag="";



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mActivityContext = getActivity();

//        textView=(TextView) mActivityContext.findViewById(R.id.fragment_text);
//        textView.setText(sortTag);
        Log.d("flexbox_lifeCycle: ","SortBackground: onActivityCreated");
//        tagCollection=(RelativeLayout)findViewById(R.id.tagCollection);
//        tagCollection=(LinearLayout) mActivityContext.findViewById(R.id.tagCollection);
//
//        //********************创建数据库********************
//        dbHelper=new MyDatabaseHelper(mActivityContext,MyDatabaseHelper.DATABASE_NAME,null,MyDatabaseHelper.DATABASE_VERSION);
//        dbHelper.getReadableDatabase();

//        recyclerView=(RecyclerView) findViewById(R.id.recycler_view_flexbox);

        SortBackground fragment=((Sort)mActivityContext).currentFragment;
        String sortTag=((Sort)mActivityContext).sortTag;//去到Sort的哪个fragment
//        getDayTagList();
        fragment.createSortComponents(sortTag);

    }

    @Override
    public void onResume() {
        super.onResume();
        int i=MyApplication.position;
        Log.d("flexbox_lifeCycle: ","SortBackground: onResume, position:"+i);
//        myAdpater.notifyItemChanged(3);
        if(MyApplication.anotherPosition!=0){
//            tagCollection.scrollToPosition(MyApplication.position);
            layoutManager.scrollToPosition(MyApplication.anotherPosition);
//            layoutManager.scrollToPositionWithOffset(MyApplication.anotherPosition,600);
            MyApplication.anotherPosition=0;
        }
//        Log.d("lifeCycle: ","list_position: "+list_position);
//        myAdpater.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view=inflater.inflate(R.layout.sort_background_fragment, container, false);
        View view=inflater.inflate(R.layout.sort_background, container, false);

//        tagCollection=(LinearLayout) view.findViewById(R.id.tagCollection);
        tagCollection=(RecyclerView) view.findViewById(R.id.tagCollection);

        Log.d("flexbox_lifeCycle: ","SortBackground: onCreateView");
//        mActivityContext = getActivity();
//
//

        //********************创建数据库********************
        dbHelper=new MyDatabaseHelper(mActivityContext,MyDatabaseHelper.DATABASE_NAME,null,MyDatabaseHelper.DATABASE_VERSION);
        dbHelper.getReadableDatabase();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            mActivityContext=(Activity) context;
        }
        Log.d("flexbox_lifeCycle: ","SortBackground: onAttach");
    }

    public SortBackground() {
        super(R.layout.sort_background);

        Log.d("flexbox_lifeCycle: ","SortBackground: 构造new()");

    }

    private void getTagList(String tag){
        int i=1;
        tagList = new ArrayList<>();
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        dbHelper.getReadableDatabase();
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

    private void getDayTagList(){
        String tag = "dateTaken";
        int i=1;
        tagList = new ArrayList<>();
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        dbHelper.getReadableDatabase();
        String[] columns={
                FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGEPATH,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL,
                FeedReaderContract.FeedEntry.COLUMN_NAME_DATETAKEN};
        Cursor cursor=db.query(FeedReaderContract.FeedEntry.TABLE_NAME, columns,
                null,null, null,null,
                FeedReaderContract.FeedEntry.COLUMN_NAME_DATETAKEN+" DESC");
        Log.d("groupByTest","cursor.getCount: "+cursor.getCount());
        //得保证数据库里有东西if(cursor.moveToFirst())
        cursor.moveToFirst();
        String nearDateTaken = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_DATETAKEN));
        cursor.moveToLast();
        String farDateTaken = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_DATETAKEN));
        String curTime=new SimpleDateFormat("yyyy:MM:dd").format(Calendar.getInstance().getTime());//2022:03:28
        int[] nearDays=MyDeltaDate.calculateMyDeltaDateFromDates(curTime,nearDateTaken);
        int[] farDays=MyDeltaDate.calculateMyDeltaDateFromDates(curTime,farDateTaken);
        String[] timePoints=MyDeltaDate.getTimePoints(curTime);
        String oneYearAgo=timePoints[0];
        String oneMonthAgo=timePoints[1];
        String oneWeekAgo=timePoints[2];
        List<CardItemEntity> moreThanOneYearAgoGroup=new ArrayList<>();
        List<CardItemEntity> oneYearAgoGroup=new ArrayList<>();
        List<CardItemEntity> oneMonthAgoGroup=new ArrayList<>();
        List<CardItemEntity> oneWeekAgoGroup=new ArrayList<>();
        Log.d("sort_day_group",MyDeltaDate.isFarerThanOrEqual("2022:05:10","2022:05:09")+" \noneWeekAgo:"+oneWeekAgo+" \noneMonthAgo:"+oneMonthAgo+" \noneYearAgo:"+oneYearAgo);
//        String[] curTimes={"2022:03:01","2022:01:03","2022:10:01","2005:11:01","2019:12:29"};
//        String[] timePoints=MyDeltaDate.getTimePoints(curTimes[0]);
//        String[] timePoints1=MyDeltaDate.getTimePoints(curTimes[1]);
//        String[] timePoints2=MyDeltaDate.getTimePoints(curTimes[2]);
//        String[] timePoints3=MyDeltaDate.getTimePoints(curTimes[3]);
//        String[] timePoints4=MyDeltaDate.getTimePoints(curTimes[4]);
//        boolean b1=MyDeltaDate.isFarerThanOrEqual("2022:05:03","2022/05/16");
//        boolean b2=MyDeltaDate.isFarerThanOrEqual("2022:07:03","2020/08/30");
//        boolean b3=MyDeltaDate.isFarerThanOrEqual("2022:07:03","2022/07/03");
        Log.d("groupByTest"," \ncurTime:"+curTime
//                +"\n"+curTimes[0]+":\n"+"oneYearAgo: "+timePoints[0]+"\noneMonthAgo: "+timePoints[1]+"\noneWeekAgo: "+timePoints[2]
//                +"\n"+curTimes[1]+":\n"+"oneYearAgo: "+timePoints1[0]+"\noneMonthAgo: "+timePoints1[1]+"\noneWeekAgo: "+timePoints1[2]
//                +"\n"+curTimes[2]+":\n"+"oneYearAgo: "+timePoints2[0]+"\noneMonthAgo: "+timePoints2[1]+"\noneWeekAgo: "+timePoints2[2]
//                +"\n"+curTimes[3]+":\n"+"oneYearAgo: "+timePoints3[0]+"\noneMonthAgo: "+timePoints3[1]+"\noneWeekAgo: "+timePoints3[2]
//                +"\n"+curTimes[4]+":\n"+"oneYearAgo: "+timePoints4[0]+"\noneMonthAgo: "+timePoints4[1]+"\noneWeekAgo: "+timePoints4[2]
//                +"b1: true:"+b1+",b2: false:"+b2+",b3: true:"+b3
                +"\nnearDateTaken: "+nearDateTaken+" \nnearDays: "+"year("+nearDays[0]+") month("+nearDays[1]+") day("+nearDays[2]+")"
                +"\nfarDateTaken: "+farDateTaken+" \nfarDays: "+"year("+farDays[0]+") month("+farDays[1]+") day("+farDays[2]+")");
        if(cursor.moveToFirst()){//moveToFirst
            do{
                String dateTaken = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_DATETAKEN));
                String title = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE));
                String label = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL));
                String imagePath = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGEPATH));
                if(MyDeltaDate.isFarerThanOrEqual(oneWeekAgo,dateTaken)){
                    Log.d("sort_day_group","一周:"+MyDeltaDate.getDaysTextFromDates(curTime,dateTaken)+" "+dateTaken);
                    oneWeekAgoGroup.add(new CardItemEntity(imagePath,title,label,dateTaken));
                }else if(MyDeltaDate.isFarerThanOrEqual(oneMonthAgo,dateTaken)){
                    Log.d("sort_day_group","一月:"+MyDeltaDate.getDaysTextFromDates(curTime,dateTaken)+" "+dateTaken);
                    oneMonthAgoGroup.add(new CardItemEntity(imagePath,title,label,dateTaken));
                }else if(MyDeltaDate.isFarerThanOrEqual(oneYearAgo,dateTaken)){
                    Log.d("sort_day_group","一年:"+MyDeltaDate.getDaysTextFromDates(curTime,dateTaken)+" "+dateTaken);
                    oneYearAgoGroup.add(new CardItemEntity(imagePath,title,label,dateTaken));
                }else {
                    Log.d("sort_day_group","更远:"+MyDeltaDate.getDaysTextFromDates(curTime,dateTaken)+" "+dateTaken);
                    moreThanOneYearAgoGroup.add(new CardItemEntity(imagePath,title,label,dateTaken));
                }

//                tagList.add(eachTag);
//                Log.d("groupByTest",i+": "+eachTag);
                Log.d("groupByTest","\n"+i+")\n"+dateTaken+"\ntitle:"+title+", imagePath:"+imagePath);
                i++;
            }while (cursor.moveToNext());//moveToNext
        }
        cursor.close();
//        eachTagCollectionList=new ArrayList<>();
        eachTagCollectionList=new LinkedHashMap<>();

        String oneWeek=getString(R.string.oneWeek_Eng);
        String oneMonth=getString(R.string.oneMonth_Eng);
        String oneYear=getString(R.string.oneYear_Eng);
        String overOneYear=getString(R.string.overOneYear_Eng);
        if(oneWeekAgoGroup.size()!=0){
            tagList.add(oneWeek);
            eachTagCollectionList.put(oneWeek,oneWeekAgoGroup);
//            eachTagCollectionList.add(new EachTagCollection("一周 7",oneWeekAgoGroup));
        }
        if(oneMonthAgoGroup.size()!=0){
            tagList.add(oneMonth);
            eachTagCollectionList.put(oneMonth,oneMonthAgoGroup);
//            eachTagCollectionList.add(new EachTagCollection("一月 30",oneMonthAgoGroup));

        }
        if(oneYearAgoGroup.size()!=0){
            tagList.add(oneYear);
            eachTagCollectionList.put(oneYear,oneYearAgoGroup);
//            eachTagCollectionList.add(new EachTagCollection("一年 365",oneYearAgoGroup));
        }
        if(moreThanOneYearAgoGroup.size()!=0){
            tagList.add(overOneYear);
            eachTagCollectionList.put(overOneYear,moreThanOneYearAgoGroup);
//            eachTagCollectionList.add(new EachTagCollection("更久 ...",moreThanOneYearAgoGroup));
        }
    }

    private void getLocationTagList(){
        String tag = "location";
        eachTagCollectionList=new LinkedHashMap<>();
        int i=1;
        tagList = new ArrayList<>();
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        dbHelper.getReadableDatabase();
        String[] columns={
                FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGEPATH,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL,
                FeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION};
        Cursor cursor=db.query(FeedReaderContract.FeedEntry.TABLE_NAME, columns,
                null,null, null,null,null);
        Log.d("groupByTest","cursor.getCount: "+cursor.getCount());
        if(cursor.moveToFirst()){
            do{
                String location = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION));
                String title = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE));
                String label = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL));
                String imagePath = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGEPATH));
                String[] textSplits=location.split(",");
                String firstLocation=textSplits[0];
                if(!tagList.contains(firstLocation)){
                    //其实好像也不需要加“无名国”，不是必须
                    tagList.add(firstLocation);
                    eachTagCollectionList.put(firstLocation,new ArrayList<CardItemEntity>());
                }
                eachTagCollectionList.get(firstLocation).add(new CardItemEntity(imagePath,title,label,location));
                Log.d("sort_location_group","group:"+firstLocation+", location:"+location+", title："+title);
            }while (cursor.moveToNext());//moveToNext
        }
        cursor.close();
    }

    private void getTagGroup(String tag){
        int i=1;

//        eachTagCollectionList=new ArrayList<>();
        eachTagCollectionList=new LinkedHashMap<>();
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String[] columns={ FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGEPATH,FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL};
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
                    String label = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL));
                    eachTagGroupList.add(new CardItemEntity(imagePath,title,label));
                    Log.d("groupByTest",j+": "+title);
                    j++;
                }while (cursor.moveToNext());//moveToNext
            }
            //'empty' group collected done
            eachTagCollectionList.put(eachTag,eachTagGroupList);
//            eachTagCollectionList.add(new EachTagCollection(eachTag,eachTagGroupList));
//            tagGroup.put(eachTag,eachTagGroupList);

            i++;
            cursor.close();
        }
    }

//    public void setSortTag(String sortTag){
//
////        this.sortTag=sortTag;
//        //------------------
////        this.mActivityContext = mActivityContext;
////        tagCollection=(LinearLayout) mActivityContext.findViewById(R.id.tagCollection);
////        dbHelper=new MyDatabaseHelper(mActivityContext,MyDatabaseHelper.DATABASE_NAME,null,MyDatabaseHelper.DATABASE_VERSION);
//        createSortComponents();
//    }

    public void createSortComponents(String sortTag){
        if(sortTag.equals("label")){
            Log.d("sort_location_group","********label");
            getTagList(sortTag);
            getTagGroup(sortTag);
        }else if(sortTag.equals("day")){
            Log.d("sort_location_group","********day");
            getDayTagList();
        } else if(sortTag.equals("location")){
            Log.d("sort_location_group","********location");
            getLocationTagList();
        }

        //Recycler View的adapter，不用tagGroup，
        //而是eachTagCollectionList
        InfoForEachGroupList=new ArrayList<>();
        for (Map.Entry<String, List<CardItemEntity>> eachTag : eachTagCollectionList.entrySet()) {
            String eachTagName=eachTag.getKey();
            String eachTagFirstImagePath=eachTag.getValue().get(0).getImagePath_card();
            int eachTagCount=eachTag.getValue().size();
            InfoForEachGroup infoForEachGroup=new InfoForEachGroup(eachTagName,eachTagFirstImagePath,eachTagCount);
            InfoForEachGroupList.add(infoForEachGroup);
        }
//        for(EachTagCollection eachTag:eachTagCollectionList){
//            String eachTagName=eachTag.getEachTagName();
//            String eachTagFirstImagePath=eachTag.getEachTagList().get(0).getImagePath_card();
//            int eachTagCount=eachTag.getEachTagList().size();
//            InfoForEachGroup infoForEachGroup=new InfoForEachGroup(eachTagName,eachTagFirstImagePath,eachTagCount);
//            InfoForEachGroupList.add(infoForEachGroup);
//        }
        myAdpater = new MyAdpaterForSort(mActivityContext, InfoForEachGroupList);
//        myAdpater = new MyAdpaterForSort(mActivityContext, eachTagCollectionList);
        myAdpater.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
        //new LinearLayoutManager() 参数为上下文环境，实现的是默认的垂直布局
//        layoutManager=new LinearLayoutManager(mActivityContext);

        layoutManager = new FlexboxLayoutManager(mActivityContext);
//        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        tagCollection.setLayoutManager(layoutManager);
        tagCollection.setAdapter(myAdpater);

//        final boolean[] isOpen = {false};

        myAdpater.setOnItemClickListener(new MyAdpaterForSort.OnItemClickListener(){
            @Override
            public void onItemClick(View view , int position){

//                list_position=String.valueOf(position);
                //全局！
                MyApplication.STATE=4;
                MyApplication.anotherPosition=position;
                String tagName=tagList.get(position);
                List<CardItemEntity> eachTagList=eachTagCollectionList.get(tagName);

                /*
                EachTagCollection eachTagCollection=eachTagCollectionList.get(position);
                String eachTagName= eachTagCollection.getEachTagName();
                final List<CardItemEntity> eachTagList=eachTagCollection.getEachTagList();
//                Toast.makeText(mActivityContext,
//                        "click MyAdpaterForSort--position: "+position+" ,label: "+eachTagName+" ,count:"+eachTagCollection.getEachTagList().size(),
//                        Toast.LENGTH_SHORT).show();
                Log.d("Sort_Test"," \nposition: "+position
                        +"\nlabel: "+eachTagName
                        +"\ncount:"+eachTagList.size());
                int i=1;
                StringBuilder logText=new StringBuilder();
                for(CardItemEntity  eachTagItem:eachTagList){
                    logText.append(" \n"+i+")"
                            +"\ndateTaken: "+eachTagItem.getDateTaken_card()
                            +"\nlabel: "+eachTagItem.getLabel_card()
                            +"\ntitle: "+eachTagItem.getTitle_card()
                            +"\nimagePath: "+eachTagItem.getImagePath_card());
                    i++;
                }
                Log.d("Sort_Test",logText.toString());
                 */
                Intent intent=null;
                intent=new Intent(mActivityContext,ShowEachTagCollection.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("eachTagList", (Serializable) eachTagList);
                intent.putExtras(bundle);
                startActivity(intent);

                /*
                Intent intent=null;
                intent=new Intent(mActivityContext,PhotoPortrait.class);
                intent.putExtra("ImagePath",eachTagCollection.getEachTagList().get(0).getImagePath_card());
                startActivity(intent);
                 */







                /*
                RecyclerView recyclerView=null;
                if (isOpen[0] == false) {
                    isOpen[0] = true;
                    int index = myAdpater.myViewHolder.getEachTagGroup().indexOfChild(recyclerView);
                    if (index == -1) {

                        recyclerView = new RecyclerView(mActivityContext);

                        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
                                1, StaggeredGridLayoutManager.VERTICAL);
//                        if(MyApplication.position!=0){
//                            layoutManager.scrollToPositionWithOffset(MyApplication.position,600);
//                            MyApplication.position=0;
//                        }
                        recyclerView.setLayoutManager(layoutManager);


//                        MyFlexboxAdapter myAdpater = new MyFlexboxAdapter(context, eachTagCollection.getEachTagList());
//                        final List<CardItemEntity> eachTagList=eachTagCollection.getEachTagList();
                        MySmallAdpater mySmallAdpater = new MySmallAdpater(mActivityContext, eachTagList);
                        recyclerView.setAdapter(mySmallAdpater);
                        mySmallAdpater.setOnItemClickListener(new MySmallAdpater.OnItemClickListener(){
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

                        myAdpater.myViewHolder.eachTagGroup.addView(recyclerView, rv_layoutParams);
                    }
                } else {
                    myAdpater.myViewHolder.eachTagGroup.removeAllViews();
                    isOpen[0] = false;
                }
                */
            }
        });


        //以下都为Linear Layout的创建TextView和CardView，再for loop加进去，
        //并且为每一个TextView设置点击“展开”事件-----用到了tagGroup

//        List<String> list=new ArrayList<>();
//        list.add("empty");
//        list.add("food");

        //这个for loop都是用linear layout不断增加（TextView+CardView）“串串”方式来实现的
//        for(String name: tagList) {//list测试用
//            final boolean[] isOpen = {false};
//            final String tagName = name;
//            //-------------------TextView---------------------
////            eachTagName=new TextView(mActivityContext);
//            final TextView eachTagName = new TextView(mActivityContext);
//            eachTagName.setText(tagName);
//            eachTagName.setTextSize(30);
//            eachTagName.setBackgroundColor(Color.parseColor("#3359EDED"));
//            ViewGroup.LayoutParams tv_layoutParams = new ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//            );
//            eachTagName.setLayoutParams(tv_layoutParams);
//            //margin(top:80dp)
//            //width/height(match,wrap)
//
//            //-------------------CardView---------------------
////            eachTagGroup=new CardView(mActivityContext);
//            final CardView eachTagGroup = new CardView(mActivityContext);
//            eachTagGroup.setCardBackgroundColor(Color.parseColor("#ffffff"));
//            eachTagGroup.setRadius(20);
//            eachTagGroup.setCardElevation(0);
////            CardView.LayoutParams cv_layoutParams=new CardView.LayoutParams(
////                    ViewGroup.LayoutParams.WRAP_CONTENT,
////                    ViewGroup.LayoutParams.WRAP_CONTENT
////            );
//            CardView.LayoutParams cv_layoutParams = new CardView.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//            );
//            cv_layoutParams.setMargins(0, 50, 0, 200);//l,t,r,b
//            eachTagGroup.setLayoutParams(cv_layoutParams);
//
//            //Linear layout的addView
//            tagCollection.addView(eachTagName);
//            tagCollection.addView(eachTagGroup);
//
//
//
//            //-------------------set click textView---------------------
//            eachTagName.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (isOpen[0] == false) {
////                    Toast.makeText(labelTest.this,"可编辑🍭 ✔ ",Toast.LENGTH_SHORT).show();
//                        isOpen[0] = true;
//                        if (eachTagGroup.indexOfChild(recyclerView) == -1) {
//
//                            recyclerView = new RecyclerView(mActivityContext);
//
//                            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
//                                    3, StaggeredGridLayoutManager.VERTICAL);
//                            if(MyApplication.position!=0){
//                                layoutManager.scrollToPositionWithOffset(MyApplication.position,600);
//                                MyApplication.position=0;
//                            }
//                            recyclerView.setLayoutManager(layoutManager);
//
//
//                            MyFlexboxAdapter myAdpater = new MyFlexboxAdapter(mActivityContext, tagGroup.get(tagName));
//                            recyclerView.setAdapter(myAdpater);
//
//                            RecyclerView.LayoutParams rv_layoutParams = new RecyclerView.LayoutParams(
//                                    ViewGroup.LayoutParams.WRAP_CONTENT,
//                                    ViewGroup.LayoutParams.WRAP_CONTENT
//                            );
////                        rv_layoutParams.setMargins(40,40,40,40);
//
//                            eachTagGroup.addView(recyclerView, rv_layoutParams);
//                        }
//                    } else {
//                        eachTagGroup.removeAllViews();
//                        isOpen[0] = false;
//                    }
//                }
//            });
//        }

    }

}
