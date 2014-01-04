package com.nicholasworkshop.test;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.nicholasworkshop.ui.InfiniteViewPager;

public class MainActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        InfiniteViewPager mInfinitePageView = (InfiniteViewPager) findViewById(R.id.InfinitePageView);

        for (int i = 0; i < 3; i++) {
            TextView textView = new TextView(this);
            textView.setText("Page " + i);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(30);
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