package com.example.lbwapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.baidu.mapapi.animation.AlphaAnimation;
import com.baidu.mapapi.animation.Animation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment {

    private MyDatabaseHelper dbHelper;

    private List<LatLng> points=new ArrayList<>();
    private List<String> pointsLocation=new ArrayList<>();
    private List<String> imagePaths=new ArrayList<>();
    private List<String> titles=new ArrayList<>();
    private List<Marker> imageMarkers=new ArrayList<>();
//    private int times=0;
    private String imageClicked="";
//    LinearLayout ll_images;
    LinearLayout layout_container;

    private BaiduMap mBaiduMap;
    private MapView mapView;
    private Activity mActivityContext;
    RequestOptions requestOptions;
    private int size=480;
    private long duration=200;
    private float alpha=0.5f;
    private LatLng initialLocation=new LatLng(35.86166,104.195397);//中国
    private float zoom=4;

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            mActivityContext=(Activity) context;
        }
        Log.d("mapFragment_lifeCycle: ","onAttach");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        getLocationInfoFromDB();


//        mActivityContext.setContentView(mapView);
//        View view=inflater.inflate(mapView,container,false);
//        View view=inflater.inflate(R.layout.map_fragment,container,false);
//        layout_container=view.findViewById(R.id.layout_container);
//        ll_images=view.findViewById(R.id.ll_images);
//        mapView=view.findViewById(R.id.mapView);
        Log.d("mapFragment_lifeCycle: ","onCreateView");

        BaiduMapOptions options = new BaiduMapOptions();
        //不允许地图旋转手势
        options.rotateGesturesEnabled(false);

        //设置语言，好像不行...百度地图，之后用谷歌地图再开发一个...

        //允许拖拽手势
//        options.scrollGesturesEnabled(true);
        //不允许俯视图手势：两指下滑后平面翘起的效果
        options.overlookingGesturesEnabled(false);
        mapView = new MapView(mActivityContext,options);
//        ViewGroup.LayoutParams mv_layoutParams=new ViewGroup.LayoutParams(
//                1500,1500
//        );

//        layout_container.addView(mapView);
//        layout_container.addView(mapView,mv_layoutParams);
        return mapView;//不需要布局文件（非必须），这个mapFragment只需要一个return mapView就行了
