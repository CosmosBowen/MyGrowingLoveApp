package com.example.lbwapp;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PhotoList extends Activity {


    public static final String TRAVEL = "travel";
    public static final String ABOARD = "aboard";
    public static final String HOME = "home";
    public static final String VISIT = "visit";
    public static final String EMPTY = "empty";

    LinearLayout photoList_welcomingText;
    List<CardItemEntity> cardList;
//    String list_position="";
    MyAdapter myAdpater;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    private MyDatabaseHelper dbHelper;

    private long exitTime=0;//用来判断程序是否在两次点击的时间间隔中退出程序

//    private void replaceFragment(Fragment fragment){
//        FragmentManager fragmentManager=getSupportFragmentManager();
//        FragmentTransaction transaction=fragmentManager.beginTransaction();
//        transaction.replace(R.id.photoList_fragment,fragment);
////        transaction.addToBackStack(null);
//        transaction.commit();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        photoList_welcomingText=findViewById(R.id.photoList_welcomingText);
        recyclerView = findViewById(R.id.recycler_view);



        MyApplication.getInstance().addActivity(this);

        Log.d("lifeCycle: ","onCreate");
        //回到应该在的卡片位置：list_position
//        if(savedInstanceState!=null){
//            list_position=savedInstanceState.getString("list_position");
//            Log.d("lifeCycle: ","savedInstanceState了: "+list_position);
//        }else {
//            Log.d("lifeCycle: ","no list_position");
//        }

        //********************创建数据库********************
        dbHelper=new MyDatabaseHelper(this,MyDatabaseHelper.DATABASE_NAME,null,MyDatabaseHelper.DATABASE_VERSION);
        dbHelper.getWritableDatabase();
        Log.d("MainActivity","create database");

        //deprecated idea
        //设想的全局标记：最后一次打开app的时间，
        //和全部照片统一需要增加的天数
//        String currentDate = new SimpleDateFormat("yyyy:MM:dd").format(Calendar.getInstance().getTime());
//        String theLastOpennedDate=MyApplication.getTheLastOpennedDate();
//        if(currentDate!=theLastOpennedDate){
//            int days=MyApplication.calculateDaysBetweenFromDates(currentDate,theLastOpennedDate);
//            MyApplication.setAddDays(days);
//            //数据库addDays...
//            MyApplication.setTheLastOpennedDate(currentDate);
//        }
        ImageView imageView_bg = findViewById(R.id.bg);
        Glide.with(this).load(R.drawable.bg).error( R.drawable.family1 ).into(imageView_bg);
//        //效率低的方式改变字体
//        TextView title = findViewById(R.id.cardTitle);
//        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/huakang.ttc"));
        //设置滚动视图，在PhotoList里放置自定义的CardView卡片

        //********************数据库db往cardList动态加载数据********************
//        SQLiteDatabase db=dbHelper.getWritableDatabase();
//        Cursor cursor=db.query("PhotoList",null,null,null,null,null,null);
//        if(cursor.moveToFirst()){
//            do{
//                int fileId=cursor.getInt(cursor.getColumnIndex("fileId"));
//                String filePath=cursor.getString(cursor.getColumnIndex("filePath"));
//                String title=cursor.getString(cursor.getColumnIndex("title"));
//                String description=cursor.getString(cursor.getColumnIndex("description"));
//                int dayNumber=cursor.getInt(cursor.getColumnIndex("dayNumber"));
//                String label=cursor.getString(cursor.getColumnIndex("label"));
//                String location=cursor.getString(cursor.getColumnIndex("location"));
//                String isUserPhoto=cursor.getString(cursor.getColumnIndex("isUserPhoto"));
//
//                CardItemEntity item=new CardItemEntity(fileId,title,dayNumber,description,label,location);
//                item.setUserPhoto(Boolean.parseBoolean(isUserPhoto));
//                cardList.add(item);
//            }while (cursor.moveToNext());
//        }
//        cursor.close();


        boolean isNoCard=getCardData();
        Toast.makeText(this,"isNoCard: "+isNoCard,Toast.LENGTH_SHORT).show();
        if(isNoCard){
            //显示文字
//            replaceFragment(new PhotoList_TextView());
            photoList_welcomingText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }else {
            //显示图片列表
//            replaceFragment(new PhotoList_RecyclerView());
            photoList_welcomingText.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            myAdpater = new MyAdapter(this, cardList);
            myAdpater.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
            //new LinearLayoutManager() 参数为上下文环境，实现的是默认的垂直布局
            layoutManager=new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(myAdpater);
            myAdpater.setOnItemClickListener(new MyAdapter.OnItemClickListener(){
                @Override
                public void onItemClick(View view , int position){

//                list_position=String.valueOf(position);
                    //全局！
                    MyApplication.STATE=2;
                    MyApplication.position=position;

                    CardItemEntity cardItem=cardList.get(position);
                    Intent intent=null;
                    //*************判断照片是竖的还是横的（通过比较长宽）*************
//                BitmapFactory.Options opts = new BitmapFactory.Options();
//                opts.inJustDecodeBounds = true;
//                BitmapFactory.decodeResource(getResources(),cardItem.getImage_card() , opts);
//                int width=opts.outWidth;
//                int height=opts.outHeight;
//                if(width>=height){
//                    //横
//                    intent=new Intent(PhotoList.this,PhotoLandscape.class);
//                }else {
//                    //竖
//                    intent=new Intent(PhotoList.this,PhotoPortrait.class);
//                }

                    //***************************Location测试*********************
//                intent=new Intent(PhotoList.this,LocationTest.class);
                    intent=new Intent(PhotoList.this,PhotoPortrait.class);
                    //***************************Location测试*********************

                    //*************两种方式传item**************
                    //通过Bundle传item
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("CardItemEntity",cardItem);//将CardItemEntity类通过Bundle传递给Detail界面
//                intent.putExtras(bundle);

                    //直接通过intent传item.imagePath
                    //直接拿到该item对象的imagePath
                    intent.putExtra("ImagePath",cardItem.getImagePath_card());
//                intent.putExtra("fromActivity","PhotoList");

                    startActivity(intent);
                }
            });
        }


        CardView cardViewAddPhotos=findViewById(R.id.btn_addPhoto);
        cardViewAddPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PhotoList.this,TakePhotos.class);
                startActivity(intent);
            }
        });
        CardView cardViewClassifiedPhotos=findViewById(R.id.btn_classifiedPhotos);
        cardViewClassifiedPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(PhotoList.this, "yeah", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(PhotoList.this,Sort.class);
                startActivity(intent);
            }
        });


