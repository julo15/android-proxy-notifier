package com.julianlo.example.proxynotifier.fragments;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;

import com.github.paolorotolo.appintro.AppIntroBaseFragment;
import com.julianlo.example.proxynotifier.R;

/**
 * Created by julianlo on 10/15/16.
 */

public class IntroFragment extends AppIntroBaseFragment {

    public static IntroFragment newInstance(CharSequence title, CharSequence description,
                                            @DrawableRes int imageDrawable, @ColorInt int bgColor,
                                            @ColorInt int titleColor, @ColorInt int descColor) {
        IntroFragment slide = new IntroFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title.toString());
        args.putString(ARG_TITLE_TYPEFACE, null);
        args.putString(ARG_DESC, description.toString());
        args.putString(ARG_DESC_TYPEFACE, null);
        args.putInt(ARG_DRAWABLE, imageDrawable);
        args.putInt(ARG_BG_COLOR, bgColor);
        args.putInt(ARG_TITLE_COLOR, titleColor);
        args.putInt(ARG_DESC_COLOR, descColor);
        slide.setArguments(args);
        return slide;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_app_intro;
    }
}
