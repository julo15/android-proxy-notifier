package com.julo.android.proxynotifier.ads;

import com.google.android.gms.ads.AdRequest;
import com.julo.android.proxynotifier.BuildConfig;

/**
 *  * Created by julianlo on 10/14/16.
 *   */

public class AdRequestBuilderImpl implements AdRequestBuilder {

    @Override
    public boolean areAdsEnabled() {
        return true;
    }

    @Override
    public AdRequest build() {
        return new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(BuildConfig.ADMOB_TEST_DEVICE_ID)
                .build();
    }
}

