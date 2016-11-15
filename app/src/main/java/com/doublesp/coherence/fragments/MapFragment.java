package com.doublesp.coherence.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doublesp.coherence.utils.LocationCluster;
import com.doublesp.coherence.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static String TAG = MapFragment.class.getSimpleName();

    // map
    MapView mMapView;
    GoogleMap mMap;
    boolean mIsMapInit;
    // Declare a variable for the cluster manager.
    private ClusterManager<LocationCluster> mClusterManager;


    public static MapFragment newInstance() {
        return new MapFragment();
    }

    public MapFragment() {
        Log.d(TAG, "Constructor");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle savedInstanceState) {
        super.onCreateView(inflater, containter, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_map, containter, false);
        mMapView = (MapView) view.findViewById(R.id.mapView);

        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(MapFragment.this);
        return view;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();

        // map stuff
        mMapView.onResume();
        try {
            if (!mIsMapInit) {
                MapsInitializer.initialize(this.getActivity());
                mIsMapInit = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();

        // map stuff
        mMapView.onPause();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.d(TAG, "onMapReady()");

        UiSettings uiSettings;

        mMap = map;
        uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        setUpClusterer();

    }

    private void setUpClusterer() {

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<LocationCluster>(getContext(), mMap);
        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }

    private void addItems() {

        // Set some lat/lng coordinates to start with.
        double lat = 51.5145160;
        double lng = -0.1270060;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            LocationCluster offsetItem = new LocationCluster(lat, lng);
            mClusterManager.addItem(offsetItem);
        }
    }

}
