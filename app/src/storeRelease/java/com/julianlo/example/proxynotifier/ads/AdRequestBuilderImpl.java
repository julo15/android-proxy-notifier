package com.julianlo.example.proxynotifier.ads;

import com.google.android.gms.ads.AdRequest;

/**
 *  * Created by julianlo on 10/14/16.
 *   */

public class AdRequestBuilderImpl implements AdRequestBuilder {
    public AdRequest build() {
        return new AdRequest.Builder().build();
    }
}

