package com.julianlo.example.proxynotifier.ads;

import com.google.android.gms.ads.AdRequest;

/**
 * Created by julianlo on 10/14/16.
 */

public interface AdRequestBuilder {

    /**
     * Checks its ads are enabled on this build.
     * @return
     */
    boolean areAdsEnabled();

    /**
     * Builds an AdRequest for this build type.
     * Used to ensure test devices are added on development builds.
     * @return
     */
    AdRequest build();
}
