package com.nicholasworkshop.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class InfinitePagerController extends PagerAdapter implements OnPageChangeListener
{
    private final String TAG = getClass().getSimpleName();
    private int mCurrent = 0;
    private InfiniteViewPager mViewPager;
    private InfinitePagerListener mListener;
    private FrameLayout[] mFrames = new FrameLayout[3];

    /**
     * Constructor of a pager adapter.
     *
     * @param pager The instance of parent InfiniteViewPager.
     */
    public InfinitePagerController(InfiniteViewPager pager)
    {
        // store the instance of view pager
        mViewPager = pager;

        // ensure that pager only has 3 views
        pager.removeAllViews();
        Context context = pager.getContext();
        assert context != null;
        for (int i = 0; i < 3; i++) {
            mFrames[i] = new FrameLayout(context);
            TextView tv = new TextView(context);
            tv.setText("Page" + i);
            mFrames[i].addView(tv);
            pager.addView(mFrames[i]);
        }
    }

    public void setPagerListener(InfinitePagerListener listener)
    {
        mListener = listener;
    }

    private void shiftViews()
    {
        // remove all views from frames first
        // to prevent views from having multiple parents
        for (int i = 0; i < 3; i++) {
            mFrames[i].removeAllViews();
        }

        // fill views into frames
        int count = mViewPager.getViewCount();
        for (int i = 0; i < 3; i++) {
            int index = (mCurrent + i - 1 + count) % count;
            Log.w(TAG, "Frame " + i + " -> View " + index);
            View view = mViewPager.getViewAt(index);
            ViewParent parent = view.getParent();
            if (parent != null) {
                // if view has parent, fill it with generated dummy
                ImageView dummy = generateDummy(view);
                mFrames[i].addView(dummy);
            } else {
                mFrames[i].addView(view);
            }
        }

        // reset the current position to center
        mViewPager.setCurrentItem(1, false);
    }

    private ImageView generateDummy(View view)
    {
        // get context from view pager
        Context context = mViewPager.getContext();
        assert context != null;

        // build bitmap
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        // create dummy image from bitmap generated
        assert bitmap != null;
        ImageView dummy = new ImageView(context);
        dummy.setImageBitmap(bitmap);
        return dummy;
    }

    // ===================================================
    // PagerAdapter
    // ===================================================

    /**
     * Always return 3.
     *
     * @return Number of view saved.
     */
    @Override
    public int getCount()
    {
        return 3;
    }

    /**
     * Draw frames to the display.
     * <p/>
     * //     * @param container
     * //     * @param position
     * //     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        return mFrames[position];
    }

    /**
     * Remove frames from display.
     * <p/>
     * //     * @param container
     * //     * @param position
     * //     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        View view = (View) object;
        container.removeView(view);
    }

    /**
     * Check if the view equals the one in saved views.
     * <p/>
     * //     * @param view
     * //     * @param object
     * //     * @return
     */
    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }

    // ===================================================
    // OnPageChangeListener
    // ===================================================

    /**
     * Whenever a page is selected (scrolled to), updated the current direction.
     */
    @Override
    public void onPageSelected(int position)
    {
        // calculate the direction
        // left if equals -1
        // right if equals 1
        int direction = position - 1;

        // calculate current position
        int count = mViewPager.getViewCount();
        mCurrent = (mCurrent + direction + count) % count;

        // callback function
        if (mListener != null) mListener.onPageChanged(mCurrent);
    }

    /**
     * //     * @param position
     * //     * @param positionOffset
     * //     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
    }

    /**
     * Handle the events when pager is scrolling or not.
     */
    @Override
    public void onPageScrollStateChanged(int state)
    {
        if (state == ViewPager.SCROLL_STATE_IDLE) shiftViews();
    }
}
