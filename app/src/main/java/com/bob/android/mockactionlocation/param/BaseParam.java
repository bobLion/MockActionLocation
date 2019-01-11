package com.bob.android.mockactionlocation.param;

import java.io.Serializable;

/**
 * @package com.bob.android.mockactionlocation.param
 * @fileName BaseParam
 * @Author Bob on 2019/1/3 15:55.
 * @Describe TODO
 */
public class BaseParam implements Serializable {

    private String userName;
    private String permission;
    private String telNum;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }
}
