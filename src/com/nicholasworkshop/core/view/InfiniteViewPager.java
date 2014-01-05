package com.nicholasworkshop.core.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import java.util.Vector;

/**
 * @author Nicholas Wong
 */
public class InfiniteViewPager extends ViewPager
{
    private InfinitePagerController mPageViewAdapter;
    private Vector<String> mViewTitles = new Vector<String>();
    private Vector<View> mViews = new Vector<View>();
    private InfinitePagerListener mInfinitePageListener;

    /**
     * Constructor
     *
     * @param context
     * @param attrs
     */
    public InfiniteViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mPageViewAdapter = new InfinitePagerController(this);
        setOnPageChangeListener(mPageViewAdapter);
        setAdapter(mPageViewAdapter);
    }

    /**
     * Function to add a view to this pager. For each view added to this pager,
     * it will be wrapped by a FrameLayout.
     *
     * @param title
     * @param view
     */
    public void addPage(String title, View view)
    {
        mViews.add(view);
        mViewTitles.add(title);
        mPageViewAdapter.notifyDataSetChanged();
//        mPageViewAdapter.redraw();
    }

    /**
     * Add a view without a title. "Untitled" will be used instead.
     *
     * @param view
     */
    public void addPage(View view)
    {
        addPage("Untitled", view);
    }

    /**
     * Get view at index.
     *
     * @param index
     * @return
     */
    public View getViewAt(int index)
    {
        return mViews.get(index);
    }

    /**
     * Get total number of views.
     *
     * @return
     */
    public int getViewCount()
    {
        return mViews.size();
    }

    /**
     * Return the list of titles.
     *
     * @return
     */
    @Deprecated
    public String[] getTitles()
    {
        return mViewTitles.toArray(new String[1]);
    }

    /**
     * Set listener to this pager.
     *
     * @param listener
     */
    public void setListener(InfinitePagerListener listener)
    {
        mInfinitePageListener = listener;
    }

}
