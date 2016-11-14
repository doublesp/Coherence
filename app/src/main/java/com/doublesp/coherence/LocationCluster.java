package com.doublesp.coherence;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by sduan on 11/13/16.
 */

public class LocationCluster implements ClusterItem {

    private final LatLng mPosition;

    public LocationCluster(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}
