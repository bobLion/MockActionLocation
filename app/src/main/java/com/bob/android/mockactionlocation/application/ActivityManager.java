package com.bob.android.mockactionlocation.application;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @package com.bob.android.mockactionlocation.application
 * @fileName ActivityManager
 * @Author Bob on 2018/12/21 17:39.
 * @Describe TODO
 */
public class ActivityManager {

    private static ActivityManager instance;
    private Context context;
    private HashMap<String, ArrayList<Activity>> activityMap;

    private ActivityManager(Context context) {
        this.context = context;
        activityMap = new HashMap<String, ArrayList<Activity>>();
    }

    public static ActivityManager getInstance(Context context) {
        if (instance == null) {
            instance = new ActivityManager(context);
        }
        return instance;
    }

    public void pushActivity(Activity activity) {
        String key = activity.getClass().getName();
        if (activityMap.containsKey(key)) {
            ArrayList<Activity> list = activityMap.get(key);
            if (!list.contains(activity)) {
                list.add(activity);
            }
        } else {
            ArrayList<Activity> list = new ArrayList<Activity>();
            list.add(activity);
            activityMap.put(key, list);
        }
    }

    public void finishAcitivty(Activity activity){
        ArrayList<Activity> list =activityMap.get(activity.getClass().getName());
        if(list!=null){
            if(list.contains(activity)){
                list.remove(activity);
            }
        }
    }

    public void finishAllAcitivty() {
        Iterator<Map.Entry<String, ArrayList<Activity>>> iter = activityMap
                .entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, ArrayList<Activity>> entry = iter.next();
            ArrayList<Activity> list = entry.getValue();
            for (int i = 0; i < list.size(); i++) {
                try {
                    list.get(i).finish();
                } catch (Exception e) {

                }
            }
        }
        activityMap.clear();
    }
    /**
     * 判断当前应用程序处于前台还是后台
     */
    public boolean isAppFront(Context context) {
        android.app.ActivityManager am = (android.app.ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<android.app.ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
