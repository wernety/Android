package com.example.mymap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.layers.WebTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "chx";
    MapView mMapView;
    private LocationDisplay mLocationDisplay;
    private View textview;
    private View textview2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.setAttributionTextVisible(false);
        textview =  findViewById(R.id.tv);
        textview2 = findViewById(R.id.tv2);


        WebTiledLayer webTiledLayer2 = Tianditu.CreateTianDiTuTiledLayer(Tianditu.LayerType.TIANDITU_IMAGE_2000);
        Basemap tdtBasemap2 = new Basemap(webTiledLayer2);
        WebTiledLayer webTiledLayer22 = Tianditu.CreateTianDiTuTiledLayer(Tianditu.LayerType.TIANDITU_IMAGE_ANNOTATION_CHINESE_2000);
        tdtBasemap2.getBaseLayers().add(webTiledLayer22);


        ArcGISMap map = new ArcGISMap(tdtBasemap2);
        mMapView.setMap(map);
        mMapView.setViewpoint(new Viewpoint(34.77669, 113.67922, 10000));

        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }

        mLocationDisplay = mMapView.getLocationDisplay();
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);

        if (!mLocationDisplay.isStarted())
            mLocationDisplay.startAsync();
//        mLocationDisplay.addLocationChangedListener(new LocationDisplay.LocationChangedListener() {
//            @Override
//            public void onLocationChanged(LocationDisplay.LocationChangedEvent locationChangedEvent) {
//                double longitude = locationChangedEvent.getLocation().getPosition().getX();
//                double latitude = locationChangedEvent.getLocation().getPosition().getY();
//                Snackbar.make(mMapView, longitude + " " + latitude, Snackbar.LENGTH_LONG).show();
//            }
//        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.dispose();
        if (mLocationDisplay.isStarted())
            mLocationDisplay.stop();
    }
}