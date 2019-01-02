package com.bob.android.mockactionlocation.service;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * 
 * @author baidu
 *
 */
public class LocationService {
	private AMapLocationClient client = null;
	private AMapLocationClientOption mOption,DIYoption;
	private Object objLock = new Object();

	/***
	 * 
	 * @param locationContext
	 */
	public LocationService(Context locationContext){
		synchronized (objLock) {
			if(client == null){
				client = new AMapLocationClient(locationContext);
				client.setLocationOption(getDefaultLocationClientOption());
			}
		}
	}
	
	/***
	 * 
	 * @param listener
	 * @return
	 */
	
	public boolean registerListener(AMapLocationListener listener){
		boolean isSuccess = false;
		if(listener != null){
			client.setLocationListener(listener);
			isSuccess = true;
		}
		return  isSuccess;
	}
	
	public void unregisterListener(AMapLocationListener listener){
		if(listener != null){
			client.unRegisterLocationListener(listener);
		}
	}
	
	/***
	 * 
	 * @param option
	 * @return isSuccessSetOption
	 */
	public boolean setLocationOption(AMapLocationClientOption option){
		boolean isSuccess = false;
		if(option != null){
			if(client.isStarted())
				client.onDestroy();
			DIYoption = option;
			client.setLocationOption(option);
		}
		return isSuccess;
	}

	/***
	 *
	 * @return DefaultLocationClientOption  默认O设置
	 */
	public AMapLocationClientOption getDefaultLocationClientOption(){
		if(mOption == null){
			mOption = new AMapLocationClientOption();
			mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//			mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
			mOption.setGpsFirst(true);
			mOption.setInterval(3000);//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
		   /* mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
		    mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
		    mOption.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
		    mOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		    mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死   
		    mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		    mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		    mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
			mOption.setOpenGps(true);//可选，默认false，设置是否开启Gps定位
		    mOption.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用*/
		 
		}
		return mOption;
	}


	/**
	 *
	 * @return DIYOption 自定义Option设置
	 */
	public AMapLocationClientOption getOption(){
		if(DIYoption == null) {
			DIYoption = new AMapLocationClientOption();
		}
		return DIYoption;
	}

	public void start(){
		synchronized (objLock) {
			if(client != null && !client.isStarted()){
				client.startLocation();
			}
		}
	}
	public void stop(){
		synchronized (objLock) {
			if(client != null && client.isStarted()){
				client.stopLocation();
			}
		}
	}

	public boolean isStart() {
		return client.isStarted();
	}


}
