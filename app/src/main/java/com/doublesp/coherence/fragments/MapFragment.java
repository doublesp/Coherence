package com.doublesp.coherence.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.doublesp.coherence.R;
import com.doublesp.coherence.utils.LocationCluster;
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


    public MapFragment() {
        Log.d(TAG, "Constructor");
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter,
            Bundle savedInstanceState) {
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

        getPermissionToAccessLocation();

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        setUpClusterer();

    }

    // Identifier for the permission request
    private static final int ACCESS_FINE_LOCATION_PERMISSIONS_REQUEST = 1;

    // Called when the user is performing an action which requires the app to read the
    // user's contacts
    public void getPermissionToAccessLocation() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

//            // The permission is NOT already granted.
//            // Check if the user has been asked about this permission already and denied
//            // it. If so, we want to give more explanation about why the permission is needed.
//            if (shouldShowRequestPermissionRationale(
//                    Manifest.permission.READ_CONTACTS)) {
//                // Show our own UI to explain to the user why we need to read the contacts
//                // before actually requesting the permission and showing the default UI
//            }

            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_PERMISSIONS_REQUEST);
        }
    }

    // Callback with the request from calling requestPermissions(...)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == ACCESS_FINE_LOCATION_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Access location permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // showRationale = false if user clicks Never Ask Again, otherwise true
                boolean showRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS);

                if (showRationale) {
                    // do something here to handle degraded mode
                } else {
                        Toast.makeText(getActivity(), "Access location permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
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
