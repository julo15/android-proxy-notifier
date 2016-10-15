package com.julianlo.example.proxynotifier.ads;

import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.julianlo.example.proxynotifier.BuildConfig;

/**
 *  * Created by julianlo on 10/14/16.
 *   */

public class AdRequestBuilderImpl implements AdRequestBuilder {
    @Override
    public boolean areAdsEnabled() {
        return false;
    }

    @Override
    public AdRequest build() {
        return null;
    }
}

