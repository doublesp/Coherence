package com.doublesp.coherence.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
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
import com.doublesp.coherence.googleplace.GooglePlaceClient;
import com.doublesp.coherence.googleplace.gplace.GPlace;
import com.doublesp.coherence.googleplace.gplace.Result;
import com.doublesp.coherence.utils.LocationCluster;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.IllegalFormatConversionException;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MapFragment extends Fragment implements LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, ClusterManager.OnClusterClickListener<LocationCluster>, ClusterManager.OnClusterItemClickListener<LocationCluster> {

    private static String TAG = MapFragment.class.getSimpleName();

    // map
    MapView mMapView;
    GoogleMap mMap;
    boolean mIsMapInit;
    // Declare a variable for the cluster manager.
    private ClusterManager<LocationCluster> mClusterManager;


    private GoogleApiClient mGoogleApiClient;
    private ArrayList<Result> mStoreList;
    private Subscription searchNearbyStoresSubscription;
    private LatLng lastKnownLocation;
    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */


    public MapFragment() {
        Log.d(TAG, "Constructor");
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStoreList = new ArrayList<>();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
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
        mClusterManager = new ClusterManager<>(getContext(), mMap);
        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);

        mClusterManager.cluster();


//        // Add cluster items (markers) to the cluster manager.
//        addItems();
    }

    private void addItems(List<Result> stores) {

        mClusterManager.clearItems();
        for (Result store : stores) {
            com.doublesp.coherence.googleplace.gplace.Location location = store.getGeometry().getLocation();
            Log.d(TAG, "Add store to cluster master: " + location.toString());
            mClusterManager.addItem(new LocationCluster(store));
        }
        mClusterManager.cluster();
    }

    @Override
    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
//        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps

        LatLng temp = new LatLng(location.getLatitude(), location.getLongitude());

        Location loc1 = new Location("");
        loc1.setLatitude(temp.latitude);
        loc1.setLongitude(temp.longitude);

        if (lastKnownLocation != null) {
            Location loc2 = new Location("");
            loc2.setLatitude(lastKnownLocation.latitude);
            loc2.setLongitude(lastKnownLocation.longitude);

            Log.d(TAG, "lastKnownLocation: " + lastKnownLocation);

            float distanceInMeters = loc1.distanceTo(loc2);
            if (distanceInMeters < 10) {
                return;
            }
        }
        lastKnownLocation = temp;


        String latAndLng;

        latAndLng = (lastKnownLocation != null) ? Double.toString(lastKnownLocation.latitude) + "," +
                Double.toString(lastKnownLocation.longitude) : null;

        Log.d(TAG, "Move camera to: " + lastKnownLocation);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLocation, 13));

        subscribeNearbyStores(latAndLng);

    }

    @Override
    public void onConnected(Bundle dataBundle) {
        // Get last known recent location.
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        // Note that this can be NULL if last location isn't already known.
        if (mCurrentLocation != null) {
            // Print current location if not null
            Log.d("DEBUG", "current location: " + mCurrentLocation.toString());
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        }
        // Begin polling for new location updates.
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(getActivity(), "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(getActivity(), "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    // Trigger new location updates at interval
    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        // Request location updates
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void subscribeNearbyStores(String latAndLng) {
        searchNearbyStoresSubscription = GooglePlaceClient.newInstance()
                .searchStores(latAndLng)
                .subscribeOn(Schedulers.io()) // optional if you do not wish to override the default behavior
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SearchNearbyStoresSubscriber(true));
    }

    @Override
    public boolean onClusterClick(Cluster<LocationCluster> cluster) {
        // Show a toast with some info when the cluster is clicked.
        Log.d(TAG, "onClusterClick()");
        float currentZoom = mMap.getCameraPosition().zoom;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cluster.getPosition(), currentZoom + 1));
        return true;
    }

    @Override
    public boolean onClusterItemClick(LocationCluster locationCluster) {
        Log.d(TAG, "onClusterItemClick: " + locationCluster);
        Result store = locationCluster.getStore();
        Toast.makeText(getActivity(), store.getName() + "\n" + store.getVicinity(), Toast.LENGTH_SHORT).show();

        return true;
    }

    private class SearchNearbyStoresSubscriber extends Subscriber<GPlace> {
        private boolean clearOldList;

        public SearchNearbyStoresSubscriber(boolean clearOldList) {
            this.clearOldList = clearOldList;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof HttpException) {
                HttpException response = (HttpException) e;
                int code = response.code();
                Log.d(TAG, "Rx Subscriber error with code: " + code);
            } else if (e instanceof IllegalFormatConversionException) {

            }

        }

        @Override
        public void onNext(GPlace response) {
            List<Result> results = response.getResults();
            Log.d(TAG, "results: " + results.size());
            if (clearOldList) {
                mStoreList.clear();
            }
            mStoreList.addAll(results);

            addItems(mStoreList);
        }
    }

}