//        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("mapFragment_lifeCycle: ","onActivityCreated");
        requestOptions = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);

        /*
        //************定义icon Maker坐标点************
        LatLng point1 = new LatLng(39.944251, 116.494996);
        LatLng point2 = new LatLng(38.898556, -77.037852);
//        LatLng point2 = new LatLng(39.86923, 116.397428);

        //********拿到图片**********
        ExifInterface exif1 = null;
        ExifInterface exif2 = null;
        String imagePath1="/storage/emulated/0/DCIM/Camera/IMG_20220514_184917.jpg";
        String imagePath2="/storage/emulated/0/DCIM/Camera/IMG_20220515_223434.jpg";
        try {
            exif1 = new ExifInterface(imagePath1);
            exif2 = new ExifInterface(imagePath2);

        } catch (IOException e) {
            Log.d("thumbnail","不能获取缩略图");
//            e.printStackTrace();
        }

        BitmapDescriptor iconBitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.marker);
        OverlayOptions iconOption=null;
        iconOption = new MarkerOptions()
                .position(point1) //必传参数
                .icon(iconBitmap) //必传参数
                .alpha(1f)
                .anchor((float) point1.latitude,(float) point1.longitude);

        OverlayOptions option1=null;
        OverlayOptions option2=null;
        byte[] imageData1=exif1.getThumbnail();
        byte[] imageData2=exif2.getThumbnail();
        if(imageData1!=null){
            Bitmap thumbnail = BitmapFactory.decodeByteArray(imageData1,0,imageData1.length);
            BitmapDescriptor bitmap1 = BitmapDescriptorFactory.fromBitmap(thumbnail);
            option1 = new MarkerOptions()
                    .position(point1) //必传参数
                    .icon(bitmap1) //必传参数
                    .alpha(0.5f);
        }
        if(imageData2!=null){
            Bitmap thumbnail = BitmapFactory.decodeByteArray(imageData2,0,imageData2.length);
            BitmapDescriptor bitmap2 = BitmapDescriptorFactory.fromBitmap(thumbnail);
            option2 = new MarkerOptions()
                    .position(point2) //必传参数
                    .icon(bitmap2) //必传参数
                    .alpha(0.5f);
        }

        //构建Marker图标
//        BitmapDescriptor bitmap1 = BitmapDescriptorFactory.fromFile();
//        BitmapDescriptor bitmap2 = BitmapDescriptorFactory.fromResource(R.drawable.family4);
        //构建MarkerOption，用于在地图上添加Marker
//        OverlayOptions option1 = new MarkerOptions()
//                .position(point1) //必传参数
//                .icon(bitmap1) //必传参数
//                .alpha(0.5f);
//                .draggable(true)
//                //设置平贴地图，在地图中双指下拉查看效果!!!
//                .flat(true)
//                .alpha(0.5f);
        //在地图上添加Marker，并显示
//        OverlayOptions option2 = new MarkerOptions()
//                .position(point2) //必传参数
//                .icon(bitmap2) //必传参数
//                .alpha(0.5f);


        Marker marker1=(Marker) mBaiduMap.addOverlay(option1);
        Bundle bundle1=new Bundle();
        bundle1.putString("imagePath",imagePath1);
        marker1.setExtraInfo(bundle1);

        //为marker1 添加动画--失败
//        ScaleAnimation anim=new ScaleAnimation(0.5f,1,0.5f,1);
//        anim.setDuration(500);
//        anim.setRepeatMode(Animation.RepeatMode.RESTART.ordinal());
//        marker1.setAnimation(anim);


        Marker marker=(Marker) mBaiduMap.addOverlay(iconOption);
//        marker.anchor(-100,-100);

        Marker marker2=(Marker) mBaiduMap.addOverlay(option2);
        Bundle bundle2=new Bundle();
        bundle2.putString("imagePath",imagePath2);
        marker2.setExtraInfo(bundle2);



        //************定义text Maker坐标点************
        LatLng llText = new LatLng(39.944251, 116.494996);
        //构建TextOptions对象
        OverlayOptions mTextOptions = new TextOptions()
                .text("百度地图SDK") //文字内容
                .bgColor(0xAAFFFF00) //背景色
                .fontSize(24) //字号
                .fontColor(0xFFFF00FF) //文字颜色
                .rotate(0) //旋转角度
                .position(llText);
        //在地图上显示文字覆盖物
        Overlay mText = mBaiduMap.addOverlay(mTextOptions);

         */

        /*
        //定义Ground的显示地理范围
        LatLng southwest = new LatLng(39.92235, 116.380338);
        LatLng northeast = new LatLng(39.947246, 116.414977);
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(northeast)
                .include(southwest)
                .build();

        ExifInterface exif = null;
        String imagePath="/storage/emulated/0/DCIM/Camera/IMG_20220514_184917.jpg";
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] imageData=exif.getThumbnail();
        if(imageData!=null) {
            Bitmap thumbnail = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            BitmapDescriptor bdGround = BitmapDescriptorFactory.fromBitmap(thumbnail);
            //定义Ground显示的图片
//        BitmapDescriptor bdGround = BitmapDescriptorFactory.fromResource(R.drawable.family3);
            //定义GroundOverlayOptions对象
            OverlayOptions ooGround = new GroundOverlayOptions()
                    .positionFromBounds(bounds)
                    .image(bdGround)
                    .transparency(0.8f); //覆盖物透明度
            // 在地图中添加Ground覆盖物
            mBaiduMap.addOverlay(ooGround);




        }
         */
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        Log.d("mapFragment_lifeCycle: ","onResume");
        //在activity执行onResume时必须调用mMapView. onResume ()
        mapView.onResume();

        //不显示缩放按钮
        mapView.showZoomControls(false);
        mBaiduMap=mapView.getMap();
//        mBaiduMap.getGLMapView().setZoomLevel(4);

        //set center成功！
        //set zoom level and center
//        initialLocation=MyApplication.MapLocation;


        /*
        //只有从"大图"来，才要改变，若是从其他地方来，就用默认的最远zoom=4和center=中国
        if(MyApplication.STATE==4){
            initialLocation=MyApplication.MapLocation;
            zoom=MyApplication.ZOOM;
            MyApplication.STATE=0;
        }

         */

        //不论是从“大图”，还是“主页”，Map视图都保持不变！
        initialLocation=MyApplication.MapLocation;
        zoom=MyApplication.ZOOM;
        MyApplication.STATE=0;

