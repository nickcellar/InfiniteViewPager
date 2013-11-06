package com.nicholasworkshop.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class InfiniteViewPager extends ViewPager
{

	private ImageView[] mCompensateImageViews;

	private InfinitePagerController mPageViewAdapter;

	// todo: use another type.
	private LinkedList<String> mViewTitles = new LinkedList<String>();
	private LinkedList<FrameLayout> mViews = new LinkedList<FrameLayout>();

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
//		this.mCompensateImageViews = new ImageView[2];
		this.mPageViewAdapter = new InfinitePagerController(this);
		this.setOnPageChangeListener(mPageViewAdapter);
		this.setAdapter(mPageViewAdapter);
		this.setCurrentItem(1, false);
	}

//	public void setCompensateImageView(int index, ImageView view)
//	{
//		mCompensateImageViews[index] = view;
//	}

	/**
	 * Function to add a view to this pager. For each view added to this pager,
	 * it will be wrapped by a FrameLayout.
	 * 
	 * @param title
	 * @param view
	 */
	public void addPage(String title, View view)
	{
		FrameLayout frameLayout = new FrameLayout(this.getContext());
		frameLayout.addView(view);
		mViews.add(frameLayout);
		mViewTitles.add(title);
		mPageViewAdapter.notifyDataSetChanged();
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

	public FrameLayout getViewAt(int index)
	{
		return mViews.get(index);
	}

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

//	public void setCompensateModeCount(int count)
//	{
//		mCompensateModeCount = count;
//	}

}
