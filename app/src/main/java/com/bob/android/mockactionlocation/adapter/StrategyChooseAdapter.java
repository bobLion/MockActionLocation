package com.bob.android.mockactionlocation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bob.android.mockactionlocation.R;
import com.bob.android.mockactionlocation.entity.StrategyStateBean;
import com.bob.android.mockactionlocation.map.overlay.util.NavigationUtil;

import java.util.List;


/**
 * 驾车偏好设置adapter，高速优先与不走高速、避免收费互斥
 * Created by ligen on 16/10/28.
 */
public class StrategyChooseAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private List<StrategyStateBean> mStrategys;

    public StrategyChooseAdapter(Context context, List<StrategyStateBean> strategys) {
        mContext = context;
        mStrategys = strategys;
    }

    @Override
    public int getCount() {
        return mStrategys.size();
    }

    @Override
    public StrategyStateBean getItem(int i) {
        return mStrategys.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_strategy_choose, null);
            holder.mStrategyNameText = (TextView) view.findViewById(R.id.strategy_name);
            holder.mStrategyChooseFlagImage = (ImageView) view.findViewById(R.id.strategy_choose_flag);
            holder.mStrategyBean = mStrategys.get(i);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        view.setOnClickListener(this);
        StrategyStateBean strategyBean = mStrategys.get(i);
        if (strategyBean != null) {
            int strategyCode = strategyBean.getStrategyCode();
            String strategyName = getStrategyName(strategyCode);
            holder.mStrategyNameText.setText(strategyName);

        }
        if (holder != null && holder.mStrategyBean != null) {
            boolean isOpen = holder.mStrategyBean.isOpen();
            if (isOpen) {
                holder.mStrategyChooseFlagImage.setBackgroundResource(R.mipmap.prefer_setting_btn_on);
            } else {
                holder.mStrategyChooseFlagImage.setBackgroundResource(R.mipmap.prefer_setting_btn_off);
            }
        }

        return view;
    }

    private String getStrategyName(int strategyCode) {
        String strategyName = "";
        switch (strategyCode) {
            case NavigationUtil.AVOID_CONGESTION:
                strategyName = mContext.getString(R.string.congestion);
                break;
            case NavigationUtil.AVOID_COST:
                strategyName = mContext.getString(R.string.cost);
                break;
            case NavigationUtil.AVOID_HIGHSPEED:
                strategyName = mContext.getString(R.string.avoidhightspeed);
                break;
            case NavigationUtil.PRIORITY_HIGHSPEED:
                strategyName = mContext.getString(R.string.hightspeed);
                break;
            default:
                break;
        }
        return strategyName;
    }

    @Override
    public void onClick(View view) {
        Holder holder = (Holder) view.getTag();
        if (holder == null || holder.mStrategyBean == null) {
            return;
        }
        if (holder.mStrategyBean.isOpen()) {
            holder.mStrategyBean.setOpen(false);
        } else {
            holder.mStrategyBean.setOpen(true);
        }

        int strategyCode = holder.mStrategyBean.getStrategyCode();

        if (strategyCode == NavigationUtil.PRIORITY_HIGHSPEED) {
            for (StrategyStateBean bean : mStrategys) {
                if (bean.getStrategyCode() == NavigationUtil.AVOID_COST || bean.getStrategyCode() == NavigationUtil.AVOID_HIGHSPEED) {
                    bean.setOpen(false);
                }
            }
        }

        if (strategyCode == NavigationUtil.AVOID_COST || strategyCode == NavigationUtil.AVOID_HIGHSPEED) {
            for (StrategyStateBean bean : mStrategys) {
                if (bean.getStrategyCode() == NavigationUtil.PRIORITY_HIGHSPEED) {
                    bean.setOpen(false);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class Holder {
        public TextView mStrategyNameText;
        public ImageView mStrategyChooseFlagImage;
        public StrategyStateBean mStrategyBean;
    }
}
