package com.bob.android.mockactionlocation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.bob.android.mockactionlocation.R;
import com.bob.android.mockactionlocation.base.BaseActivity;
import com.bob.android.mockactionlocation.entity.StrategyBean;
import com.bob.android.mockactionlocation.map.overlay.util.NavigationUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalculateRouteActivity extends BaseActivity implements AMapNaviListener, View.OnClickListener, AMap.OnMapLoadedListener {


    @BindView(R.id.calculate_route_start_navi)
    Button calculateRouteStartNavi;
    @BindView(R.id.calculate_route_navi_overview)
    TextView calculateRouteNaviOverview;
    @BindView(R.id.route_line_one_view)
    View routeLineOneView;
    @BindView(R.id.route_line_one_strategy)
    TextView routeLineOneStrategy;
    @BindView(R.id.route_line_one_time)
    TextView routeLineOneTime;
    @BindView(R.id.route_line_one_distance)
    TextView routeLineOneDistance;
    @BindView(R.id.route_line_one)
    LinearLayout routeLineOne;
    @BindView(R.id.route_line_two_view)
    View routeLineTwoView;
    @BindView(R.id.route_line_two_strategy)
    TextView routeLineTwoStrategy;
    @BindView(R.id.route_line_two_time)
    TextView routeLineTwoTime;
    @BindView(R.id.route_line_two_distance)
    TextView routeLineTwoDistance;
    @BindView(R.id.route_line_two)
    LinearLayout routeLineTwo;
    @BindView(R.id.route_line_three_view)
    View routeLineThreeView;
    @BindView(R.id.route_line_three_strategy)
    TextView routeLineThreeStrategy;
    @BindView(R.id.route_line_three_time)
    TextView routeLineThreeTime;
    @BindView(R.id.route_line_three_distance)
    TextView routeLineThreeDistance;
    @BindView(R.id.route_line_three)
    LinearLayout routeLineThree;
    @BindView(R.id.calculate_route_strategy_tab)
    LinearLayout calculateRouteStrategyTab;
    @BindView(R.id.navi_view)
    MapView naviView;
    @BindView(R.id.strategy_choose)
    ImageView strategyChoose;
    @BindView(R.id.map_traffic)
    ImageView mapTraffic;
    private StrategyBean mStrategyBean;
    private static final float ROUTE_UNSELECTED_TRANSPARENCY = 0.3F;
    private static final float ROUTE_SELECTED_TRANSPARENCY = 1F;


    /**
     * 导航对象(单例)
     */
    private AMapNavi mAMapNavi;

    private AMap mAMap;
    private AMapLocation mLatLongPoint;
    private NaviLatLng endLatlng = new NaviLatLng(39.90759, 116.392582);
    private NaviLatLng startLatlng = new NaviLatLng(39.993537, 116.472875);
    private List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    private double startLatitude,startLongitude,endLatitude,endLongitude;

    /**
     * 途径点坐标集合
     */
    private List<NaviLatLng> wayList = new ArrayList<NaviLatLng>();
    /**
     * 终点坐标集合［建议就一个终点］
     */
    private List<NaviLatLng> endList = new ArrayList<NaviLatLng>();
    /**
     * 保存当前算好的路线
     */
    private SparseArray<RouteOverLay> routeOverlays = new SparseArray<RouteOverLay>();
    /*
     * strategyFlag转换出来的值都对应PathPlanningStrategy常量，用户也可以直接传入PathPlanningStrategy常量进行算路。
     * 如:mAMapNavi.calculateDriveRoute(mStartList, mEndList, mWayPointList,PathPlanningStrategy.DRIVING_DEFAULT);
     */
    int strategyFlag = 0;


    private int routeID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_route);
        ButterKnife.bind(this);
        mContext = this;
        init();
        startLatitude = getIntent().getDoubleExtra("latLongPointLatitude",0);
        startLongitude = getIntent().getDoubleExtra("latLongPointLongitude",0);
        endLatitude = getIntent().getDoubleExtra("terminalPointLatitude",0);
        endLongitude = getIntent().getDoubleExtra("terminalPointLongitude",0);
        if(0 != startLatitude || 0 != startLongitude
                && 0!= endLatitude || 0!= endLongitude){
            startLatlng = new NaviLatLng(startLatitude,startLongitude);
            endLatlng = new NaviLatLng(endLatitude,endLongitude);
        }
        naviView.onCreate(savedInstanceState);
        initNavi();
        calculateDriveRoute();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (mAMap == null) {
            mAMap = naviView.getMap();
            mAMap.setTrafficEnabled(false);
            mAMap.setOnMapLoadedListener(this);
            mapTraffic.setImageResource(R.mipmap.map_traffic_white);
            UiSettings uiSettings = mAMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(false);
        }
    }

    /**
     * 导航初始化
     */
    private void initNavi() {
        mStrategyBean = new StrategyBean(false, false, false, false);
        startList.add(startLatlng);
        endList.add(endLatlng);
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);

    }

    /**
     * 接收驾车偏好设置项
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (NavigationUtil.ACTIVITY_RESULT_CODE == resultCode) {
            boolean congestion = data.getBooleanExtra(NavigationUtil.INTENT_NAME_AVOID_CONGESTION, false);
            mStrategyBean.setCongestion(congestion);
            boolean cost = data.getBooleanExtra(NavigationUtil.INTENT_NAME_AVOID_COST, false);
            mStrategyBean.setCost(cost);
            boolean avoidhightspeed = data.getBooleanExtra(NavigationUtil.INTENT_NAME_AVOID_HIGHSPEED, false);
            mStrategyBean.setAvoidhightspeed(avoidhightspeed);
            boolean hightspeed = data.getBooleanExtra(NavigationUtil.INTENT_NAME_PRIORITY_HIGHSPEED, false);
            mStrategyBean.setHightspeed(hightspeed);
            calculateDriveRoute();
        }
    }

    /**
     * 驾车路径规划计算
     */
    private void calculateDriveRoute() {
        try {
            strategyFlag = mAMapNavi.strategyConvert(mStrategyBean.isCongestion(), mStrategyBean.isCost(),
                    mStrategyBean.isAvoidhightspeed(), mStrategyBean.isHightspeed(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAMapNavi.calculateDriveRoute(startList, endList, wayList, strategyFlag);
    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        cleanRouteOverlay();
        HashMap<Integer, AMapNaviPath> paths = mAMapNavi.getNaviPaths();
        for (int i = 0; i < ints.length; i++) {
            AMapNaviPath path = paths.get(ints[i]);
            if (path != null) {
                drawRoutes(ints[i], path);
            }
        }
        setRouteLineTag(paths, ints);
        mAMap.setMapType(AMap.MAP_TYPE_NAVI);
    }

    /**
     * 绘制路径规划结果
     *
     * @param routeId 路径规划线路ID
     * @param path    AMapNaviPath
     */
    private void drawRoutes(int routeId, AMapNaviPath path) {
        mAMap.moveCamera(CameraUpdateFactory.changeTilt(0));
        RouteOverLay routeOverLay = new RouteOverLay(mAMap, path, this);
        try {
            routeOverLay.setWidth(60f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        routeOverLay.setTrafficLine(true);
        routeOverLay.addToMap();
        routeOverlays.put(routeId, routeOverLay);
    }

    /**
     * @param paths 多路线回调路线
     * @param ints  多路线回调路线ID
     */
    private void setRouteLineTag(HashMap<Integer, AMapNaviPath> paths, int[] ints) {
        if (ints.length < 1) {
            visiableRouteLine(false, false, false);
            return;
        }
        int indexOne = 0;
        String stragegyTagOne = paths.get(ints[indexOne]).getLabels();
        setLinelayoutOneContent(ints[indexOne], stragegyTagOne);
        if (ints.length == 1) {
            visiableRouteLine(true, false, false);
            focuseRouteLine(true, false, false);
            return;
        }

        int indexTwo = 1;
        String stragegyTagTwo = paths.get(ints[indexTwo]).getLabels();
        setLinelayoutTwoContent(ints[indexTwo], stragegyTagTwo);
        if (ints.length == 2) {
            visiableRouteLine(true, true, false);
            focuseRouteLine(true, false, false);
            return;
        }

        int indexThree = 2;
        String stragegyTagThree = paths.get(ints[indexThree]).getLabels();
        setLinelayoutThreeContent(ints[indexThree], stragegyTagThree);
        if (ints.length >= 3) {
            visiableRouteLine(true, true, true);
            focuseRouteLine(true, false, false);
        }

    }

    /**
     * 设置第一条线路Tab 内容
     *
     * @param routeID  路线ID
     * @param strategy 策略标签
     */
    private void setLinelayoutOneContent(int routeID, String strategy) {
        routeLineOne.setTag(routeID);
        RouteOverLay overlay = routeOverlays.get(routeID);
        overlay.zoomToSpan();
        AMapNaviPath path = overlay.getAMapNaviPath();
        routeLineOneStrategy.setText(strategy);
        String timeDes = NavigationUtil.getFriendlyTime(path.getAllTime());
        routeLineOneTime.setText(timeDes);
        String disDes = NavigationUtil.getFriendlyDistance(path.getAllLength());
        routeLineOneDistance.setText(disDes);
    }


    /**
     * 设置第二条路线Tab 内容
     *
     * @param routeID  路线ID
     * @param strategy 策略标签
     */
    private void setLinelayoutTwoContent(int routeID, String strategy) {
        routeLineTwo.setTag(routeID);
        RouteOverLay overlay = routeOverlays.get(routeID);
        AMapNaviPath path = overlay.getAMapNaviPath();
        routeLineTwoStrategy.setText(strategy);
        String timeDes = NavigationUtil.getFriendlyTime(path.getAllTime());
        routeLineTwoTime.setText(timeDes);
        String disDes = NavigationUtil.getFriendlyDistance(path.getAllLength());
        routeLineTwoDistance.setText(disDes);
    }


    /**
     * 设置第三条路线Tab 内容
     *
     * @param routeID  路线ID
     * @param strategy 策略标签
     */
    private void setLinelayoutThreeContent(int routeID, String strategy) {
        routeLineThree.setTag(routeID);
        RouteOverLay overlay = routeOverlays.get(routeID);
        AMapNaviPath path = overlay.getAMapNaviPath();
        routeLineThreeStrategy.setText(strategy);
        String timeDes = NavigationUtil.getFriendlyTime(path.getAllTime());
        routeLineThreeTime.setText(timeDes);
        String disDes = NavigationUtil.getFriendlyDistance(path.getAllLength());
        routeLineThreeDistance.setText(disDes);
    }


    private void visiableRouteLine(boolean lineOne, boolean lineTwo, boolean lineThree) {
        setLinelayoutOneVisiable(lineOne);
        setLinelayoutTwoVisiable(lineTwo);
        setLinelayoutThreeVisiable(lineThree);
    }

    private void setLinelayoutOneVisiable(boolean visiable) {
        if (visiable) {
            routeLineOne.setVisibility(View.VISIBLE);
        } else {
            routeLineOne.setVisibility(View.GONE);
        }
    }

    private void setLinelayoutTwoVisiable(boolean visiable) {
        if (visiable) {
            routeLineTwo.setVisibility(View.VISIBLE);
        } else {
            routeLineTwo.setVisibility(View.GONE);
        }
    }

    private void setLinelayoutThreeVisiable(boolean visiable) {
        if (visiable) {
            routeLineThree.setVisibility(View.VISIBLE);
        } else {
            routeLineThree.setVisibility(View.GONE);
        }
    }

    private void cleanRouteOverlay() {
        for (int i = 0; i < routeOverlays.size(); i++) {
            int key = routeOverlays.keyAt(i);
            RouteOverLay overlay = routeOverlays.get(key);
            overlay.removeFromMap();
            overlay.destroy();
        }
        routeOverlays.clear();
    }

    @OnClick({R.id.map_traffic,R.id.calculate_route_start_navi,R.id.route_line_one, R.id.route_line_two,
            R.id.route_line_three, R.id.calculate_route_strategy_tab, R.id.strategy_choose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case  R.id.map_traffic:
                setTraffic();
                break;
            case R.id.calculate_route_start_navi:
                startNavi();
                break;
            case R.id.route_line_one:
                focuseRouteLine(true, false, false);
                break;
            case R.id.route_line_two:
                focuseRouteLine(false, true, false);
                break;
            case R.id.route_line_three:
                focuseRouteLine(false, false, true);
                break;
            case R.id.calculate_route_strategy_tab:
                break;
            case R.id.strategy_choose:
                strategyChoose();
                break;
                default:break;
        }
    }

    /**
     * 地图实时交通开关
     */
    private void setTraffic() {
        if (mAMap.isTrafficEnabled()) {
            mapTraffic.setImageResource(R.mipmap.map_traffic_white);
            mAMap.setTrafficEnabled(false);
        } else {
            mapTraffic.setImageResource(R.mipmap.map_traffic_hl_white);
            mAMap.setTrafficEnabled(true);
        }
    }

    /**
     * 开始导航
     */
    private void startNavi() {
        if (routeID != -1){
            mAMapNavi.selectRouteId(routeID);
            Intent gpsintent = new Intent(getApplicationContext(), RouteNavigationActivity.class);
            gpsintent.putExtra("gps", true); // gps 为true为真实导航，为false为模拟导航
            startActivity(gpsintent);
        }
    }

    /**
     * 路线tag选中设置
     *
     * @param lineOne
     * @param lineTwo
     * @param lineThree
     */
    private void focuseRouteLine(boolean lineOne, boolean lineTwo, boolean lineThree) {
        Log.d("LG", "lineOne:" + lineOne + " lineTwo:" + lineTwo + " lineThree:" + lineThree);
        setLinelayoutOne(lineOne);
        setLinelayoutTwo(lineTwo);
        setLinelayoutThree(lineThree);
    }

    /**
     * 第一条路线是否focus
     *
     * @param focus focus为true 突出颜色显示，标示为选中状态，为false则标示非选中状态
     */
    private void setLinelayoutOne(boolean focus) {
        if (routeLineOne.getVisibility() != View.VISIBLE) {
            return;
        }
        try {
            RouteOverLay overlay = routeOverlays.get((int)routeLineOne.getTag());
            if (focus) {
                routeID = (int) routeLineOne.getTag();
                calculateRouteNaviOverview.setText(NavigationUtil.getRouteOverView(overlay.getAMapNaviPath()));
                mAMapNavi.selectRouteId(routeID);
                overlay.setTransparency(ROUTE_SELECTED_TRANSPARENCY);
                routeLineOneView.setVisibility(View.VISIBLE);
                routeLineOneStrategy.setTextColor(getResources().getColor(R.color.material_blue));
                routeLineOneTime.setTextColor(getResources().getColor(R.color.material_blue));
                routeLineOneDistance.setTextColor(getResources().getColor(R.color.material_blue));
            } else {
                overlay.setTransparency(ROUTE_UNSELECTED_TRANSPARENCY);
                routeLineOneView.setVisibility(View.INVISIBLE);
                routeLineOneStrategy.setTextColor(getResources().getColor(R.color.darker_gray));
                routeLineOneTime.setTextColor(getResources().getColor(R.color.darker_gray));
                routeLineOneDistance.setTextColor(getResources().getColor(R.color.darker_gray));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 第二条路线是否focus
     *
     * @param focus focus为true 突出颜色显示，标示为选中状态，为false则标示非选中状态
     */
    private void setLinelayoutTwo(boolean focus) {
        if (routeLineTwo.getVisibility() != View.VISIBLE) {
            return;
        }
        try {
            RouteOverLay overlay = routeOverlays.get((int) routeLineTwo.getTag());
            if (focus) {
                routeID = (int) routeLineTwo.getTag();
                calculateRouteNaviOverview.setText(NavigationUtil.getRouteOverView(overlay.getAMapNaviPath()));
                mAMapNavi.selectRouteId(routeID);
                overlay.setTransparency(ROUTE_SELECTED_TRANSPARENCY);
                routeLineTwoView.setVisibility(View.VISIBLE);
                routeLineTwoStrategy.setTextColor(getResources().getColor(R.color.material_blue));
                routeLineTwoTime.setTextColor(getResources().getColor(R.color.material_blue));
                routeLineTwoDistance.setTextColor(getResources().getColor(R.color.material_blue));
            } else {
                overlay.setTransparency(ROUTE_UNSELECTED_TRANSPARENCY);
                routeLineTwoView.setVisibility(View.INVISIBLE);
                routeLineTwoStrategy.setTextColor(getResources().getColor(R.color.darker_gray));
                routeLineTwoTime.setTextColor(getResources().getColor(R.color.darker_gray));
                routeLineTwoDistance.setTextColor(getResources().getColor(R.color.darker_gray));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 第三条路线是否focus
     *
     * @param focus focus为true 突出颜色显示，标示为选中状态，为false则标示非选中状态
     */
    private void setLinelayoutThree(boolean focus) {
        if (routeLineThree.getVisibility() != View.VISIBLE) {
            return;
        }
        try {
            RouteOverLay overlay = routeOverlays.get((int) routeLineThree.getTag());
            if (overlay == null) {
                return;
            }
            if (focus) {
                routeID = (int) routeLineThree.getTag();
                calculateRouteNaviOverview.setText(NavigationUtil.getRouteOverView(overlay.getAMapNaviPath()));
                mAMapNavi.selectRouteId(routeID);
                overlay.setTransparency(ROUTE_SELECTED_TRANSPARENCY);
                routeLineThreeView.setVisibility(View.VISIBLE);
                routeLineThreeStrategy.setTextColor(getResources().getColor(R.color.material_blue));
                routeLineThreeTime.setTextColor(getResources().getColor(R.color.material_blue));
                routeLineThreeDistance.setTextColor(getResources().getColor(R.color.material_blue));
            } else {
                overlay.setTransparency(ROUTE_UNSELECTED_TRANSPARENCY);
                routeLineThreeView.setVisibility(View.INVISIBLE);
                routeLineThreeStrategy.setTextColor(getResources().getColor(R.color.darker_gray));
                routeLineThreeTime.setTextColor(getResources().getColor(R.color.darker_gray));
                routeLineThreeDistance.setTextColor(getResources().getColor(R.color.darker_gray));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到驾车偏好设置页面
     */
    private void strategyChoose() {
        Intent intent = new Intent(this, StrategyChooseActivity.class);
        intent.putExtra(NavigationUtil.INTENT_NAME_AVOID_CONGESTION, mStrategyBean.isCongestion());
        intent.putExtra(NavigationUtil.INTENT_NAME_AVOID_COST, mStrategyBean.isCost());
        intent.putExtra(NavigationUtil.INTENT_NAME_AVOID_HIGHSPEED, mStrategyBean.isAvoidhightspeed());
        intent.putExtra(NavigationUtil.INTENT_NAME_PRIORITY_HIGHSPEED, mStrategyBean.isHightspeed());
        startActivityForResult(intent, NavigationUtil.START_ACTIVITY_REQUEST_CODE);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        naviView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        naviView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        naviView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        naviView.onDestroy();
        if (mAMapNavi != null) {
            mAMapNavi.destroy();
        }
    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

    }
}
