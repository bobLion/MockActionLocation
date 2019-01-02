package com.bob.android.mockactionlocation.entity;

import com.amap.api.services.core.LatLonPoint;

import java.io.Serializable;

/**
 * @package com.bob.android.mockactionlocation.entity
 * @fileName PositionEntity
 * @Author Bob on 2018/12/26 11:55.
 * @Describe TODO
 */
public class PositionEntity implements Serializable{

    private String name;
    private String code;
    private String title;
    private String cityName;
    private LatLonPoint latLonPoint;
    public double latitue;
    public double longitude;
    public String address;
    public String city;


    public PositionEntity(String name, String code, String title, String cityName, LatLonPoint latLonPoint) {
        this.name = name;
        this.code = code;
        this.title = title;
        this.cityName = cityName;
        this.latLonPoint = latLonPoint;
    }

    public PositionEntity(String name, String code, LatLonPoint latLonPoint) {
        this.name = name;
        this.code = code;
        this.latLonPoint = latLonPoint;
    }


    public double getLatitue() {
        return latitue;
    }

    public void setLatitue(double latitue) {
        this.latitue = latitue;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public LatLonPoint getLatLonPoint() {
        return latLonPoint;
    }

    public void setLatLonPoint(LatLonPoint latLonPoint) {
        this.latLonPoint = latLonPoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
