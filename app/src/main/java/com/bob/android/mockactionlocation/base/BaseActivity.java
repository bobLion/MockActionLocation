package com.bob.android.mockactionlocation.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bob.android.mockactionlocation.application.ActivityManager;

/**
 * @package com.bob.android.mockactionlocation.base
 * @fileName BaseActivity
 * @Author Bob on 2018/12/21 17:29.
 * @Describe TODO
 */
public class BaseActivity extends Activity {

    public long exitTime;
    public Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance(this).pushActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        ActivityManager.getInstance(this).finishAcitivty(this);
    }
    protected boolean onBackQuit() {
        return false;
    }
}
