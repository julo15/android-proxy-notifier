package com.julianlo.example.proxynotifier.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro;
import com.julianlo.example.proxynotifier.R;
import com.julianlo.example.proxynotifier.fragments.IntroFragment;

/**
 * Created by julianlo on 10/15/16.
 */

public class IntroActivity extends AppIntro {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, IntroActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(getString(R.string.first_slide_title, getString(R.string.app_name)),
                R.string.first_slide_description,
                R.drawable.ic_settings_input_antenna_white_48dp,
                R.color.first_slide_background,
                R.color.slide_text);
        addSlide(getString(R.string.check_slide_title),
                R.string.check_slide_description,
                R.drawable.ic_check_white_48dp,
                R.color.notification_proxy_off,
                R.color.slide_text);
        addSlide(getString(R.string.exclamation_slide_title),
                R.string.exclamation_slide_description,
                R.drawable.ic_priority_high_white_48dp,
                R.color.notification_proxy_on,
                R.color.slide_text);
        addSlide(getString(R.string.last_slide_title),
                R.string.last_slide_description,
                R.drawable.ic_play_circle_outline_white_48dp,
                R.color.last_slide_background,
                R.color.slide_text);
    }

    private void addSlide(String title, @StringRes int description, @DrawableRes int image,
                          @ColorRes int background, @ColorRes int text) {
        addSlide(IntroFragment.newInstance(
                title,
                getString(description),
                image,
                ContextCompat.getColor(this, background),
                ContextCompat.getColor(this, text),
                ContextCompat.getColor(this, text)));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        proceed();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        proceed();
    }

    private void proceed() {
        finish();
    }
}
