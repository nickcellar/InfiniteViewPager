package com.nicholasworkshop.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class InfinitePagerAdapter extends PagerAdapter
{
	private final String CACHE_DIR = Environment.getExternalStorageDirectory() + "/.nicholasworkshop/cache/";

	private InfinitePageView mViewPager;

	public InfinitePagerAdapter(InfinitePageView pager)
	{
		mViewPager = pager;
	}

	/**
	 * Always return 3.
	 * 
	 * @return
	 */
	@Override
	public int getCount()
	{
		if (mViewPager.getViewCount() == 1 || mViewPager.getViewCount() == 2) {
			mViewPager.setCompensateModeCount(mViewPager.getViewCount());
			for (int i = 0; i < 2; i++) {

				Display display = ((Activity) mViewPager.getContext()).getWindowManager().getDefaultDisplay();
				mViewPager.getViewAt(i).getChildAt(0).layout(0, 0, display.getWidth(), display.getHeight());
				mViewPager.getViewAt(i).getChildAt(0).setDrawingCacheEnabled(true);
				mViewPager.getViewAt(i).getChildAt(0).buildDrawingCache();
				Bitmap b = mViewPager.getViewAt(i).getChildAt(0).getDrawingCache();

				String path = CACHE_DIR + i + ".jpg";
				new File(CACHE_DIR).mkdirs();
				try {
					b.compress(CompressFormat.JPEG, 95, new FileOutputStream(path));
				}
				catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				LayoutParams layout = new LayoutParams();
				layout.height = display.getHeight();
				layout.width = display.getWidth();

				ImageView imageView = new ImageView(mViewPager.getContext());
				imageView.setScaleType(ImageView.ScaleType.FIT_START);
				imageView.setLayoutParams(layout);
				imageView.setImageDrawable(Drawable.createFromPath(path));
				imageView.setTag(i);
				mViewPager.setCompensateImageView(i, imageView);
				mViewPager.addPage(imageView);
			}
		}
		return 3;
	}

	/**
	 * Draw the saved views to the display.
	 * 
	 * @param collection
	 * @param position
	 * @return
	 */
	@Override
	public Object instantiateItem(View collection, int position)
	{
		if (mViewPager.getViewCount() == 0) return null;
		((ViewPager) collection).addView(mViewPager.getViewAt(position));
		return mViewPager.getViewAt(position);
	}

	/**
	 * Remove the view from display.
	 * 
	 * @param collection
	 * @param position
	 * @param view
	 */
	@Override
	public void destroyItem(View collection, int position, Object view)
	{
		((ViewPager) collection).removeView((FrameLayout) view);
	}

	/**
	 * Check if the view equals the one in saved views.
	 * 
	 * @param view
	 * @param object
	 * @return
	 */
	@Override
	public boolean isViewFromObject(View view, Object object)
	{
		return view == ((FrameLayout) object);
	}
}
