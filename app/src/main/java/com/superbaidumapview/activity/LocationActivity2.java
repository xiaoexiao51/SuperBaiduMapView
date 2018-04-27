package com.superbaidumapview.activity;

import android.animation.AnimatorSet;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.superbaidumapview.R;
import com.superbaidumapview.RadarView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LocationActivity2 extends AppCompatActivity {

    @Bind(R.id.mapView)
    MapView mMapView;
    @Bind(R.id.tv_location)
    TextView mTvLocation;
    @Bind(R.id.radarView)
    RadarView mRadarView;
   /* @Bind(R.id.iv_bigpin)
    ImageView mIvBigpin;
    @Bind(R.id.ll_location)
    LinearLayout mLlLocation;*/

    @Bind(R.id.searchView)
    SearchView searchView;

    private BaiduMap mBaiduMap;
    private GeoCoder mGeoCoder;
    private LocationClient locationClient;
    private Boolean isFirstRequest = true;

    private double selectLat;
    private double selectLon;
    private String mAddress;
    private String mDescription;
    private AnimatorSet mAnimatorSet;
    private LatLng src_point;
    private double latitude;
    private double longitude;
    private Overlay overlay;
    private List<Overlay> overlayList = new ArrayList<>();
    private List<LatLng> latLngList = new ArrayList<>();

    /**
     * 格式化数字，保留小数点后两位
     *
     * @param value
     * @return
     */
    public String formatValue(double value) {
        DecimalFormat formatter = new DecimalFormat("#.######");
        return formatter.format(value);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location2);
        /*View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(option);*/
        /*ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }*/

        ButterKnife.bind(this);
        // 地理编码查询结果监听器
        mGeoCoder = GeoCoder.newInstance();

        // 初始化BaiduMap对象
        initMapView();
        // 声明LocationClient类
        initLocationOptions();
        EditText editText = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        editText.setHintTextColor(Color.WHITE);
        editText.setTextColor(Color.WHITE);
        /*searchView.setFocusable(true);
        searchView.requestFocus();
        searchView.requestFocusFromTouch();*/
        mRadarView.setSearching(true);
        searchView.setVisibility(View.GONE);
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setQueryHint("请输入地理坐标，并以逗号分隔");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String input = query.replaceAll("，", ",");
                String[] arr = input.split(",");
                if (arr.length < 2) {
                    Toast.makeText(getApplicationContext(), "坐标输入有误！", Toast.LENGTH_SHORT).show();
                    return false;
                }
                src_point = new LatLng(Double.parseDouble(arr[0]), Double.parseDouble(arr[1]));
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(src_point);
                mBaiduMap.animateMapStatus(update);
                searchView.postDelayed(srcLatLonRunnable, 500);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        /*Intent intent = getIntent();
        if (intent != null) {
            String src_latlon = intent.getStringExtra("src_latlon");
            String[] split = src_latlon.split(",");
            src_point = new LatLng(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(src_point);
            mBaiduMap.animateMapStatus(update);
            mLlLocation.postDelayed(srcLatLonRunnable, 500);
        }*/
    }


    private Runnable srcLatLonRunnable = new Runnable() {
        @Override
        public void run() {
            setPopupTipsInfo(src_point);
        }
    };

    private void createMaker(double latitude,double longitude) {
        if(latLngList.isEmpty()){
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_pin);
            latLngList.add(new LatLng(latitude + 0.001,longitude + 0.001));
            latLngList.add(new LatLng(latitude - 0.001,longitude + 0.001));
            latLngList.add(new LatLng(latitude - 0.001,longitude - 0.001));
            latLngList.add(new LatLng(latitude + 0.001,longitude - 0.001));
            for (LatLng latLng : latLngList) {
                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .icon(bitmap)
                        .zIndex(12)
                        .draggable(true)
                        .title(mAddress);
                overlayList.add(mBaiduMap.addOverlay(options));
            }

        }

    }

    private void initMapView() {
        mBaiduMap = mMapView.getMap();
//        mMapView.setVisibility(View.GONE);
//        mRadarView.setVisibility(View.GONE);
        mBaiduMap.setMaxAndMinZoomLevel(20, 11);
        MapStatus mapStatus = new MapStatus.Builder().zoom(18).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mBaiduMap.setMapStatus(mapStatusUpdate);
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
//                mLlLocation.setVisibility(View.GONE);
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                LatLng ptCenter = mBaiduMap.getMapStatus().target;
//                setPopupTipsInfo(ptCenter);

                //重新把当前位置设置为地图中心
                /*MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ptCenter);
                mBaiduMap.animateMapStatus(update);*/
                Point p = mBaiduMap.getProjection().toScreenLocation(mapStatus.target);
                Log.d("point", p.x + "\n" + p.y);
                List<Point> pointList = new ArrayList<>();
                for (LatLng latLng : latLngList) {
                    p = mBaiduMap.getProjection().toScreenLocation(latLng);
                    pointList.add(p);
                    Log.d("init", p.x + "\n" + p.y);
                }
                mRadarView.addPoint(pointList);
            }
        });

        mGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
