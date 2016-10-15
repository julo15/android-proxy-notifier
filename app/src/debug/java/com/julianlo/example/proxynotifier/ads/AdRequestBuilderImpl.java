package com.julianlo.example.proxynotifier.ads;

import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.julianlo.example.proxynotifier.BuildConfig;

/**
 *  * Created by julianlo on 10/14/16.
 *   */

public class AdRequestBuilderImpl implements AdRequestBuilder {
    public AdRequest build() {
        return new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(BuildConfig.ADMOB_TEST_DEVICE_ID)
                .build();
    }
}
