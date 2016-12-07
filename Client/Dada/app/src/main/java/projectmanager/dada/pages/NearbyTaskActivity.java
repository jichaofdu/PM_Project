package projectmanager.dada.pages;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.LatLng;

import projectmanager.dada.R;

public class NearbyTaskActivity extends AppCompatActivity implements LocationSource, AMapLocationListener {

    private MapView mapView;
    private AMap aMap;

    private AMapLocationClient mapLocationClient = null;                    //定位发起端
    private AMapLocationClientOption mapLocationClientOption = null;       //定位参数
    private OnLocationChangedListener mapListener = null;  //定位监听器

    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_task);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        aMap = mapView.getMap();

        //设置显示定位按钮 并且可以点击
        UiSettings settings = aMap.getUiSettings();
        //设置定位监听
        aMap.setLocationSource(this);
        // 是否显示定位按钮
        settings.setMyLocationButtonEnabled(true);
        // 是否可触发定位并显示定位层
        aMap.setMyLocationEnabled(true);

        initLoc();
    }

    //定位
    private void initLoc() {
        //初始化定位
        mapLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mapLocationClient.setLocationListener(this);
        //初始化定位参数
        mapLocationClientOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mapLocationClientOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mapLocationClientOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mapLocationClientOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mapLocationClientOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mapLocationClientOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mapLocationClient.setLocationOption(mapLocationClientOption);
        //启动定位
        mapLocationClient.startLocation();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mapView.onSaveInstanceState(outState);
    }

    //激活定位
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mapListener = onLocationChangedListener;
    }

    //停止定位
    @Override
    public void deactivate() {
        mapListener = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                if (isFirstLoc) {
                    mapListener.onLocationChanged(aMapLocation);
                    LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    mapListener.onLocationChanged(aMapLocation);
                    isFirstLoc = false;
                }
            }
        }
    }
}
