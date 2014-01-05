package com.nicholasworkshop.core.test;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import com.nicholasworkshop.core.view.InfiniteViewPager;

public class InfiniteViewPagerTestActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        InfiniteViewPager mInfinitePageView = (InfiniteViewPager) findViewById(R.id.InfinitePageView);

        int[] colors = new int[]{Color.RED, Color.BLUE, Color.GREEN};

        for (int i = 0; i < 1; i++) {
            TextView view = new TextView(this);
            view.setText(String.valueOf(i));
            view.setTextColor(Color.WHITE);
            view.setTextSize(128);
            view.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            view.setBackgroundColor(colors[i % colors.length]);
            mInfinitePageView.addPage(view);
        }
    }
}