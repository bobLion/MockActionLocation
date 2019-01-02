package com.bob.android.mockactionlocation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.MapView;
import com.amap.api.services.route.WalkPath;
import com.bob.android.mockactionlocation.R;
import com.bob.android.mockactionlocation.adapter.WalkSegmentListAdapter;
import com.bob.android.mockactionlocation.base.BaseActivity;
import com.bob.android.mockactionlocation.map.overlay.util.AMapUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalkRouteDetailActivity extends BaseActivity {

    @BindView(R.id.title_back)
    LinearLayout titleBack;
    @BindView(R.id.title_center)
    TextView mTitle;
    @BindView(R.id.map)
    TextView map;
    @BindView(R.id.title_map)
    LinearLayout titleMap;
    @BindView(R.id.route_title)
    RelativeLayout routeTitle;
    @BindView(R.id.firstline)
    TextView mTitleWalkRoute;
    @BindView(R.id.secondline)
    TextView secondline;
    @BindView(R.id.bus_segment_list)
    ListView mWalkSegmentList;
    @BindView(R.id.bus_path)
    LinearLayout busPath;
    @BindView(R.id.route_map)
    MapView routeMap;

    private WalkPath mWalkPath;
    private WalkSegmentListAdapter mWalkSegmentListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_route_detail);
        ButterKnife.bind(this);
        mContext = this;
        getIntentData();
        mTitle.setText("步行路线详情");
        String dur = AMapUtil.getFriendlyTime((int) mWalkPath.getDuration());
        String dis = AMapUtil.getFriendlyLength((int) mWalkPath.getDistance());
        mTitleWalkRoute.setText(dur + "(" + dis + ")");
        mWalkSegmentList = (ListView) findViewById(R.id.bus_segment_list);
        mWalkSegmentListAdapter = new WalkSegmentListAdapter(this.getApplicationContext(),
                mWalkPath.getSteps());
        mWalkSegmentList.setAdapter(mWalkSegmentListAdapter);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mWalkPath = intent.getParcelableExtra("walk_path");
    }

    @OnClick(R.id.calculate_route_start_navi)
    void startNavigationClick(View view){
        Intent intent = new Intent(mContext,RouteNavigationActivity.class);
        intent.putExtra("gps", false); // gps 为true为真实导航，为false为模拟导航
        startActivity(intent);
    }
    public void onBackClick(View view) {
        this.finish();
    }
}
