package com.nicholasworkshop.core.test;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import com.nicholasworkshop.core.view.InfinitePagerAdapter;
import com.nicholasworkshop.core.view.OnInfinitePageChangeListener;

import java.util.Vector;

public class InfiniteViewPagerTestActivity extends Activity implements OnInfinitePageChangeListener
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // create views and add them to controller
        Vector<View> views = new Vector<View>();
        int[] colors = new int[]{Color.RED, Color.BLUE, Color.GREEN};
        for (int i = 0; i < 9; i++) {
            TextView view = new TextView(this);
            view.setText(String.valueOf(i));
            view.setTextColor(Color.WHITE);
            view.setTextSize(128);
            view.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            view.setBackgroundColor(colors[i % colors.length]);
            views.add(view);
        }

        // create a controller and put it as adapter of view pager
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        InfinitePagerAdapter adapter = new InfinitePagerAdapter(pager);
        adapter.setViews(views);
        adapter.setOnInfinitePageChangeListener(this);
        pager.setAdapter(adapter);
    }

    @Override
    public void onPageChanged(int position)
    {
        TextView view = (TextView) findViewById(R.id.status_bar);
        view.setText("Current Position: " + position);
    }
}