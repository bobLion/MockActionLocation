package com.bob.android.mockactionlocation.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * @package com.bob.android.mockactionlocation.util
 * @fileName PermissionUtil
 * @Author Bob on 2018/12/21 17:31.
 * @Describe TODO
 */
public class PermissionUtil {

    private final Context mContext;

    public PermissionUtil(Context mContext) {
        this.mContext = mContext;
    }
    // 判断权限集合
    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    // 判断是否缺少权限
    private boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(mContext, permission) ==
                PackageManager.PERMISSION_DENIED;
    }
}