//        String[] splits=initialLocation.split(",");
//        LatLng centerPoint = new LatLng(Double.parseDouble(splits[0]),Double.parseDouble(splits[1]));//初始，以中国39.87234878527778,116.47469329833334
        MapStatus mMapStatus = new MapStatus.Builder()
                .zoom(zoom)
                .target(initialLocation)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);//Change the map status

//        GeoPoint geoPoint=new GeoPoint(0,0);//39.87234878527778,116.47469329833334
//        mBaiduMap.getGLMapView().setMapCenter(geoPoint);
        for(int i=0;i<points.size();i++){
            LatLng point=points.get(i);

            //*********image 方法3*********
            View view_mapCard=LayoutInflater.from(mActivityContext).inflate(R.layout.card_map,null);
//            ImageView icon=(ImageView) view_mapCard.findViewById(R.id.marker_mapCard);
            ImageView image_mapCard=(ImageView)view_mapCard.findViewById(R.id.image_mapCard);
            MyTextView title_mapCard=(MyTextView)view_mapCard.findViewById(R.id.title_mapCard);

            String imagePath=imagePaths.get(i);

            File imgFile = new File(imagePath);
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            int maxHeight = size;
            int maxWidth = size;
            float scale = Math.min(((float)maxHeight / bitmap.getWidth()), ((float)maxWidth / bitmap.getHeight()));

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            image_mapCard.setImageBitmap(bitmap);

            if(titles.get(i).equals("")){
                LinearLayout.LayoutParams heightZero=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                heightZero.setMargins(0,0,0,20);
                title_mapCard.setLayoutParams(heightZero);
            }
            title_mapCard.setText(titles.get(i));
//            File imageFile=new File(imagePath);
//            Uri imageUri = FileProvider7.getUriForFile(mActivityContext, imageFile);
//            image_mapCard.setImageURI(imageUri);
//            RequestOptions requestOptions = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
//            Glide.with(view_mapCard).load(imagePath).apply(requestOptions).into(image_mapCard);
            BitmapDescriptor view_bitmap = BitmapDescriptorFactory.fromView(view_mapCard);

            OverlayOptions view_options = new MarkerOptions()
                    .position(point)
                    .icon(view_bitmap)
                    .alpha(alpha);
//            OverlayOptions view_options = new MarkerOptions()
//                    .position(point)
//                    .icon(view_bitmap);
            Marker viewMarker=(Marker) mBaiduMap.addOverlay(view_options);
            viewMarker.setAnchor(0.11f,0.29f);

//            Bitmap icon_bitmap=BitmapFactory.decodeResource(mActivityContext.getResources(),R.drawable.bear);
//            BitmapDescriptor icon_bitmapDescriptor = (BitmapDescriptor) BitmapDescriptorFactory.fromBitmap(icon_bitmap);
//            viewMarker.setIcon(icon_bitmapDescriptor);
            Bundle bundle=new Bundle();
            bundle.putString("imagePath",imagePath);
//            bundle.putString("latitudeAndLongtitudeString",pointsLocation.get(i));
            viewMarker.setExtraInfo(bundle);
            imageMarkers.add(viewMarker);

            /*
            //*********image 方法1*********
            String imagePath=imagePaths.get(i);
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(imagePath);

            } catch (IOException e) {
                Log.d("thumbnail","不能获取缩略图");
            }
            byte[] imageData=exif.getThumbnail();
            if(imageData!=null){
                Bitmap thumbnail = BitmapFactory.decodeByteArray(imageData,0,imageData.length);
                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(thumbnail);
                OverlayOptions imageOption = new MarkerOptions()
                        .position(point)
                        .icon(bitmap)
                        .alpha(0.5f);
                Marker imageMarker=(Marker) mBaiduMap.addOverlay(imageOption);
                Bundle bundle1=new Bundle();
                bundle1.putString("imagePath",imagePath);
                imageMarker.setExtraInfo(bundle1);
                imageMarkers.add(imageMarker);
            }

             */

            /*
            //*********image 方法2*********
            String imagePath=imagePaths.get(i);
//            Bitmap thumbnail = BitmapFactory.decodeByteArray(imageData,0,imageData.length);
//            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(thumbnail);
            File imgFile = new File(imagePath);
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            int maxHeight = 480;
            int maxWidth = 480;
            float scale = Math.min(((float)maxHeight / bitmap.getWidth()), ((float)maxWidth / bitmap.getHeight()));

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

//            bitmap = Bitmap.createScaledBitmap(bitmap, 480, 480, false);
//            ViewGroup.LayoutParams iv_layoutParams=new ViewGroup.LayoutParams(
//                    200,200
//            );
//            ImageView imageView = new ImageView(mActivityContext);
//            imageView.setImageBitmap(bitmap);
//            ll_images.addView(imageView,iv_layoutParams);

//            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath,bmOptions);
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);

            OverlayOptions imageOption = new MarkerOptions()
                    .position(point)
                    .icon(bitmapDescriptor)
                    .alpha(0.5f);
            Marker imageMarker=(Marker) mBaiduMap.addOverlay(imageOption);
            imageMarker.setAnchor(-1,-1);
            Bundle bundle=new Bundle();
            bundle.putString("imagePath",imagePath);
            imageMarker.setExtraInfo(bundle);
            imageMarkers.add(imageMarker);

            //*********icon*********
            BitmapDescriptor iconBitmap = BitmapDescriptorFactory.fromResource(R.drawable.marker);
            MarkerOptions iconMarkerOption = new MarkerOptions()
                    .position(point) //必传参数
                    .icon(iconBitmap) //必传参数
                    .alpha(1f);
            iconMarkerOption=iconMarkerOption.anchor(1f,0f);
            Log.d("mapFragment_lifeCycle",iconMarkerOption.getAnchorX()+","+iconMarkerOption.getAnchorY());
            OverlayOptions iconOption=(OverlayOptions)iconMarkerOption;
            Marker iconMarker=(Marker) mBaiduMap.addOverlay(iconOption);
            */

        }

        BaiduMap.OnMarkerClickListener markerClicklistener = new BaiduMap.OnMarkerClickListener() {
            /**
             * 地图 Marker 覆盖物点击事件监听函数
             * @param marker 被点击的 marker
             */
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onMarkerClick(Marker marker) {
                //在地图里定位图片/聚焦，MyApplication.
//                MyApplication.ZOOM=mBaiduMap.getMapStatus().zoom;
                MyApplication.ZOOM=mBaiduMap.getGLMapView().getZoomLevel();
                MyApplication.MapLocation=mBaiduMap.getMapStatus().target;
//                LatLng l=mBaiduMap.getMapStatus().target;
//                float z=mBaiduMap.getMapStatus().zoom;
//                Log.d("getMapCenter"," \nz:"+z+"\nl1:"+l.latitude+", l2:"+l.longitude);
                String newImageClicked=(String) marker.getExtraInfo().get("imagePath");
//                String latitudeAndLongtitudeString=(String) marker.getExtraInfo().get("latitudeAndLongtitudeString");
//                MyApplication.latitudeAndLongtitudeString=latitudeAndLongtitudeString;
//                MyApplication.ZOOM=mBaiduMap.getGLMapView().getZoomLevel();
                if(imageClicked.equals(newImageClicked)){
                    MyApplication.STATE=4;
                    Intent intent=new Intent(mActivityContext,PhotoPortrait.class);
                    intent.putExtra("ImagePath",newImageClicked);
                    startActivity(intent);
//                    Toast.makeText(mActivityContext,"走！",Toast.LENGTH_SHORT).show();
                }else {
                    imageClicked=newImageClicked;
                    for(Marker imageMarker:imageMarkers){
                        if(imageMarker!=marker){
                            imageMarker.setAlpha(alpha);
                        }
                    }
                }

                AlphaAnimation anim=new AlphaAnimation(0.6f,1.0f);
                anim.setDuration(duration);
                marker.setAnimation(anim);
                marker.startAnimation();

//                Toast.makeText(mActivityContext,"mapView:"+text,Toast.LENGTH_SHORT).show();

                return false;
            }


        };

        BaiduMap.OnMapStatusChangeListener statusChangeListener = new BaiduMap.OnMapStatusChangeListener() {

            //手势操作地图，设置地图状态等操作导致地图状态开始改变。
            //param status 地图状态改变开始时的地图状态
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            //param status 地图状态改变开始时的地图状态
            //地图状态改变的原因int
            //1 (REASON_GESTURE):用户手势触发导致的地图状态改变,比如双击、拖拽、滑动底图
            //2 (REASON_API_ANIMATION)SDK导致的地图状态改变, 比如点击缩放控件、指南针图标
            //3 (REASON_DEVELOPER_ANIMATION)开发者调用,导致的地图状态改变
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
                if(i==REASON_GESTURE){
                    Log.d("mapFragment_lifeCycle", " \nStatusChangeStart:"+mapStatus.zoom);
                    //5的时候，可以显示照片了。
                    //放大：mapStatus.zoom值变大(float)
                }
            }

            //地图状态变化中
            //param status 当前地图状态
            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
