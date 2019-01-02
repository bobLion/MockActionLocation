package com.bob.android.mockactionlocation.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.bob.android.mockactionlocation.R;
import com.bob.android.mockactionlocation.base.BaseActivity;
import com.bob.android.mockactionlocation.config.AppConfig;
import com.bob.android.mockactionlocation.dialog.SearchPointDialog;
import com.bob.android.mockactionlocation.map.overlay.util.MapLatlnUtil;
import com.bob.android.mockactionlocation.service.MockGpsService;
import com.bob.android.mockactionlocation.toast.rxtools.RxToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bob.android.mockactionlocation.service.MockGpsService.RunCode;
import static com.bob.android.mockactionlocation.service.MockGpsService.StopCode;

public class MainActivity extends BaseActivity implements LocationSource,
        AMapLocationListener, PoiSearch.OnPoiSearchListener, DistrictSearch.OnDistrictSearchListener,
        SearchPointDialog.OnPointSelectedListener {

    @BindView(R.id.map_main)
    MapView mMapView;
    @BindView(R.id.edt_search)
    EditText mEdtSearch;
    @BindView(R.id.btn_search)
    Button mBtnSearch;
    @BindView(R.id.img_search)
    ImageView imgSearch;
    @BindView(R.id.fab_start)
    FloatingActionButton fabStart;
    @BindView(R.id.fabStop)
    FloatingActionButton fabStop;

    private Context mContext;
    private AMap aMap;
    private UiSettings uiSettings;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private AMapLocation mCunrrentLocation;
    private boolean firstLocation = true;

    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);

    private PoiSearch.Query mPointQuery;
    private PoiSearch mPoiSearch;


    //位置欺骗相关
    //  latLngInfo  经度&纬度
    public static String latLngInfo ;

    private boolean isMockLocOpen = false;
    private boolean isServiceRun = false;
    private boolean isMockServStart = false;
    private boolean isMockLocationStart = true;


    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;

    private DistrictSearch districtSearch;
    private DistrictSearchQuery districtSearchQuery;

    public static final int RESULT_CODE_KEYWORDS = 102;
    public static final int RESULT_CODE_INPUTTIPS = 101;

    private Marker mPoiMarker;
    private LatLonPoint terminalPoint;
    public static BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_gcoding);


    public static LatLng currentPt;


    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (18 == msg.what) {
                PolylineOptions options = (PolylineOptions) msg.obj;
                aMap.addPolyline(options);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        mMapView.onCreate(savedInstanceState);
        setUpMap();

        // 是否开启位置模拟
        isMockLocOpen = isAllowMockLocation();
        districtSearch = new DistrictSearch(mContext);
        districtSearch.setOnDistrictSearchListener(this);

        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                updateMapState(latLng);
            }
        });
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        if (uiSettings == null) {
            uiSettings = aMap.getUiSettings();
        }
        uiSettings.setAllGesturesEnabled(true);
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.showBuildings(true);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        setupLocationStyle();
    }


    private void setupLocationStyle() {
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.mipmap.gps_point));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(5);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(FILL_COLOR);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setGpsFirst(true);
            //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。默认连续定位 切最低时间间隔为1000ms
            mLocationOption.setInterval(3500);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }


    @OnClick(R.id.img_search)
    void clickSearch(View view) {
       /* SearchPointDialog dialog = new SearchPointDialog(this, R.style.MyDialog);
        dialog.setOnPointSelectedListener(this);
        dialog.show();*/
       Intent intent = new Intent(mContext,CalculateRouteActivity.class);
       intent.putExtra("latLongPointLatitude",mCunrrentLocation.getLatitude());
       intent.putExtra("latLongPointLongitude",mCunrrentLocation.getLongitude());
       if(null != terminalPoint){
           intent.putExtra("terminalPointLatitude",terminalPoint.getLatitude());
           intent.putExtra("terminalPointLongitude",terminalPoint.getLongitude());
       }else{
           RxToast.error(mContext,"未指定终点！").show();
           return;
       }
       startActivity(intent);
    }

    @OnClick(R.id.walk_route)
    void walkRouteClick(View view){
        Intent intent = new Intent(mContext,WalkNavigationActivity.class);
        intent.putExtra("latLongPointLatitude",mCunrrentLocation.getLatitude());
        intent.putExtra("latLongPointLongitude",mCunrrentLocation.getLongitude());
        if(null != terminalPoint){
            intent.putExtra("terminalPointLatitude",terminalPoint.getLatitude());
            intent.putExtra("terminalPointLongitude",terminalPoint.getLongitude());
        }else{
            RxToast.error(mContext,"未指定终点！").show();
            return;
        }
        startActivity(intent);
    }

    @OnClick(R.id.rl_query_location)
    void locationClick(View view) {
        if (mCunrrentLocation != null) {
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCunrrentLocation.getLatitude(),
                    mCunrrentLocation.getLongitude()), 15));
            currentPt = new LatLng(mCunrrentLocation.getLatitude(), mCunrrentLocation.getLongitude());
        } else {
            RxToast.error(mContext, getString(R.string.tip_location_fail)).show();
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                mCunrrentLocation = aMapLocation;
                if (firstLocation) {
                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCunrrentLocation.getLatitude(), mCunrrentLocation.getLongitude()), 15));
                    firstLocation = false;
                }
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("onLocationChanged", errText);
            }
        }
    }

    /**
     * 点击搜索按钮找出地点并将点标注在地图上
     * @param poiItems
     */
    private void initBayonetInfoData(List<PoiItem> poiItems) {
//        upDateView(mPersonTrackEntityList.get(0));
        for (int i = 0; i < poiItems.size(); i++) {
            PoiItem mPoiItem = poiItems.get(i);
            MarkerOptions markerOption;
            View view = View.inflate(this, R.layout.layout_view_marker, null);
            TextView tvTime = (TextView) view.findViewById(R.id.tv_time);
            TextView tvPlace = (TextView) view.findViewById(R.id.tv_place);
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_pic);
            tvTime.setText(i + 1 + "");
            tvPlace.setText("地点");
//            tvTime.setText("" + mVehicleTrackEntity.getTime());
//            tvPlace.setText("" + mVehicleTrackEntity.getLocaltion());
            imageView.setImageResource(R.mipmap.amap_man);
            Bitmap bitmap = convertViewToBitmap(view);
            markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                    .position(new LatLng(mPoiItem.getLatLonPoint().getLatitude(), mPoiItem.getLatLonPoint().getLongitude()))
                    .snippet(mPoiItem.getSnippet())
                    .title(mPoiItem.getTitle())
                    .visible(true)
                    .setFlat(false)
//                    .anchor( 30,30)
                    .draggable(true);
            Marker marker = aMap.addMarker(markerOption);
            marker.setObject(mPoiItem);
            marker.setAnchor(0.5f, 0.5f);
            Animation animation = new ScaleAnimation(0, 1, 0, 1);
            animation.setInterpolator(new LinearInterpolator());
            //整个移动所需要的时间
            animation.setDuration(1000);
            //设置动画
            marker.setAnimation(animation);
            //开始动画
            marker.startAnimation();
        }
    }

    //view 转bitmap
    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    @Override
    public void onDistrictSearched(DistrictResult districtResult) {
        if (districtResult != null && districtResult.getDistrict() != null) {
            if (districtResult.getAMapException().getErrorCode() == AMapException.CODE_AMAP_SUCCESS) {
                ArrayList<DistrictItem> district = districtResult.getDistrict();
                if (district != null && district.size() > 0) {
                    //adcode 440106
                    //获取对应的行政区划的item
                   /* DistrictItem districtItem = getDistrictItem(district, fromLocation.county.getId());
                    if (districtItem == null) {
                        return;
                    }
                    //创建划线子线程
                    fromRunnable = new PolygonRunnable(districtItem, handler);
                    fromRunnable.run();*/
                    //线程池执行
//                    fromRunnable.execute(fromRunnable);
                }
            }
        }
    }

    @OnClick({R.id.fab_start, R.id.fabStop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab_start:
                if (!(isMockLocOpen = isAllowMockLocation())) {
                    setDialog();
                } else {
                    if(null != currentPt){
                        if (!isMockServStart && !isServiceRun) {
                            Log.d("DEBUG", "current pt is " + mCurrentLon + "  " + mCurrentLat);
                            LatLng pLatLng = new LatLng(currentPt.latitude,currentPt.longitude);
                            updateMapState(pLatLng);
                            //start mock location service
                            Intent mockLocServiceIntent = new Intent(MainActivity.this, MockGpsService.class);
                            latLngInfo =pLatLng.longitude+ "&"+ pLatLng.latitude ;
                            mockLocServiceIntent.putExtra("key", latLngInfo);
                            if (Build.VERSION.SDK_INT >= 26) {
                                startForegroundService(mockLocServiceIntent);
                                Log.d("DEBUG", "startForegroundService");
                            } else {
                                startService(mockLocServiceIntent);
                                Log.d("DEBUG", "startService");
                            }
                            isMockServStart = true;
                            RxToast.info(mContext,"位置模拟已开启").show();
                            fabStart.setVisibility(View.INVISIBLE);
                            fabStop.setVisibility(View.VISIBLE);
                        } else {
                            RxToast.info(mContext,"位置模拟已终止").show();
                            fabStart.setVisibility(View.INVISIBLE);
                            fabStop.setVisibility(View.VISIBLE);
                            isMockServStart = true;
                        }
                    }else{
                        RxToast.warning(mContext,"请先选择模拟定位地点！").show();
                        return;
                    }

                }
                break;
            case R.id.fabStop:
                if (isMockServStart) {
                    //end mock location
                    Intent mockLocServiceIntent = new Intent(MainActivity.this, MockGpsService.class);
                    stopService(mockLocServiceIntent);
                    RxToast.info(mContext,"位置模拟服务终止").show();
                    //service finish
                    isMockServStart = false;
                    fabStart.setVisibility(View.VISIBLE);
                    fabStop.setVisibility(View.INVISIBLE);
                    //重新定位
                    mlocationClient.stopLocation();
                    mlocationClient.startLocation();
                }
                break;
        }
    }

    //提醒开启位置模拟的弹框
    private void setDialog() {
        new AlertDialog.Builder(this)
                .setTitle("启用位置模拟")//这里是表头的内容
                .setMessage("请在开发者选项->选择模拟位置信息应用中进行设置")//这里是中间显示的具体信息
                .setPositiveButton("设置",//这个string是设置左边按钮的文字
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
                                    startActivity(intent);
                                }catch (Exception e){
                                    RxToast.warning(mContext,"无法跳转到开发者选项,请先确保您的设备已处于开发者模式").show();
                                    e.printStackTrace();
                                }
                            }
                        })//setPositiveButton里面的onClick执行的是左边按钮
                .setNegativeButton("取消",//这个string是设置右边按钮的文字
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })//setNegativeButton里面的onClick执行的是右边的按钮的操作
                .show();
    }


    //更新地图状态显示面板
    private void updateMapState(LatLng latLng) {
        Log.d("DEBUG", "updateMapState");
        if(null != latLng){
            currentPt = latLng;
            MarkerOptions ooA = new MarkerOptions().position(currentPt).icon(bdA);
            mCunrrentLocation.setLatitude(currentPt.latitude);
            mCunrrentLocation.setLongitude(currentPt.longitude);
            aMap.clear();
            aMap.addMarker(ooA);
        }
    }

    /**
     * 输入提示activity选择结果后的处理逻辑
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CODE_INPUTTIPS && data != null) {
            aMap.clear();
            Tip tip = data.getParcelableExtra(AppConfig.EXTRA_TIP);
            if (tip.getPoiID() == null || tip.getPoiID().equals("")) {
                doSearchQuery(tip.getName());
            } else {
                addTipMarker(tip);
            }
            mEdtSearch.setText(tip.getName());
            if(!tip.getName().equals("")){
//                mCleanKeyWords.setVisibility(View.VISIBLE);
            }
        } else if (resultCode == RESULT_CODE_KEYWORDS && data != null) {
            aMap.clear();
            String keywords = data.getStringExtra(AppConfig.MAP_KEY_WORD);
            if(keywords != null && !keywords.equals("")){
                doSearchQuery(keywords);
            }
            mEdtSearch.setText(keywords);
            if(!keywords.equals("")){
//                mCleanKeyWords.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(String keywords) {
//        showProgressDialog();// 显示进度框
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        mPointQuery = new PoiSearch.Query(keywords, "", AppConfig.DEFAULT_CITY);
        // 设置每页最多返回多少条poiitem
        mPointQuery.setPageSize(10);
        // 设置查第一页
        mPointQuery.setPageNum(0);

        mPoiSearch = new PoiSearch(this, mPointQuery);
        mPoiSearch.setOnPoiSearchListener(this);
        mPoiSearch.searchPOIAsyn();
    }

    /**
     * 用marker展示输入提示list选中数据
     *
     * @param tip
     */
    private void addTipMarker(Tip tip) {
        if (tip == null) {
            return;
        }
        mPoiMarker = aMap.addMarker(new MarkerOptions());
        terminalPoint = tip.getPoint();
        if (terminalPoint != null) {
            LatLng markerPosition = new LatLng(terminalPoint.getLatitude(), terminalPoint.getLongitude());
            mPoiMarker.setPosition(markerPosition);
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 17));
        }
        mPoiMarker.setTitle(tip.getName());
        mPoiMarker.setSnippet(tip.getAddress());
    }

    private class PolygonRunnable implements Runnable {
        private DistrictItem districtItem;
        private Handler handler;
        private boolean isCancel = false;

        /**
         * districtBoundary()
         * 以字符串数组形式返回行政区划边界值。
         * 字符串拆分规则： 经纬度，经度和纬度之间用","分隔，坐标点之间用";"分隔。
         * 例如：116.076498,40.115153;116.076603,40.115071;116.076333,40.115257;116.076498,40.115153。
         * 字符串数组由来： 如果行政区包括的是群岛，则坐标点是各个岛屿的边界，各个岛屿之间的经纬度使用"|"分隔。
         * 一个字符串数组可包含多个封闭区域，一个字符串表示一个封闭区域
         */
        public PolygonRunnable(DistrictItem districtItem, Handler handler) {
            this.districtItem = districtItem;
            this.handler = handler;
        }

        public void cancel() {
            isCancel = true;
        }

        /**
         * Starts executing the active part of the class' code. This method is
         * called when a thread is started that has been created with a class which
         * implements {@code Runnable}.
         */
        @Override
        public void run() {
            if (!isCancel) {
                try {
                    String[] boundary = districtItem.districtBoundary();
                    if (boundary != null && boundary.length > 0) {
                        for (String b : boundary) {
                            if (!b.contains("|")) {
                                String[] split = b.split(";");
                                PolylineOptions polylineOptions = new PolylineOptions();
                                boolean isFirst = true;
                                LatLng firstLatLng = null;
                                for (String s : split) {
                                    String[] ll = s.split(",");
                                    if (isFirst) {
                                        isFirst = false;
                                        firstLatLng = new LatLng(Double.parseDouble(ll[1]), Double.parseDouble(ll[0]));
                                    }
                                    polylineOptions.add(new LatLng(Double.parseDouble(ll[1]), Double.parseDouble(ll[0])));
                                }
                                if (firstLatLng != null) {
                                    polylineOptions.add(firstLatLng);
                                }
                                polylineOptions.width(10).color(Color.BLUE).setDottedLine(true);
                                Message message = handler.obtainMessage();
                                message.what = 18;
                                message.obj = polylineOptions;
                                handler.sendMessage(message);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void selectedPoint(String selectStr) {
        if (null != selectStr) {
            districtSearchQuery = new DistrictSearchQuery();
            districtSearchQuery.setKeywords(selectStr);
            districtSearchQuery.setShowBoundary(true);
            districtSearchQuery.setShowChild(false);
            districtSearch.setQuery(districtSearchQuery);
            districtSearch.searchDistrictAsyn();
        }
    }


    @OnClick(R.id.edt_search)
    void searchClick(View view){
        Intent intent = new Intent(mContext,InputTipActivity.class);
        startActivityForResult(intent,RESULT_CODE_INPUTTIPS);

    }
    public class MockServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int statusCode;
            Bundle bundle = intent.getExtras();
            assert bundle != null;
            statusCode = bundle.getInt("statusCode");
            Log.d("DEBUG", statusCode + "");
            if (statusCode == RunCode) {
                isServiceRun = true;
            } else if (statusCode == StopCode) {
                isServiceRun = false;
            }
        }
    }


    //模拟位置权限是否开启
    public boolean isAllowMockLocation() {
        boolean canMockPosition = false;
        if (Build.VERSION.SDK_INT <= 22) {//6.0以下
            canMockPosition = Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0;
        } else {
            try {
                LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);//获得LocationManager引用
                String providerStr = LocationManager.GPS_PROVIDER;
                LocationProvider provider = locationManager.getProvider(providerStr);
                if (provider != null) {
                    locationManager.addTestProvider(
                            provider.getName()
                            , provider.requiresNetwork()
                            , provider.requiresSatellite()
                            , provider.requiresCell()
                            , provider.hasMonetaryCost()
                            , provider.supportsAltitude()
                            , provider.supportsSpeed()
                            , provider.supportsBearing()
                            , provider.getPowerRequirement()
                            , provider.getAccuracy());
                } else {
                    locationManager.addTestProvider(
                            providerStr
                            , true, true, false, false, true, true, true
                            , Criteria.POWER_HIGH, Criteria.ACCURACY_FINE);
                }
                locationManager.setTestProviderEnabled(providerStr, true);
                locationManager.setTestProviderStatus(providerStr, LocationProvider.AVAILABLE, null, System.currentTimeMillis());
                // 模拟位置可用
                canMockPosition = true;
                locationManager.setTestProviderEnabled(providerStr, false);
                locationManager.removeTestProvider(providerStr);
            } catch (SecurityException e) {
                canMockPosition = false;
            }
        }
        return canMockPosition;
    }


    /*private void upDateView(YRZTPersonTrackEntity mYRZTPersonTrackEntity) {
        mUrl = mYRZTPersonTrackEntity.getPicture_uri();
        ImageUtil.loadImage(mContext, ivPerson, mYRZTPersonTrackEntity.getFace_image_uri());
        String mSimilarity=mYRZTPersonTrackEntity.getSimilarity()+"";
        if (!StringUtils.isEmpty(mSimilarity)&&mSimilarity.length()>6){
            mSimilarity=mSimilarity.substring(0,5);
        }
        BindValueUtils.bindTextValue(tvSimilarity, mSimilarity+ "%");
//        BindValueUtils.bindTextValue(tvIdentityCode, identityCode);
        BindValueUtils.bindTextValue(tvTime, mYRZTPersonTrackEntity.getStime());
        BindValueUtils.bindTextValue(tvLocation, mYRZTPersonTrackEntity.getCameraName());
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
        if (null != mlocationClient) {
            mlocationClient.stopLocation();
        }
        //deactivate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
        if (null != mlocationClient) {
            mlocationClient.startLocation();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        deactivate();

    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        List<PoiItem> poiItems = poiResult.getPois();
        aMap.clear();
        initBayonetInfoData(poiItems);
    }


    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @OnClick(R.id.btn_search)
    public void onSearchClick(View view) {
        String keyWord = mEdtSearch.getText().toString().trim();
        mPointQuery = new PoiSearch.Query(keyWord, "", "190402");
        mPointQuery.setPageSize(20);// 设置每页最多返回多少条poiitem
        mPoiSearch = new PoiSearch(this, mPointQuery);
        mPoiSearch.setOnPoiSearchListener(this);
        mPoiSearch.searchPOIAsyn();
    }

}
