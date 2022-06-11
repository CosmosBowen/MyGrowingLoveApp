package com.example.lbwapp;

import android.provider.BaseColumns;

public final class FeedReaderContract {

    private FeedReaderContract(){}

    public static class FeedEntry implements BaseColumns{
        public static final String TABLE_NAME = "PhotoLibrary";
        public static final String COLUMN_NAME_IMAGEPATH = "imagePath";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_DATETAKEN = "dateTaken";
        public static final String COLUMN_NAME_ISDATEEDITABLE = "isDateEditable";
        public static final String COLUMN_NAME_LABEL = "label";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_LATITUDEANDLONGITUDE = "latitudeAndLongitude";
        public static final String COLUMN_NAME_ISCOUNTRYEDITABLE = "isCountryEditable";
        //V2.0
//        public static final String COLUMN_NAME_ISDAYNUMBERALREADYCALCULATED = "isdayNumberAlreadyCalculated";
//        public static final String COLUMN_NAME_DAYNUMBER = "dayNumber";
        //V1.0
//        public static final String COLUMN_NAME_ISUSERPHOTO = "isUserPhoto";
//        public static final String COLUMN_NAME_FILEID = "fileId";

        public static final String LABEL_TABLE_NAME = "LabelLibrary";
        public static final String COLUMN_NAME_LABELTEXT = "labelText";
        public static final String COLUMN_NAME_LABELCOLOR = "labelColor";


    }
}
