package com.doublesp.coherence.utils;

import com.doublesp.coherence.googleplace.gplace.Location;
import com.doublesp.coherence.googleplace.gplace.Result;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class LocationClusterItem implements ClusterItem {

    private final LatLng mPosition;
    private final Result mStore;

    public LocationClusterItem(Result store) {
        mStore = store;
        Location location = store.getGeometry().getLocation();
        mPosition = new LatLng(location.getLat(), location.getLng());
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public Result getStore() {
        return mStore;
    }
}
