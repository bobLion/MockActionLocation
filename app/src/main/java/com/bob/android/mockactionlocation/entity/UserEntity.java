package com.bob.android.mockactionlocation.entity;

import java.io.Serializable;

/**
 * @package com.bob.android.mockactionlocation.entity
 * @fileName UserEntity
 * @Author Bob on 2019/1/11 17:10.
 * @Describe TODO
 */
public class UserEntity implements Serializable{

    private String userName;
    private String pswd;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }
}
