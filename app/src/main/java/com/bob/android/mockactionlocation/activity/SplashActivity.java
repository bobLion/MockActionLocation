package com.bob.android.mockactionlocation.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.bob.android.mockactionlocation.R;
import com.bob.android.mockactionlocation.base.BaseActivity;
import com.bob.android.mockactionlocation.config.AppConfig;
import com.bob.android.mockactionlocation.util.PermissionUtil;
import com.bob.android.mockactionlocation.util.PreferencesUtils;

import java.io.File;

import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    private Context mContext;
    private Handler handler = new Handler();
    private Runnable splashRunnable = new SplashRunnable();

    //所需权限
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.REQUEST_INSTALL_PACKAGES,
            Manifest.permission.READ_PHONE_STATE,
    };

    private PermissionUtil mPermissionUtil; //权限检测器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        mContext = this;
        mPermissionUtil = new PermissionUtil(mContext);
        initPermission();
    }

    private void initPermission() {
        if (mPermissionUtil.lacksPermissions(PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, AppConfig.PERMISSION_REQUEST_CODE);
        } else {
            handler.postDelayed(splashRunnable, 2000);
        }
    }

    private class SplashRunnable implements Runnable {
        @Override
        public void run() {
            Intent intent = new Intent(mContext,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case AppConfig.PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    handler.postDelayed(splashRunnable, 2000);
                } else {
                    finish();
                }
        }
    }
}