// ***********************For test**************************
//        //数据库处理
//        //create
//        Button createButton=(Button)findViewById(R.id.create_db);
//        createButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dbHelper.getWritableDatabase();
//            }
//        });
//        //add
//        Button addButton=(Button)findViewById(R.id.add_data);
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String filePath="path/haha";
//                int fileId=123;
//                String title="happy day";
//                String description="no sleeping day👾";
//                int dayNumber=6;
//                String label="travel";
//                String location="湖北武汉";
//                String isUserPhoto="false";
//
//                SQLiteDatabase db=dbHelper.getWritableDatabase();
//                ContentValues values=new ContentValues();
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FILEPATH, filePath);
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FILEID, fileId);
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, title);
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION, description);
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DAYNUMBER, dayNumber);
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL, label);
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION, location);
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ISUSERPHOTO, isUserPhoto);
//                db.insert(FeedReaderContract.FeedEntry.TABLE_NAME,null,values);
//
//                values.clear();
//
//                String filePath2="The Lost Symbol";
//                int fileId2=888888888;
//                String title2="bad day";
//                String description2="music!!!!";
//                int dayNumber2=350;
//                String label2="home";
//                String location2="美国纽约";
//                String isUserPhoto2="true";
//
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FILEPATH, filePath2);
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FILEID, fileId2);
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, title2);
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION, description2);
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DAYNUMBER, dayNumber2);
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL, label2);
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION, location2);
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ISUSERPHOTO, isUserPhoto2);
//                db.insert(FeedReaderContract.FeedEntry.TABLE_NAME,null,values);
//            }
//        });
//        //update
//        Button updateButton=(Button)findViewById(R.id.update_data);
//        updateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SQLiteDatabase db=dbHelper.getWritableDatabase();
//                String title="omg";
//                ContentValues values=new ContentValues();
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,title);
//                String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_FILEPATH+" = ?";
//                String[] selectionArgs={"The Lost Symbol"};
//                //受影响的行数
//                int count = db.update(
//                        FeedReaderContract.FeedEntry.TABLE_NAME,
//                        values,
//                        selection,
//                        selectionArgs);
//            }
//        });

