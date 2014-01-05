package com.nicholasworkshop.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class InfinitePagerController extends PagerAdapter implements OnPageChangeListener
{
    private final String TAG = getClass().getSimpleName();
    private int mDirection;
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
        mViewPager = pager;

        // ensure that pager only has 3 views
        pager.removeAllViews();
        for (int i = 0; i < 3; i++) {
            mFrames[i] = new FrameLayout(pager.getContext());
            TextView tv = new TextView(pager.getContext());
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

        // filling views into frames
        int count = mViewPager.getViewCount();
        for (int i = 0; i < 3; i++) {
            int index = (mCurrent + i - 1 + count) % count;
            Log.w(TAG, "Frame " + i + " -> View " + index);
            View view = mViewPager.getViewAt(index);
            mFrames[i].addView(view);
        }

        // reset the current position to center
        mViewPager.setCurrentItem(1, false);
    }


    private void generateDummies()
    {
        Log.d(TAG, "Started generating dummies");

        // get display width and height
        Context context = mViewPager.getContext();
        assert context != null;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        int count = mViewPager.getViewCount();
        ImageView[] dummies = new ImageView[3];

        // generate images for each page
        for (int i = 0; i < count; i++) {
            try {
                // generate bitmap
                // image must be build with display size
                View view = mViewPager.getViewAt(i);
                assert view != null;
                view.layout(0, 0, width, height);
                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();
                Bitmap bitmap = view.getDrawingCache();

                // create file
                assert bitmap != null;
                File file = new File(context.getCacheDir(), "infinite-view-" + i);
                bitmap.compress(CompressFormat.JPEG, 50, new FileOutputStream(file));

                // create dummy
                dummies[i] = new ImageView(context);
                dummies[i].setImageDrawable(Drawable.createFromPath(file.getAbsolutePath()));
            }
            catch (FileNotFoundException e) {
                // unable to create bitmap file.
                e.printStackTrace();
            }
        }

        // stick dummies on the null pages
        for (int i = count - 1; i < 3; i++) {
//            ViewGroup group = mViewPager.getViewAt(i);
//            group.removeAllViews();
//            group.addView(dummies[i % count]);
        }
        Log.d(TAG, "Generated dummies");
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
     *
//     * @param container
//     * @param position
//     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        return mFrames[position];
    }

    /**
     * Remove frames from display.
     *
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
        mDirection = position - 1;

        // calculate current position
        int count = mViewPager.getViewCount();
        mCurrent = (mCurrent + mDirection + count) % count;

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
//        else
//        if (mViewPager.getViewCount() < 3 && state == ViewPager.SCROLL_STATE_DRAGGING) generateDummies();
    }
}
