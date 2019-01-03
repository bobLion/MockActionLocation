package com.bob.android.mockactionlocation.application;

import android.app.Activity;
import android.app.Application;

import com.bob.android.mockactionlocation.util.NetworkUtils;
import com.rey.material.app.ThemeManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @package com.bob.android.mockactionlocation.base
 * @fileName GlobalApplication
 * @Author Bob on 2018/12/21 17:31.
 * @Describe TODO
 */
public class GlobalApplication extends Application {

    private static GlobalApplication application;
    private List<Activity> activityList = new ArrayList<>();

    public static GlobalApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        //material  UI组件
        ThemeManager.init(this, 2, 0, null);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }

    //遍历所有Activity并finish
    public void exit() {
        for(Activity activity:activityList) {
            activity.finish();
        }
        System.exit(0);
    }

    public String getIp() {
        return NetworkUtils.getIPAddress(getApplicationContext());
    }
}