//        //delete
//        Button deleteButton=(Button)findViewById(R.id.delete_data);
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_DAYNUMBER+" < ?";
//                String[] selectionArgs = {"200"};
//                SQLiteDatabase db=dbHelper.getWritableDatabase();
//                //从数据库删除的行数
//                int deletedRows = db.delete(
//                        FeedReaderContract.FeedEntry.TABLE_NAME,
//                        selection,
//                        selectionArgs);
//                //同效果：db.delete(FeedReaderContract.FeedEntry.TABLE_NAME,"dayNumber > ?",new String[]{"200"});
//            }
//        });

//        //query
//        Button queryButton=(Button)findViewById(R.id.query_data);
//        queryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SQLiteDatabase db=dbHelper.getWritableDatabase();
//                Cursor cursor=db.query(FeedReaderContract.FeedEntry.TABLE_NAME,null,null,null,null,null,null);
//                if(cursor.moveToFirst()){
//                    do{
//                        String filePath = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_FILEPATH));
//                        int fileId = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_FILEID));
//                        String title = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE));
//                        String description = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION));
//                        int dayNumber = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_DAYNUMBER));
//                        String label = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL));
//                        String location = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION));
//                        String isUserPhoto = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_ISUSERPHOTO));
//                        Log.d("MainActivity", "filePath is " + filePath);
//                        Log.d("MainActivity", "filePath is " + fileId);
//                        Log.d("MainActivity", "title is " + title);
//                        Log.d("MainActivity", "description is " + description);
//                        Log.d("MainActivity", "dayNumber is " + dayNumber);
//                        Log.d("MainActivity", "label is " + label);
//                        Log.d("MainActivity", "location is " + location);
//                        Log.d("MainActivity", "isUserPhoto is " + isUserPhoto);
////                        String fileId=cursor.getString(cursor.getColumnIndex("fileId"));
////                        String filePath=cursor.getString(cursor.getColumnIndex("filePath"));
////                        String title=cursor.getString(cursor.getColumnIndex("title"));
////                        String description=cursor.getString(cursor.getColumnIndex("description"));
////                        int dayNumber=cursor.getInt(cursor.getColumnIndex("dayNumber"));
////                        String label=cursor.getString(cursor.getColumnIndex("label"));
////                        String location=cursor.getString(cursor.getColumnIndex("location"));
////                        String isUserPhoto=cursor.getString(cursor.getColumnIndex("isUserPhoto"));
////                        Log.d("hh","query data:"+"\n"+fileId+","+filePath+","+title+","+description+","+dayNumber+","+label+","+location+","+isUserPhoto);
//                    }while (cursor.moveToNext());
//                }
//                cursor.close();
//
//            }
//        });
    }

    //这tm有点奇怪啊，怎么是写在noKeyDown里面呢？？？？！！！！退出，不应该这样写吧...
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if((System.currentTimeMillis()-exitTime)>2000){
            Toast.makeText(PhotoList.this,getString(R.string.exitApp_Eng),Toast.LENGTH_SHORT).show();
            //更新上一次点击返回键的时间
            exitTime=System.currentTimeMillis();
        }else {
            MyApplication.getInstance().exit();//单例模式，使整个app在这里退出
//            finish();//测试！！！！！！！
//            System.exit(0);//我猜这个是直接结束整个app进程
        }
    }

    private boolean getCardData () {
        cardList = new ArrayList<>();

        //模拟从数据库获得的一行行数据项

// ***********************For test->从资源库里放的样本图片**************************
//        cardList.add(new CardItemEntity(
//                R.drawable.family1,
//                "和弟弟一起玩",
//                3,
//                "和弟弟一起玩，小丫和两个弟弟一起在公园搭帐篷、过家家，小丫和弟弟一起吃蛋糕、冰淇淋，喝饮料，玩的很开心！还一起玩了荡秋千和做迷藏。我们还一起抓小鱼。",
//                TRAVEL,
//                "中国香港小丫和两个弟弟一起在公园搭帐篷、过家家，"));
//        cardList.add(new CardItemEntity(
//                R.drawable.family2,
//                "和姐姐一起玩",
//                5,
//                "和姐姐一起玩，小丫和两个弟弟一起在公园搭帐篷、过家家，小丫和弟弟一起吃蛋糕、冰淇淋，喝饮料，玩的很开心！还一起玩了荡秋千和做迷藏。如果有人钟爱着一朵独一无二的盛开在浩瀚星海里的花。那么，当他抬头仰望繁星时，便会心满意足。他会告诉自己：“我心爱的花在那里，在那颗遥远的星星上。”可是，如果羊把花吃掉了。那么，对他来说，所有的星光变会在刹那间暗淡无光！而你却认为这并不重要！",
//                ABOARD,
//                "美国加州"));
//        cardList.add(new CardItemEntity(
//                R.drawable.family3,
//                "和妈妈一起玩",
//                10,
//                "和妈妈一起玩，小丫和两个弟弟一起在公园搭帐篷、过家家，小丫和弟弟一起吃蛋糕、冰淇淋，喝饮料，玩的很开心！还一起玩了荡秋千和做迷藏。为忘记自己的朋友是一件悲哀的事情，并不是每个人都有朋友，如果我忘记了小王子，那我就会变得和那些除了对数字感兴趣，对其他事都漠不关心的大人们一样了。",
//                HOME,
//                "澳大利亚悉尼"));
//        cardList.add(new CardItemEntity(
//                R.drawable.family4,
//                "和妹妹一起玩",
//                19,
//                "和妹妹一起玩，小丫和两个弟弟一起在公园搭帐篷、过家家，小丫和弟弟一起吃蛋糕、冰淇淋，喝饮料，玩的很开心！还一起玩了荡秋千和做迷藏。玫瑰花：哦，如果我想跟蝴蝶交朋友的话，当然就得忍耐两三只毛毛虫的拜访咯。我听说蝴蝶长的很漂亮。况且，如果没有蝴蝶，没有毛毛虫，还会有谁来看我呢？你离我那么远...至于大动物，我才不怕呢，我有我的力爪啊。",
//                ABOARD,
//                "英国伦敦"));
//        cardList.add(new CardItemEntity(
//                R.drawable.family5,
//                "和爸爸一起玩",
//                30,
//                "和爸爸一起玩，小丫和两个弟弟一起在公园搭帐篷、过家家，小丫和弟弟一起吃蛋糕、冰淇淋，喝饮料，玩的很开心！还一起玩了荡秋千和做迷藏。就像水一样因为辘轳和绳子，使得你让我喝的水有如音乐一般。你记得吗？它是如此凄美。",
//                VISIT,
//                "日本东京"));
//        cardList.add(new CardItemEntity(
//                R.drawable.family6,
//                "和外公外婆一起玩",
//                44,
//                "和外公外婆一起玩，小丫和两个弟弟一起在公园搭帐篷、过家家，小丫和弟弟一起吃蛋糕、冰淇淋，喝饮料，玩的很开心！还一起玩了荡秋千和做迷藏。小王子：人吗？我想大概有六七个吧，几年前看到过他们，但我不知道在哪能找到他们，风把他们吹散了，他们没有根，活得很辛苦。",
//                HOME,
//                "印度"));
//        cardList.add(new CardItemEntity(
//                R.drawable.family7,
//                "和爷爷奶奶一起玩",
//                47,
//                "和爷爷奶奶一起玩，小丫和两个弟弟一起在公园搭帐篷、过家家，小丫和弟弟一起吃蛋糕、冰淇淋，喝饮料，玩的很开心！还一起玩了荡秋千和做迷藏。狐狸说：“对我而言，你只不过是个小男骇，就像其他千万个小男孩一样。我不需要你，你也同样用不着我。对你来说。我也只不过是只狐狸，就跟其他千万只狐狸一样。然而，如果你驯养我。我们将会彼此需要，对我而言，你将是宇宙唯一的了，我对你来说，也是世界上唯一的了。",
//                TRAVEL,
//                "非洲大草原"));
//        cardList.add(new CardItemEntity(
//                R.drawable.family8,
//                "我自己玩",
//                148,
//                "我自己玩，小丫和两个弟弟一起在公园搭帐篷、过家家，小丫和弟弟一起吃蛋糕、冰淇淋，喝饮料，玩的很开心！还一起玩了荡秋千和做迷藏。“人群里也是很寂寞的。”蛇说。",
//                HOME,
//                "南美洲小岛"));
//        cardList.add(new CardItemEntity(
//                R.drawable.pan1,
//                "潘潘和他的故事",
//                200,
//                "我自己玩，小丫和两个弟弟一起在公园搭帐篷、过家家，小丫和弟弟一起吃蛋糕、冰淇淋，喝饮料，玩的很开心！还一起玩了荡秋千和做迷藏。“人群里也是很寂寞的。”蛇说。",
//                HOME,
//                "马来西亚"));
//        cardList.add(new CardItemEntity(
//                R.drawable.pan2,
//                "这是一个浪漫的故事",
//                356,
//                "我自己玩，小丫和两个弟弟一起在公园搭帐篷、过家家，小丫和弟弟一起吃蛋糕、冰淇淋，喝饮料，玩的很开心！还一起玩了荡秋千和做迷藏。“人群里也是很寂寞的。”蛇说。",
//                HOME,
//                "新西兰"));

        //直接在这对主页数据修改，更新/删除
        /*
        ContentValues values=new ContentValues();

        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL,"food");//v(vx)
//        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION,"China,成都, ");//v(vx)
//        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION,"China, ,南宁小家");//v(xv)
//        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION," ,California,Silicon Valley");//x(vv)
//        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION," , , ");//x(xx)
//        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION,"China,Beijing,ChaoyangQu");//v(vv)
        String selectionUpdate = FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL +" = ?";
        String[] selectionArgsUpdate={"超级无敌好"};
        SQLiteDatabase db=dbHelper.getWritableDatabase();//！！！！！因为没有onDestroy,所以不能：dbHelper.close();
//        //从数据库更新/删除的行数
        int count=0;
        count = db.update(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                values,
                selectionUpdate,
                selectionArgsUpdate);
        Log.d("update","updatedRows:"+count);
         */


        SQLiteDatabase db=dbHelper.getWritableDatabase();//！！！！！因为没有onDestroy,所以不能：dbHelper.close();
        Cursor cursor=db.query(FeedReaderContract.FeedEntry.TABLE_NAME,null,null,null,null,null,null);
        //新加的置顶，所以应该倒过来，一直网上叠加，就像instagram或者微信朋友圈，最新的一定放在最上面
        if(cursor.moveToLast()){//moveToFirst
            do{
                String imagePath = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGEPATH));
                String title = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION));
                String dateTaken = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_DATETAKEN));
                String label = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL));
                String location = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION));
                //***********************For test***********************
                //v2.0
