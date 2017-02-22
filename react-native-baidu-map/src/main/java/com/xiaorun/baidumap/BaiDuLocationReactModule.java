package com.xiaorun.baidumap;

import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;


public class BaiDuLocationReactModule extends ReactContextBaseJavaModule implements BDLocationListener{

    //声明AMapLocationClient类对象
    private final LocationClient mLocationClient;

    //声明定位回调监听器
    private BDLocationListener mLocationListener = this; // new AMapLocationListener();

    private final ReactApplicationContext mReactContext;


    private boolean needMars = false;
    private boolean needDetail = false;

    private void sendEvent(String eventName,
                            @Nullable WritableMap params) {
        if (mReactContext != null) {
            mReactContext
              .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
              .emit(eventName, params);
          }
    }


    public BaiDuLocationReactModule(ReactApplicationContext reactContext) {
        super(reactContext);
        //初始化定位
        this.mLocationClient = new LocationClient(reactContext);
        //设置定位回调监听
        this.mLocationClient.registerLocationListener(mLocationListener);
        mReactContext = reactContext;

    }

    @Override
    public String getName() {
        return "BaiDuLocation";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        // constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
        // constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
        return constants;
    }

    // 开启位置监听
    @ReactMethod
    public void startLocation(@Nullable ReadableMap options) {
        LocationClientOption option = new LocationClientOption();

        // 默认值
        needMars = false;
        needDetail = false;
        try {
            Log.i("startLocation---","AAAAAAAA");
        if (options != null) {
            Log.i("startLocation---","BBBBBB");
            if (options.hasKey("coorType")) {
                option.setCoorType( options.getString("coorType"));
                //可选，默认gcj02，设置返回的定位结果坐标系
                Log.i("startLocation---",options.getString("coorType"));
            }
            if (options.hasKey("span")) {
                option.setScanSpan(options.getInt("span"));
                //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
            }


            if (options.hasKey("LocationMode")) {
                //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
                switch(options.getString("LocationMode")) {
                    case "Accuracy":
                        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

                        break;
                    case "Sensors":
                        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
                        break;
                    case "Saving":
                        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
                        break;
                    default:
                        break;
                }
            }
            if (options.hasKey("needAddress")) {
                option.setIsNeedAddress(options.getBoolean("needAddress"));
                //可选，设置是否需要地址信息，默认不需要

            }
            if (options.hasKey("openGps")) {
                option.setOpenGps(options.getBoolean("openGps"));
                //可选，默认false,设置是否使用gps

            }
            if (options.hasKey("killProcess")) {
                option.setIgnoreKillProcess(options.getBoolean("killProcess"));
                //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

            }
            option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
            option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
            option.setIsNeedLocationPoiList(true);


        }
        } catch (Exception e){
            Toast.makeText(mReactContext,"参数类型出现错误",Toast.LENGTH_SHORT).show();
        }
        Log.i("startLocation---","CCCCCCC");
        //给定位客户端对象设置定位参数


        this.mLocationClient.setLocOption(option);
//        setLocationOption();
        //启动定位
        this.mLocationClient.start();
    }

    // 停止位置监听
    @ReactMethod
    public void stopLocation() {
        if(null!=this.mLocationClient){
            this.mLocationClient.stop();
        }

    }

    // 坐标转换

    /*
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != this.mLocationClient) {
            this.mLocationClient.onDestroy();
            this.mLocationClient = null;
        }
    }
    */

    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private String tempcoor = "gcj02";
    // //////////////////////////定位\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        // TODO Auto-generated method stub
        option.setLocationMode(tempMode);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType(tempcoor);// 可选，默认gcj02，设置返回的定位结果坐标系，
        int span = 1000;

        option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);

    }

    private WritableMap amapLocationToObject(BDLocation location) {
        WritableMap map = Arguments.createMap();
        if (null != location && location.getLocType() != BDLocation.TypeServerError) {


            map.putInt("locType", location.getLocType());
            map.putDouble("latitude", location.getLatitude());
            map.putDouble("lontitude",location.getLongitude());
            map.putDouble("radius", location.getRadius());

            map.putString("address", location.getAddrStr());
            map.putString("countryCode", location.getCountryCode());
            map.putString("country", location.getCountry());
            map.putString("province", location.getProvince());
            map.putString("citycode", location.getCityCode());
            map.putString("city", location.getCity());
            map.putString("district", location.getDistrict());//区
            map.putString("street", location.getStreet());//街道
            Log.i("address---",location.getAddrStr()+"");

        }else{
            map.putInt("errorCode", BDLocation.TypeServerError);
            map.putString("errorInfo","定位失败");
        }

       return map;
    }

    // Utils
    public static double transformLat(double x, double y) {
        double ret =
            -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * Math.PI) + 40.0 * Math.sin(y / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * Math.PI) + 320 * Math.sin(y * Math.PI / 30.0)) * 2.0 / 3.0;
        return ret;
      }

      public static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * Math.PI) + 40.0 * Math.sin(x / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret +=
            (150.0 * Math.sin(x / 12.0 * Math.PI) + 300.0 * Math.sin(x / 30.0 * Math.PI)) * 2.0 / 3.0;
        return ret;
      }
      /**
       *
       * @param -lat纬度
       * @param -lng经度
       * @return delta[0] 是纬度差，delta[1]是经度差
       */
      public static double[] delta(double lat,double lng){
        double[] delta = new double[2];
        double a = 6378245.0;
        double ee = 0.00669342162296594323;
        double dLat = transformLat(lng-105.0, lat-35.0);
        double dLng = transformLon(lng-105.0, lat-35.0);
        double radLat = lat / 180.0 * Math.PI;
        double magic = Math.sin(radLat);
        magic = 1 - ee*magic*magic;
        double sqrtMagic = Math.sqrt(magic);
        delta[0] = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * Math.PI);
        delta[1] = (dLng * 180.0) / (a / sqrtMagic * Math.cos(radLat) * Math.PI);
        return delta;
      }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
            Log.i("onReceiveLocation---","开始定位");
        if (bdLocation != null) {
            sendEvent("onReceiveLocationBaiDu", amapLocationToObject(bdLocation));
             this.mLocationClient.stop();

        }else{
            Log.i("onReceiveLocation---","开始失败");
        }
    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }
}
