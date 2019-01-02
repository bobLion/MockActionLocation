package com.bob.android.mockactionlocation.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @package com.bob.android.mockactionlocation.entity
 * @fileName provinceEntity
 * @Author Bob on 2018/12/23 11:21.
 * @Describe TODO
 */
public class ProvinceEntity implements Serializable{

    private List<CityEntity> cityEntityList;

    public List<CityEntity> getCityEntityList() {
        return cityEntityList;
    }

    public void setCityEntityList(List<CityEntity> cityEntityList) {
        this.cityEntityList = cityEntityList;
    }
}