//                int dayNumber = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_DAYNUMBER));
                //v1.0
//                String isUserPhoto = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_ISUSERPHOTO));
//                int fileId = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_FILEID));
                cardList.add(new CardItemEntity(imagePath,title,dateTaken,description,label,location));
            }while (cursor.moveToPrevious());//moveToNext
        }
        cursor.close();

        if(cardList.size()==0){
            return true;
        }else {
            return false;
        }

        //另一种放数据的方式
//        int[] images = new int[]{R.drawable.family1, R.drawable.family2, R.drawable.family3, R.drawable.family4, R.drawable.family5, R.drawable.family6, R.drawable.family7, R.drawable.family8};
//        String[] titles = new String[]{"和弟弟一起玩", "和姐姐一起玩", "和妈妈一起玩", "和妹妹一起玩", "和爸爸一起玩", "和外公外婆一起玩", "和爷爷奶奶一起玩", "我自己玩"};
//        String text = "小丫和两个弟弟一起在公园搭帐篷、过家家，小丫和弟弟一起吃蛋糕、冰淇淋，喝饮料，玩的很开心！还一起玩了荡秋千和做迷藏。";
//        int[] days = new int[]{3, 5, 10, 19, 30, 44, 47, 88};
//        String[] labels = new String[]{TRAVEL, ABOARD, HOME, ABOARD, VISIT, HOME, TRAVEL, HOME};
//        for (int i=0;i<days.length;i++) {
//            CardItemEntity cardItemEntity = new CardItemEntity();
//            cardItemEntity.setImage_card(images[i]);
//            cardItemEntity.setTitle_card(titles[i]);
//            cardItemEntity.setDay_card(days[i]);
//            cardItemEntity.setText_card(text);
//            cardItemEntity.setLabel_card(labels[i]);
//            cardList.add(cardItemEntity);
//        }
    }



//    @Override
//    protected void onSaveInstanceState(Bundle outState){
//        super.onSaveInstanceState(outState);
//        outState.putString("list_position",""+list_position);
//        Log.d("lifeCycle: ","onSaveInstanceState:"+list_position);
//
//    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lifeCycle: ","onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifeCycle: ","onResume");
//        myAdpater.notifyItemChanged(3);
        if(MyApplication.position!=0){
//            recyclerView.scrollToPosition(MyApplication.position);
            layoutManager.scrollToPositionWithOffset(MyApplication.position,600);
            MyApplication.position=0;
        }
//        Log.d("lifeCycle: ","list_position: "+list_position);
//        myAdpater.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifeCycle: ","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lifeCycle: ","onStop");
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
        Log.d("lifeCycle: ","onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifeCycle: ","onRestart");
    }


}