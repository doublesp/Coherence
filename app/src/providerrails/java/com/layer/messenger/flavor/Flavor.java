package com.layer.messenger.flavor;

import android.content.Context;

import com.layer.messenger.App;
import com.layer.messenger.R;
import com.layer.messenger.flavor.util.CustomEndpoint;
import com.layer.messenger.util.AuthenticationProvider;
import com.layer.messenger.util.Log;
import com.layer.sdk.LayerClient;

public class Flavor implements App.Flavor {
    // Set your Layer App ID from your Layer Developer Dashboard.
    public final static String LAYER_APP_ID = null;

    @Override
    public String getLayerAppId() {
        return (LAYER_APP_ID != null) ? LAYER_APP_ID : CustomEndpoint.getLayerAppId();
    }

    @Override
    public LayerClient generateLayerClient(Context context, LayerClient.Options options) {
        String layerAppId = getLayerAppId();
        if (layerAppId == null) {
            if (Log.isLoggable(Log.ERROR)) Log.e(context.getString(R.string.app_id_required));
            return null;
        }
        options.useFirebaseCloudMessaging(true);
        CustomEndpoint.setLayerClientOptions(options);
        return LayerClient.newInstance(context, layerAppId, options);
    }

    @Override
    public AuthenticationProvider generateAuthenticationProvider(Context context) {
        return new RailsAuthenticationProvider(context);
    }
}
