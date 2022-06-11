package com.example.lbwapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.SupportMapFragment;


public class MapTest extends FragmentActivity {

//    private MapView mMapView=null;
//    private Fragment fragment_map_test;
    private MapView mapView=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mMapView = new MapView(this);
//        setContentView(mMapView);
        setContentView(R.layout.activity_map_test);
        Button bt_map_test=(Button) findViewById(R.id.bt_map_test);
        bt_map_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager=getSupportFragmentManager();
                FragmentTransaction transaction=fragmentManager.beginTransaction();
//                BaiduMapOptions mapOptions=new BaiduMapOptions();
//                //获取到SupportMapFragment实例
//                SupportMapFragment mSupportMapFragment = SupportMapFragment.newInstance(mapOptions);
//                Fragment mapFragment=(Fragment) mSupportMapFragment;
                transaction.replace(R.id.fragment_map_test,new MapFragment());
//                transaction.addToBackStack(null);
                transaction.commit();

//                mapView=findViewById(R.id.mapView);



//                manager.beginTransaction().replace(R.id.map_container, mapFragment).commit();
            }
        });

//        if(mapView!=null){
//            BaiduMap mBaiduMap=mapView.getMap();
//
//            Toast.makeText(this,"mapView",Toast.LENGTH_SHORT).show();
//            BaiduMap.OnMarkerClickListener listener = new BaiduMap.OnMarkerClickListener() {
//                /**
//                 * 地图 Marker 覆盖物点击事件监听函数
//                 * @param marker 被点击的 marker
//                 */
//                @Override
//                public boolean onMarkerClick(Marker marker) {
//
//                    return false;
//                }
//
//
//            };
//
//            //隐藏地图标注，只显示道路信息
//            // 默认显示地图标注
//            mBaiduMap.showMapPoi(false);
//
//            // 设置地图 Marker 覆盖物点击事件监听者,自3.4.0版本起可设置多个监听对象，停止监听时调用removeMarkerClickListener移除监听对象
//            mBaiduMap.setOnMarkerClickListener(listener);
//            //停止监听时移除监听对象
////            mBaiduMap.removeMarkerClickListener(listener);
//        }else {
//            Toast.makeText(this,"no",Toast.LENGTH_SHORT).show();
//        }

        /*
        BaiduMapOptions options = new BaiduMapOptions();
        //设置地图模式为卫星地图
        options.mapType(BaiduMap.MAP_TYPE_SATELLITE);
        mMapView = new MapView(this,options);
         */

        /*
        //可以通过如下代码设置地图的缩放级别：
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(18.0f);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
         */

//        mMapView = new MapView(this);
//        ViewGroup.LayoutParams mv_layoutParams=new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//        );
//        mapContainer.addView(mMapView,mv_layoutParams);

    }

    /*
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

     */
}