//                Log.d("mapFragment_lifeCycle", "StatusChange");
            }

            //地图状态改变结束
            //param status 地图状态改变结束后的地图状态
            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {

            }

        };

        //设置地图状态监听
        mBaiduMap.setOnMarkerClickListener(markerClicklistener);
        mBaiduMap.setOnMapStatusChangeListener(statusChangeListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("mapFragment_lifeCycle: ","onPause");
        //在activity执行onPause时必须调用mMapView. onPause ()
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        Log.d("mapFragment_lifeCycle: ","onDestroy");
        super.onDestroy();
        //在activity执行onDestroy时必须调用mMapView.onDestroy()
        mapView.onDestroy();
    }

    private void getLocationInfoFromDB(){
        dbHelper=new MyDatabaseHelper(mActivityContext,MyDatabaseHelper.DATABASE_NAME,null,MyDatabaseHelper.DATABASE_VERSION);
        dbHelper.getReadableDatabase();

        /*
        pointsLocation.add("39.944251,116.494996");
        pointsLocation.add("38.898556,-77.037852");
        pointsLocation.add("39.86923,116.397428");
        for (String locationString:pointsLocation){
            String[] splits = locationString.split(",");
            double latitde=Double.parseDouble(splits[0]);
            double longtitude=Double.parseDouble(splits[1]);
            LatLng point = new LatLng(latitde, longtitude);
            points.add(point);
        }
        imagePaths.add("/storage/emulated/0/DCIM/Camera/IMG_20220515_223434.jpg");
        imagePaths.add("/storage/emulated/0/Pictures/WeiXin/mmexport1651978661356.jpg");
        imagePaths.add("/storage/emulated/0/DCIM/Camera/IMG_20220514_184917.jpg");

        titles.add("和一家人真开心！");
        titles.add("我是全世界最开心的人！");
        titles.add("今天的茄子是世界上最美味的大餐噢:)");

         */

//        SQLiteDatabase db=dbHelper.getWritableDatabase();
//        ContentValues values=new ContentValues();
//        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LABELTEXT, texts[i]);
//        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LABELCOLOR, colors[i]);
//        db.insert(FeedReaderContract.Feed
//        Entry.LABEL_TABLE_NAME,null,values);

        ContentValues values=new ContentValues();

        int i=1;
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String[] columns={
                FeedReaderContract.FeedEntry.COLUMN_NAME_LATITUDEANDLONGITUDE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_ISCOUNTRYEDITABLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION,
                FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGEPATH};
        Cursor cursor=db.query(FeedReaderContract.FeedEntry.TABLE_NAME, columns,null,null,null,null,null);
        Log.d("mapDB","cursor.getCount: "+cursor.getCount());
        if(cursor.moveToFirst()){//moveToFirst
            do{
                String imagePath = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_IMAGEPATH));
                String title = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE));
//                String locationText=cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LOCATION));
                String latitudeAndLongitude= cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LATITUDEANDLONGITUDE));
//                int isCountryEditable= cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_ISCOUNTRYEDITABLE));
                if(!latitudeAndLongitude.equals("")){
                    pointsLocation.add(latitudeAndLongitude);
                    titles.add(title);
                    imagePaths.add(imagePath);
                }

                Log.d("mapDB"," \n"+i+")"+"\ntitle: "+title+"\nlatitudeAndLongitude:"+latitudeAndLongitude+"\nimagePath"+imagePath);
                i++;
            }while (cursor.moveToNext());
        }
        cursor.close();
        for (String locationString:pointsLocation){
            String[] splits = locationString.split(",");
            double latitde=Double.parseDouble(splits[0]);
            double longtitude=Double.parseDouble(splits[1]);
            LatLng point = new LatLng(latitde, longtitude);
            points.add(point);
        }


    }
}
