package com.xw.sample.slidinguppanellayout.demo1;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.xw.repo.ISlidingUpPanel;
import com.xw.repo.SlidingUpPanelLayout;
import com.xw.sample.slidinguppanellayout.R;
import com.xw.sample.slidinguppanellayout.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xw.repo.SlidingUpPanelLayout.COLLAPSED;
import static com.xw.repo.SlidingUpPanelLayout.EXPANDED;
import static com.xw.repo.SlidingUpPanelLayout.HIDDEN;

public class DemoActivity1 extends AppCompatActivity {

    @BindView(R.id.sliding_up_panel_layout)
    SlidingUpPanelLayout mSlidingUpPanelLayout;
    @BindView(R.id.bg_layout)
    View mBgLayout;

    private List<WeatherModel> mWeatherList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // transparent status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_demo1);
        ButterKnife.bind(this);

        mSlidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListenerAdapter() {
            @Override
            public void onPanelExpanded(ISlidingUpPanel panel) {
                // 被展开的Panel非最上层Panel，做如下处理，再被收起时已在最上层
                if (panel instanceof BasePanelView) {

                    int count = mSlidingUpPanelLayout.getChildCount();
                    if (((BasePanelView) panel).getFloor() != count - 1) {

                        mSlidingUpPanelLayout.removeView(panel.getPanelView());
                        mSlidingUpPanelLayout.addView(panel.getPanelView(), 1);

                        for (int i = 1; i < count; i++) {
                            BasePanelView child = (BasePanelView) mSlidingUpPanelLayout.getChildAt(i);
                            child.setFloor(count - i);
                        }
                        mSlidingUpPanelLayout.requestLayout();
                    }
                }

                int count = mSlidingUpPanelLayout.getChildCount();
                for (int i = 1; i < count; i++) {
                    ISlidingUpPanel panel2 = (ISlidingUpPanel) mSlidingUpPanelLayout.getChildAt(i);
                    if (panel2 == panel) {
                        panel2.getPanelView().setEnabled(false);
                    } else {
                        panel2.setSlideState(HIDDEN);
                        panel2.getPanelView().setEnabled(true);
                    }
                }
            }

            @Override
            public void onPanelCollapsed(ISlidingUpPanel panel) {
                int count = mSlidingUpPanelLayout.getChildCount();
                for (int i = 1; i < count; i++) {
                    panel = (ISlidingUpPanel) mSlidingUpPanelLayout.getChildAt(i);
                    panel.setSlideState(COLLAPSED);
                    panel.getPanelView().setEnabled(true);
                }
            }
        });

        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = getResources().getDimensionPixelSize(resId);
        mBgLayout.setPadding(0, statusBarHeight, 0, 0);

        loadData();
    }

    private void loadData() {
        mWeatherList.clear();
        mWeatherList.add(new WeatherModel("成都", 0, "晴", "24℃", "20℃", "28℃", "优"));
        mWeatherList.add(new WeatherModel("北京", 1, "多云", "26℃", "22℃", "32℃", "良"));
        mWeatherList.add(new WeatherModel("上海", 2, "雨", "20℃", "18℃", "25℃", "良"));

        mSlidingUpPanelLayout.setAdapter(new SlidingUpPanelLayout.Adapter() {

            private int mSize = mWeatherList.size();

            @Override
            public int getItemCount() {
                return mSize;
            }

            @NonNull
            @Override
            public ISlidingUpPanel onCreateSlidingPanel(int position) {
                WeatherSlidingPanelView panel = new WeatherSlidingPanelView(DemoActivity1.this);
                panel.setFloor(mSize - position);
                panel.setPanelHeight(mSize == 1 ? Util.dp2px(120) : Util.dp2px(80));
                if (position == 0) {
                    panel.setSlideState(EXPANDED);
                    panel.setEnabled(false);
                } else {
                    panel.setSlideState(HIDDEN);
                    panel.setEnabled(true);
                }

                return panel;
            }

            @Override
            public void onBindView(final ISlidingUpPanel panel, int position) {
                if (mSize == 0)
                    return;

                BasePanelView BasePanel = (BasePanelView) panel;
                BasePanel.setWeatherModel(mWeatherList.get(position));
                BasePanel.setClickable(true);
                BasePanel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (panel.getSlideState() != EXPANDED) {
                            mSlidingUpPanelLayout.expandPanel();
                        } else {
                            mSlidingUpPanelLayout.collapsePanel();
                        }
                    }
                });
            }
        });
    }

    @OnClick(R.id.add_city_text)
    public void onViewClicked() {
    }
}
