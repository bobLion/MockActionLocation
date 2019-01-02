package com.bob.android.mockactionlocation.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bob.android.mockactionlocation.R;
import com.bob.android.mockactionlocation.entity.Province;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @package com.sailing.android.szzs.util
 * @fileName RemoveControlDialog
 * @Author Bob on 2018/10/18 9:18.
 * @Describe TODO
 */

public class SearchPointDialog extends Dialog {

    @BindView(R.id.sp_province)
    Spinner spProvince;
    @BindView(R.id.sp_city)
    Spinner spCity;
    @BindView(R.id.sp_area)
    Spinner spArea;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;

    private ArrayAdapter<String> cityAdapter;
    private ArrayAdapter<String> areaAdapter;
    private String provinceName = "";    // 省的名字
    private String cityName = "";        // 市的名字
    private String areaName = "";        // 区的名字

    private int provincePosition = 0; // 当前选的省份的位置
    private int cityPosition = 0;     // 当前城市在List中的位置
    private int areaPosition = 0;     // 当前城区在list的位置


    private Activity mContext;
    public SearchPointDialog mDialog;
    List<Province> provinces = new ArrayList<>();
    private int page = 1, size = 10, pageCount;

    private List<String> provinceList = new ArrayList<>(); // 所有省份的list
    private List<String> cityList = new ArrayList<>();     // 当前选中省份的城市
    private List<String> areaList = new ArrayList<>();     // 当前选中城市的城区


    public SearchPointDialog(Activity context, int theme) {
        super(context, theme);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.layout_select_point);
        ButterKnife.bind(this);
        mDialog = this;

        Window dialogWindow = this.getWindow();
        WindowManager m = mContext.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
//        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        p.width = (int) (d.getWidth() * 0.95);
        dialogWindow.setAttributes(p);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
        initJsonData();
        showData();
    }

    private void initJsonData() {
        try {
            StringBuffer sb = new StringBuffer();
            InputStream is = mContext.getAssets().open("citylist.json");
            byte[] b = new byte[is.available()];
            int len = -1;
            while ((len = is.read(b)) != -1) {
                sb.append(new String(b, 0, len, "utf-8"));
            }
            is.close();
            provinces = JSON.parseArray(sb.toString(),Province.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_confirm)
    void confirmClick(View view){
        mOnPointSelected.selectedPoint(provinceName + cityName + areaName);
        mDialog.dismiss();
    }

    /**
     * 展示数据
     */
    private void showData() {
        for (Province province : provinces) {
            provinceList.add(province.getName());
        }
        // 显示省份数据
        spProvince.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, provinceList));
        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                provincePosition = position;
                provinceName = provinceList.get(position);
                // 获取当前省份对应的城市list
                cityList.clear();
                List<Province.CityBean> cityBeans = provinces.get(position).getCity();
                for (Province.CityBean city : cityBeans) {
                    cityList.add(city.getName());
                }

                // 刷新城市列表
                spCity.setSelection(0);
                cityName = cityList.get(0);
                cityAdapter.notifyDataSetChanged();

                // 刷新城区列表
                updateArea(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 显示城市数据
        spCity.setAdapter(cityAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, cityList));
        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityPosition = position;
                cityName = cityList.get(position);
                // 刷新城区列表
                updateArea(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 显示城区数据
        spArea.setAdapter(areaAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, areaList));
        spArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                areaName = areaList.get(position);
                mTvAddress.setText(provinceName + cityName + areaName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * 更新城区数据
     *
     * @param position
     */
    private void updateArea(int position) {
        areaList.clear();
        Province.CityBean cityBean = provinces.get(provincePosition).getCity().get(position);
        areaList.addAll(cityBean.getArea());
        spArea.setSelection(0);
        areaName = areaList.get(0);
        areaAdapter.notifyDataSetChanged();
        mTvAddress.setText(provinceName + cityName + areaName);
    }
    private OnPointSelectedListener mOnPointSelected ;

    public interface OnPointSelectedListener{
        void selectedPoint(String selectStr);
    }

    public void setOnPointSelectedListener(OnPointSelectedListener onPointSelected){
        this.mOnPointSelected = onPointSelected;
    }
}
