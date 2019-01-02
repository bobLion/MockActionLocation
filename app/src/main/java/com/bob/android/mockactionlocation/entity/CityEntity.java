package com.bob.android.mockactionlocation.entity;

import java.io.Serializable;

/**
 * @package com.bob.android.mockactionlocation.entity
 * @fileName CityEntity
 * @Author Bob on 2018/12/23 11:19.
 * @Describe TODO
 */
public class CityEntity implements Serializable {

    public String chinese;
    public String adcode;
    public String citycode;


    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }
}
