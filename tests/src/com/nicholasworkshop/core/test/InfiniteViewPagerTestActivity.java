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

        for (int i = 0; i < 1; i++) {
            TextView textView = new TextView(this);
            textView.setText("" + i);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(128);
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            switch (i % 3) {
                case 0:
                    textView.setBackgroundColor(Color.RED);
                    break;
                case 1:
                    textView.setBackgroundColor(Color.BLUE);
                    break;
                case 2:
                    textView.setBackgroundColor(Color.GREEN);
                    break;
            }
            mInfinitePageView.addPage(textView);
        }
    }
}