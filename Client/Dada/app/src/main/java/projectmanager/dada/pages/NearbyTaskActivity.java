package projectmanager.dada.pages;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;

import java.util.ArrayList;
import java.util.List;

import projectmanager.dada.R;
import projectmanager.dada.model.Location;
import projectmanager.dada.model.Task;
import projectmanager.dada.util.ApiManager;
import projectmanager.dada.util.DataManager;

public class NearbyTaskActivity extends AppCompatActivity implements LocationSource, AMapLocationListener, AMap.OnMarkerClickListener {

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

        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.strokeColor(Color.argb(0,0,0,0));
        myLocationStyle.radiusFillColor(Color.argb(0,0,0,0));
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setOnMarkerClickListener(this);
        initLoc();

        Toast.makeText(this, "我也执行了", Toast.LENGTH_SHORT).show();
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
                    aMap.addCircle(new CircleOptions().center(new LatLng(aMap.getMyLocation().getLatitude(), aMap.getMyLocation().getLongitude())).fillColor(Color.argb(127, 0, 0, 255)).strokeColor(Color.BLACK).strokeWidth(5).radius(300));
                    Location location = new Location(0, aMap.getMyLocation().getLongitude(), aMap.getMyLocation().getLatitude(), "");
                    NearbyTask nearbyTask = new NearbyTask(location, 300);
                    nearbyTask.execute((Void) null);
                }
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.getPosition().longitude == aMap.getMyLocation().getLongitude() && marker.getPosition().latitude == aMap.getMyLocation().getLatitude()){
            return false;
        }else {
            List<Task> tasks = DataManager.getInstance().getNearbyList();
            Intent intent = new Intent(this, TaskDetailActivity.class);
            Bundle bundle = new Bundle();
            for(Task task : tasks){
                if (task.getLocation().getLongitude() == marker.getPosition().longitude && task.getLocation().getLatitude() == marker.getPosition().latitude){
                    bundle.putSerializable("task", task);
                    intent.putExtras(bundle);
                }
            }

            startActivity(intent);
            return false;
        }

    }

    public class NearbyTask extends AsyncTask<Void, Void, Boolean> {
        private Location location;
        private double radius;
        private List<Task> taskList = new ArrayList<>();

        NearbyTask(Location location, double radius) {
            this.location = location;
            this.radius = radius;

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            taskList = ApiManager.getInstance().handleGetNearbyTasks(location, radius);
            DataManager.getInstance().setNearbyList(taskList);
            if(taskList == null){
                System.out.println("[Tip] get nearby tasks failed.");
       //         Toast.makeText(NearbyTaskActivity.this, "get nearby tasks failed", Toast.LENGTH_SHORT).show();
                Log.i("xwk", "get nearby task failed");
                return false;
            }else{
                Log.i("xwk", taskList.size() + "");
                return true;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success){
                for(Task task : taskList){
                    LatLng latLng = new LatLng(task.getLocation().getLatitude(), task.getLocation().getLongitude());
                    Marker marker = aMap.addMarker(new MarkerOptions().position(latLng));
                }

            }else {
                Toast.makeText(NearbyTaskActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
        }
    }

//    private Location getLocationByLngLat(double latitude, double longitude){
//        return new Location(0, longitude, latitude, "");
//    }
}
