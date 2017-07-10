package com.xw.sample.slidinguppanellayout.demo1;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xw.repo.SlidingUpPanelLayout;
import com.xw.sample.slidinguppanellayout.R;

/**
 * <p/>
 * Created by woxingxiao on 2017-07-10.
 */

public class WeatherSlidingPanelView extends BasePanelView implements View.OnClickListener {

    private View mContentLayout;
    private View mMenuLayout;
    private View mExpendLayout;
    private ImageView mCollapseImg;
    private ImageView mSettingsImg;
    private TextView mCityText;
    private ImageView mWeatherIcon;
    private TextView mWeatherDescText;
    private TextView mTempNowText;
    private TextView mAqiDescText;
    private TextView mTempRangeText;

    private View mCollapseLayout;
    private TextView mCityTextCollapse;
    private TextView mWeatherDescTextCollapse;
    private TextView mTempNowTextCollapse;
    private ImageView mWeatherIconCollapse;

    private int mWeatherTypeCode;

    public WeatherSlidingPanelView(Context context) {
        this(context, null);
    }

    public WeatherSlidingPanelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeatherSlidingPanelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.content_weather_panel_view, this, true);
        mContentLayout = findViewById(R.id.panel_content_layout);
        mMenuLayout = findViewById(R.id.panel_menu_layout);
        mExpendLayout = findViewById(R.id.panel_expend_layout);
        mCollapseImg = (ImageView) findViewById(R.id.panel_collapse_img);
        mSettingsImg = (ImageView) findViewById(R.id.panel_settings_img);
        mCityText = (TextView) findViewById(R.id.panel_city_text);
        mWeatherIcon = (ImageView) findViewById(R.id.panel_weather_icon);
        mWeatherDescText = (TextView) findViewById(R.id.panel_weather_desc_text);
        mTempNowText = (TextView) findViewById(R.id.panel_temp_now_text);
        mAqiDescText = (TextView) findViewById(R.id.panel_air_condition_text);
        mTempRangeText = (TextView) findViewById(R.id.panel_temp_range_text);
        mCollapseLayout = findViewById(R.id.panel_collapse_layout);
        mCityTextCollapse = (TextView) findViewById(R.id.panel_city_text_collapse);
        mWeatherDescTextCollapse = (TextView) findViewById(R.id.panel_weather_desc_text_collapse);
        mTempNowTextCollapse = (TextView) findViewById(R.id.panel_temp_now_collapse);
        mWeatherIconCollapse = (ImageView) findViewById(R.id.panel_weather_icon_collapse);
        mCollapseImg.setOnClickListener(this);
        mSettingsImg.setOnClickListener(this);

        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = getResources().getDimensionPixelSize(resId);
        mMenuLayout.setPadding(0, statusBarHeight, 0, 0);
        mExpendLayout.setPadding(0, statusBarHeight, 0, 0);

        checkVisibilityOfViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.panel_collapse_img:
                if (mCollapseImg.getAlpha() == 1) {
                    ((SlidingUpPanelLayout) getParent()).collapsePanel();
                }

                break;
            case R.id.panel_settings_img:
                if (mSettingsImg.getAlpha() == 1) {

                }

                break;
        }
    }

    @Override
    public void setSlideState(@SlidingUpPanelLayout.SlideState int slideState) {
        super.setSlideState(slideState);

        checkVisibilityOfViews();
    }

    @Override
    public void onSliding(int top, int dy) {
        if (dy < 0) { // 向上
            float radius = getRadius();
            if (radius > 0 && MAX_RADIUS >= top) {
                setRadius(top);
            }

            float alpha = mCollapseLayout.getAlpha();
            if (alpha > 0f && top < 200) {
                alpha += dy / 200.0f;
                mCollapseLayout.setAlpha(alpha < 0 ? 0 : alpha); // 逐隐
            }

            alpha = mMenuLayout.getAlpha();
            if (alpha < 1f && top < 100) {
                alpha -= dy / 100.0f;
                mMenuLayout.setAlpha(alpha > 1 ? 1 : alpha); // 逐显
            }

            alpha = mExpendLayout.getAlpha();
            if (alpha < 1f) {
                alpha -= dy / 1000.0f;
                mExpendLayout.setAlpha(alpha > 1 ? 1 : alpha); // 逐显
            }
        } else { // 向下
            float radius = getRadius();
            if (radius < MAX_RADIUS) {
                radius += top;
                setRadius(radius > MAX_RADIUS ? MAX_RADIUS : radius);
            }

            float alpha = mCollapseLayout.getAlpha();
            if (alpha < 1f) {
                alpha += dy / 800.0f;
                mCollapseLayout.setAlpha(alpha > 1 ? 1 : alpha); // 逐显
            }

            alpha = mMenuLayout.getAlpha();
            if (alpha > 0f) {
                alpha -= dy / 100.0f;
                mMenuLayout.setAlpha(alpha < 0 ? 0 : alpha); // 逐隐
            }

            alpha = mExpendLayout.getAlpha();
            if (alpha > 0f) {
                alpha -= dy / 1000.0f;
                mExpendLayout.setAlpha(alpha < 0 ? 0 : alpha); // 逐隐
            }
        }
    }

    @Override
    public void setWeatherModel(WeatherModel weather) {
        if (weather == null)
            return;

        mCityText.setText(weather.getCity());
        mCityTextCollapse.setText(weather.getCity());
        mWeatherTypeCode = weather.getCode();
        if (mWeatherTypeCode == 1) {
            mWeatherIcon.setImageResource(R.drawable.svg_ic_sunny_cloudy);
            mWeatherIconCollapse.setImageResource(R.drawable.svg_ic_sunny_cloudy);
            mWeatherDescText.setText("多云");
            mWeatherDescTextCollapse.setText("多云");
        } else if (mWeatherTypeCode == 2) {
            mWeatherIcon.setImageResource(R.drawable.svg_ic_rainy);
            mWeatherIconCollapse.setImageResource(R.drawable.svg_ic_rainy);
            mWeatherDescText.setText("雨");
            mWeatherDescTextCollapse.setText("雨");
        } else {
            mWeatherIcon.setImageResource(R.drawable.svg_ic_sunny);
            mWeatherIconCollapse.setImageResource(R.drawable.svg_ic_sunny);
            mWeatherDescText.setText("晴");
            mWeatherDescTextCollapse.setText("晴");
        }
        mTempNowText.setText(weather.getTempNow());
        mTempNowTextCollapse.setText(weather.getTempNow());
        String s = weather.getTempMin() + " / " + weather.getTempMax();
        mTempRangeText.setText(s);
        mAqiDescText.setText(weather.getAqiDesc());

        checkVisibilityOfViews();
    }

    private void checkVisibilityOfViews() {
        if (mWeatherTypeCode == 1) {
            mContentLayout.setBackgroundColor(Color.parseColor("#80DEEA"));
        } else if (mWeatherTypeCode == 2) {
            mContentLayout.setBackgroundColor(Color.parseColor("#78909C"));
        } else {
            mContentLayout.setBackgroundColor(Color.parseColor("#03A9F4"));
        }

        if (mSlideState == SlidingUpPanelLayout.COLLAPSED) {
            setRadius(MAX_RADIUS);

            mMenuLayout.setAlpha(0f);
            mExpendLayout.setAlpha(0f);
            mCollapseLayout.setAlpha(1f);
        } else if (mSlideState == SlidingUpPanelLayout.EXPANDED) {
            setRadius(0);

            mMenuLayout.setAlpha(1f);
            mExpendLayout.setAlpha(1f);
            mCollapseLayout.setAlpha(0f);
        }
    }
}
