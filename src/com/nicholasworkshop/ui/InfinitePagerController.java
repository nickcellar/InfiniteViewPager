package com.nicholasworkshop.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class InfinitePagerController extends PagerAdapter implements OnPageChangeListener
{
	private int mCompensateModeCount = 0;

	private int mDirection;
	private int mCurrent = 1;

	private final int LEFT = 0;
	private final int RIGHT = 2;
	/**
	 * Instance of the view pager.
	 */
	private InfiniteViewPager mViewPager;

	/**
	 * Constructor of a pager adapter.
	 * 
	 * @param pager
	 *        The instance of parent InfiniteViewPager.
	 */
	public InfinitePagerController(InfiniteViewPager pager)
	{
		mViewPager = pager;
	}

	/**
	 * Always return 3. Shift the view
	 * 
	 * @return Number of view saved.
	 */
	@Override
	public int getCount()
	{
		if (mViewPager.getViewCount() == 1 || mViewPager.getViewCount() == 2) {
			mCompensateModeCount = mViewPager.getViewCount();
			for (int i = 0; i < 2; i++) {

				Display display = ((Activity) mViewPager.getContext()).getWindowManager().getDefaultDisplay();
				mViewPager.getViewAt(i).getChildAt(0).layout(0, 0, display.getWidth(), display.getHeight());
				mViewPager.getViewAt(i).getChildAt(0).setDrawingCacheEnabled(true);
				mViewPager.getViewAt(i).getChildAt(0).buildDrawingCache();
				Bitmap b = mViewPager.getViewAt(i).getChildAt(0).getDrawingCache();

				File file = new File(mViewPager.getContext().getCacheDir(), i + "jpg");
				try {
					b.compress(CompressFormat.JPEG, 95, new FileOutputStream(file));
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
				imageView.setImageDrawable(Drawable.createFromPath(file.getAbsolutePath()));
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