//                    Toast.makeText(LocationActivity2.this, "没找到该地址", Toast.LENGTH_SHORT).show();
                } else {
                    // 获取大头针的地理位置
                    mAddress = reverseGeoCodeResult.getAddress();
                    mDescription = reverseGeoCodeResult.getSematicDescription();
                    // 获取大头针的坐标
                    LatLng location = reverseGeoCodeResult.getLocation();
                    selectLat = location.latitude;
                    selectLon = location.longitude;
                    // 显示大头针所在的信息
                    /*if (mAnimatorSet == null) {
                        mAnimatorSet = new AnimatorSet();
                    }
                    mAnimatorSet.playTogether(ObjectAnimator.ofFloat(mIvBigpin, "translationY", 0.0f, -30.0f, 0.0f),
                            ObjectAnimator.ofFloat(mIvBigpin, "rotationY", 0.0f, 720.0f));
                    mAnimatorSet.setDuration(500);
                    mAnimatorSet.start();
                    mTvLocation.setText(mDescription);*/
                    /*mLlLocation.setVisibility(View.VISIBLE);
                    mIvBigpin.setVisibility(View.VISIBLE);*/
                    if (overlay != null) {
                        overlay.remove();
                    }
                    LatLng ll_pt = new LatLng(selectLat, selectLon);
                    /*BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.radar_default_point_ico);
                    OverlayOptions options = new MarkerOptions()
                            .position(ll_pt)
                            .icon(bitmap)
                            .zIndex(12)
                            .draggable(true)
                            .title(mAddress);
                    overlay = mBaiduMap.addOverlay(options);*/
                    LatLng point = new LatLng(latitude, longitude);
                    Log.d("result", latitude + "\n" + longitude);
                    MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(point);
                    mBaiduMap.animateMapStatus(update);
                    Point p = mBaiduMap.getProjection().toScreenLocation(point);
                    Log.d("point", p.x + "\n" + p.y);
                    for (LatLng latLng : latLngList) {
                        p = mBaiduMap.getProjection().toScreenLocation(latLng);
                        Log.d("init", p.x + "\n" + p.y);
                    }
                }
            }
        });
    }

    private void initLocationOptions() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true);// 打开gps:默认不打开
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        locationClient = new LocationClient(getApplicationContext());
        locationClient.setLocOption(option);
        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null) {
                    return;
                }
               /* mLlLocation.setVisibility(View.VISIBLE);
                mIvBigpin.setVisibility(View.VISIBLE);*/
                // 第一次定位时，将地图位置移动到当前位置
                if (isFirstRequest) {
                    Log.d("NetUtil", "定位吧.......");
                    isFirstRequest = false;
//                    LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
//                    MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
//                    mBaiduMap.animateMapStatus(update);
                    latitude = bdLocation.getLatitude();
                    longitude = bdLocation.getLongitude();
                    Log.d("NetUtil", latitude + "\n" + longitude);
                    MyLocationData locData = new MyLocationData.Builder()
                            .latitude(latitude)  //纬度
                            .longitude(longitude)//经度
                            .build();
                    mBaiduMap.setMyLocationData(locData);
//                    setPopupTipsInfo(latLng);

                    src_point = new LatLng(latitude, longitude);
                    MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(src_point);
                    mBaiduMap.animateMapStatus(update);
                    /*mLlLocation.postDelayed(srcLatLonRunnable, 500);
                    mLlLocation.setVisibility(View.GONE);
                    mIvBigpin.setVisibility(View.GONE);*/

                    createMaker(latitude,longitude);

                }
            }
        });
    }

    private void setPopupTipsInfo(LatLng latLng) {
        //设置反地理编码位置坐标
        ReverseGeoCodeOption option = new ReverseGeoCodeOption();
        option.location(latLng);
        //发起反地理编码请求
        mGeoCoder.reverseGeoCode(option);
        /*mLlLocation.setVisibility(View.VISIBLE);
        mIvBigpin.setVisibility(View.VISIBLE);*/
    }

    @OnClick({R.id.tv_return, R.id.iv_location})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_return:
                Intent data = new Intent();
                data.putExtra("address", mAddress + " " + mDescription);
                data.putExtra("latLon", formatValue(selectLat) + "," + formatValue(selectLon));
                /*setResult(RESULT_OK, data);
                LocationActivity2.this.finish();*/
                break;
            case R.id.iv_location:
                isFirstRequest = true;
                LatLng point = new LatLng(latitude, longitude);
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(point);
                mBaiduMap.animateMapStatus(update);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        mBaiduMap.setMyLocationEnabled(true);
        if (!locationClient.isStarted()) {
            locationClient.start();
        }
        super.onStart();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
//        mLlLocation.removeCallbacks(srcLatLonRunnable);
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        mMapView = null;
        mBaiduMap.setMyLocationEnabled(false);
        if (locationClient.isStarted()) {
            locationClient.stop();
        }
        mGeoCoder.destroy();
        super.onDestroy();
    }
}
