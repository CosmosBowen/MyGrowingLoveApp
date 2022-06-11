package com.example.lbwapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "GrowingLove.db";

    private static final String SQL_CREATE_PHOTOLIBRARY =
            "CREATE TABLE "+ FeedReaderContract.FeedEntry.TABLE_NAME+"("+
                    FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGEPATH +" TEXT PRIMARY KEY,"+
                    FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE+" TEXT,"+
                    FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION+" TEXT,"+
                    FeedReaderContract.FeedEntry.COLUMN_NAME_DATETAKEN+" TEXT,"+
                    FeedReaderContract.FeedEntry.COLUMN_NAME_ISDATEEDITABLE+" INTEGER,"+
                    FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL+" TEXT,"+
                    FeedReaderContract.FeedEntry.COLUMN_NAME_ISCOUNTRYEDITABLE+" INTEGER,"+
                    FeedReaderContract.FeedEntry.COLUMN_NAME_LATITUDEANDLONGITUDE+" TEXT,"+
                    FeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION+" TEXT)";
    //v2.0
//    FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGEPATH +" TEXT PRIMARY KEY,"+
//    FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE+" TEXT,"+
//    FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION+" TEXT,"+
//    FeedReaderContract.FeedEntry.COLUMN_NAME_DAYNUMBER+" INTEGER,"+
//    FeedReaderContract.FeedEntry.COLUMN_NAME_ISDAYNUMBERALREADYCALCULATED+" INTEGER,"+
//    FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL+" TEXT,"+
//    FeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION+" TEXT)";
    //v1.0
//    FeedReaderContract.FeedEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
//    FeedReaderContract.FeedEntry.COLUMN_NAME_FILEPATH+" TEXT,"+
//    FeedReaderContract.FeedEntry.COLUMN_NAME_FILEID+" TEXT,"+
//    FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE+" TEXT,"+
//    FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION+" TEXT,"+
//    FeedReaderContract.FeedEntry.COLUMN_NAME_DAYNUMBER+" INTEGER,"+
//    FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL+" TEXT,"+
//    FeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION+" TEXT,"+
//    FeedReaderContract.FeedEntry.COLUMN_NAME_ISUSERPHOTO+" TEXT)";
    private static final String SQL_CREATE_LABELLIBRARY =
            "CREATE TABLE "+ FeedReaderContract.FeedEntry.LABEL_TABLE_NAME+"("+
                    FeedReaderContract.FeedEntry.COLUMN_NAME_LABELTEXT+" TEXT,"+
                    FeedReaderContract.FeedEntry.COLUMN_NAME_LABELCOLOR+" TEXT)";
    private static final String SQL_DELETE_PHOTOLIBRARY =
            "DROP TABLE IF EXISTS "+ FeedReaderContract.FeedEntry.TABLE_NAME;
    private static final String SQL_DELETE_LABELLIBRARY =
            "DROP TABLE IF EXISTS "+ FeedReaderContract.FeedEntry.LABEL_TABLE_NAME;

    private Context mContext;

    public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    private void addInitialLabels(SQLiteDatabase db){
        String[] texts={"travel","home","visit","abroad"};
        String[] colors={"#F892B5","#7BD1F8","#B3E47A","#FFEB3F"};
        ContentValues values=new ContentValues();
        for(int i=0;i<texts.length;i++){
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LABELTEXT, texts[i]);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LABELCOLOR, colors[i]);
            db.insert(FeedReaderContract.FeedEntry.LABEL_TABLE_NAME,null,values);
            values.clear();
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PHOTOLIBRARY);
        db.execSQL(SQL_CREATE_LABELLIBRARY);
        Toast.makeText(mContext,"new version db create succeeded",Toast.LENGTH_SHORT).show();
        addInitialLabels(db);
//        getCardData();
//        ContentValues values=new ContentValues();
//        //把数据cardList加到数据库db里
//        for(CardItemEntity card:cardList){
//            values.put("fileId",card.getImage_card());
//            values.put("filePath",card.getImagePath_card());
//            values.put("title",card.getTitle_card());
//            values.put("description",card.getText_card());
//            values.put("dayNumber",card.getDayNumber_card());
//            values.put("label",card.getLabel_card());
//            values.put("location",card.getPlace_card());
//            values.put("isUserPhoto",String.valueOf(card.isUserPhoto()));
//            db.insert("PhotoList",null,values);
//            values.clear();
//        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_PHOTOLIBRARY);
        db.execSQL(SQL_DELETE_LABELLIBRARY);
        onCreate(db);

    }

}
