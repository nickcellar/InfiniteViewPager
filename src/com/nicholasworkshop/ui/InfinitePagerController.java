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
import android.widget.FrameLayout;
import android.widget.ImageView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class InfinitePagerController extends PagerAdapter implements OnPageChangeListener
{
    // 2 views, one left and one right
    private final String TAG = getClass().getSimpleName();
    private ImageView[] mDummyImageViews = new ImageView[2];

    private int mCompensateModeCount = 0;
    private int mDirection;
    private int mCurrent = 1;
    private final int LEFT = 0;
    private final int RIGHT = 2;
    private InfiniteViewPager mViewPager;

    /**
     * Constructor of a pager adapter.
     *
     * @param pager The instance of parent InfiniteViewPager.
     */
    public InfinitePagerController(InfiniteViewPager pager)
    {
        mViewPager = pager;
    }

    private void shiftViews()
    {
        do {
            int lastIndex = mViewPager.getViewCount() - 1;
            switch (mDirection) {
                case LEFT:
                    View lastView = mViewPager.getViewAt(lastIndex).getChildAt(0);
                    mViewPager.getViewAt(lastIndex).removeAllViews();
                    for (int i = mViewPager.getViewCount() - 1; i > 0; i--) {
                        View view = mViewPager.getViewAt(i - 1).getChildAt(0);
                        mViewPager.getViewAt(i - 1).removeAllViews();
                        mViewPager.getViewAt(i).addView(view);
                    }
                    mViewPager.getViewAt(0).addView(lastView);
                    break;
                case RIGHT:
                    View firstView = mViewPager.getViewAt(0).getChildAt(0);
                    mViewPager.getViewAt(0).removeAllViews();
                    for (int i = 0; i < mViewPager.getViewCount() - 1; i++) {
                        View view = mViewPager.getViewAt(i + 1).getChildAt(0);
                        mViewPager.getViewAt(i + 1).removeAllViews();
                        mViewPager.getViewAt(i).addView(view);
                    }
                    mViewPager.getViewAt(lastIndex).addView(firstView);
            }
        } while (mViewPager.getViewAt(1).getChildAt(0).getTag() != null);
        mViewPager.setCurrentItem(1, false);
    }


    private void generateDummies()
    {
        if (mCompensateModeCount <= 0) return;
        Log.d(TAG, "Started generating dummies");

        // get display width and height
        Context context = mViewPager.getContext();
        assert context != null;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        // generate images for each page
        for (int i = 0; i < 2; i++) {
            try {
                // image must be build with display size
                View view = mViewPager.getViewAt(i).getChildAt(0);
                assert view != null;
                view.layout(0, 0, width, height);
                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();
                File file = new File(mViewPager.getContext().getCacheDir(), i + "jpg");
                Bitmap bitmap = view.getDrawingCache();
                assert bitmap != null;
                bitmap.compress(CompressFormat.JPEG, 50, new FileOutputStream(file));
                mDummyImageViews[i].setImageDrawable(Drawable.createFromPath(file.getAbsolutePath()));
            }
            catch (FileNotFoundException e) {
                // unable to create bitmap file.
                e.printStackTrace();
            }
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
     * Draw the saved views to the display.
     * <p/>
     * //     * @param collection
     * //     * @param position
     * //     * @return
     */
    @Nullable
    @Override
    public Object instantiateItem(@NotNull View collection, int position)
    {
        if (mViewPager.getViewCount() == 0) return null;
        ((ViewPager) collection).addView(mViewPager.getViewAt(position));
        return mViewPager.getViewAt(position);
    }

    /**
     * Remove the view from display.
     * <p/>
     * //     * @param collection
     * //     * @param position
     * //     * @param view
     */
    @Override
    public void destroyItem(@NotNull View collection, int position, Object view)
    {
        ((ViewPager) collection).removeView((FrameLayout) view);
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
        mDirection = position;
        mCurrent += (mDirection == LEFT) ? -1 : 1;
        int count = mViewPager.getViewCount();
        if (count != 0) mCurrent %= count;
//		if (mInfinitePageListener != null) mInfinitePageListener.onPageChanged(mCurrent);
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
        else if (state == ViewPager.SCROLL_STATE_DRAGGING) generateDummies();
    }
}
