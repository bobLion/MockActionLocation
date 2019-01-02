package com.bob.android.mockactionlocation.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.bob.android.mockactionlocation.R;
import com.bob.android.mockactionlocation.base.BaseActivity;
import com.bob.android.mockactionlocation.map.overlay.WalkRouteOverlay;
import com.bob.android.mockactionlocation.toast.rxtools.RxToast;
import com.bob.android.mockactionlocation.map.overlay.util.AMapUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalkNavigationActivity extends BaseActivity implements AMap.OnMapClickListener,
        AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter, RouteSearch.OnRouteSearchListener, AMap.OnMapLoadedListener {

    @BindView(R.id.route_drive)
    ImageView routeDrive;
    @BindView(R.id.route_bus)
    ImageView routeBus;
    @BindView(R.id.route_walk)
    ImageView routeWalk;
    @BindView(R.id.route_CrosstownBus)
    TextView routeCrosstownBus;
    @BindView(R.id.routemap_choose)
    LinearLayout routemapChoose;
    @BindView(R.id.routemap_header)
    RelativeLayout mHeadLayout;
    @BindView(R.id.firstline)
    TextView mRotueTimeDes;
    @BindView(R.id.secondline)
    TextView mRouteDetailDes;
    @BindView(R.id.detail)
    LinearLayout detail;
    @BindView(R.id.bottom_layout)
    RelativeLayout mBottomLayout;
    @BindView(R.id.route_map)
    MapView mapView;
    @BindView(R.id.bus_result_list)
    ListView busResultList;
    @BindView(R.id.bus_result)
    LinearLayout busResult;

    private AMap aMap;
    private Context mContext;
    private RouteSearch mRouteSearch;
    private WalkRouteResult mWalkRouteResult;
    private LatLonPoint mStartPoint = new LatLonPoint(39.996678, 116.479271);//起点，39.996678,116.479271
    private LatLonPoint mEndPoint = new LatLonPoint(39.997796, 116.468939);//终点，39.997796,116.468939
    private final int ROUTE_TYPE_WALK = 3;

    private ProgressDialog progDialog = null;// 搜索时进度条
    private WalkRouteOverlay walkRouteOverlay;
    private double startLatitude,startLongitude,endLatitude,endLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_navigation);
        ButterKnife.bind(this);
        mContext = this;
        startLatitude = getIntent().getDoubleExtra("latLongPointLatitude",0);
        startLongitude = getIntent().getDoubleExtra("latLongPointLongitude",0);
        endLatitude = getIntent().getDoubleExtra("terminalPointLatitude",0);
        endLongitude = getIntent().getDoubleExtra("terminalPointLongitude",0);
        if(0 != startLatitude || 0 != startLongitude
                && 0!= endLatitude || 0!= endLongitude){
            mStartPoint = new LatLonPoint(startLatitude,startLongitude);
            mEndPoint = new LatLonPoint(endLatitude,endLongitude);
        }
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
    }


    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        registerListener();
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
        mHeadLayout.setVisibility(View.GONE);
    }

    /**
     * 注册监听
     */
    private void registerListener() {
        aMap.setOnMapClickListener(WalkNavigationActivity.this);
        aMap.setOnMarkerClickListener(WalkNavigationActivity.this);
        aMap.setOnInfoWindowClickListener(WalkNavigationActivity.this);
        aMap.setInfoWindowAdapter(WalkNavigationActivity.this);
        aMap.setOnMapLoadedListener(this);

    }
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLoaded() {
        searchRouteResult(ROUTE_TYPE_WALK);
    }

    private void searchRouteResult(int routeType) {
        if (mStartPoint == null) {
            RxToast.warning(mContext, "定位中，稍后再试...").show();
            return;
        }
        if (mEndPoint == null) {
            RxToast.warning(mContext, "终点未设置").show();
        }
        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_WALK) {// 步行路径规划
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo);
            mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
        }
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        dissmissProgressDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mWalkRouteResult = result;
                    final WalkPath walkPath = mWalkRouteResult.getPaths()
                            .get(0);
                    if (walkRouteOverlay != null){
                        walkRouteOverlay.removeFromMap();
                    }
                    walkRouteOverlay = new WalkRouteOverlay(
                            this, aMap, walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos());
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
                    mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) walkPath.getDistance();
                    int dur = (int) walkPath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur)+"("+ AMapUtil.getFriendlyLength(dis)+")";
                    mRotueTimeDes.setText(des);
                    mRouteDetailDes.setVisibility(View.GONE);
                    mBottomLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext,
                                    WalkRouteDetailActivity.class);
                            intent.putExtra("walk_path", walkPath);
                            intent.putExtra("walk_result",
                                    mWalkRouteResult);
                            startActivity(intent);
                        }
                    });
                } else if (result != null && result.getPaths() == null) {
                    RxToast.info(mContext,"对不起，没有搜索到相关数据").show();
                }
            } else {
                RxToast.info(mContext, "对不起，没有搜索到相关数据").show();
            }
        } else {
            RxToast.error(mContext, String.valueOf(errorCode)).show();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }


    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
