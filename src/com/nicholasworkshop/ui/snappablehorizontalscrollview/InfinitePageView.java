package com.nicholasworkshop.ui.snappablehorizontalscrollview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class InfinitePageView extends ViewPager {

	private LinkedList<FrameLayout> mViews = new LinkedList<FrameLayout>();

	public InfinitePageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setAdapter(mPageViewAdapter);
		this.setCurrentItem(1, false);
		this.setOnPageChangeListener(mOnPageChangeListener);
	}

	public void addPage(View view) {
		FrameLayout frameLayout = new FrameLayout(this.getContext());
		frameLayout.addView(view);
		mViews.add(frameLayout);
	}

	private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {

		private int mDirection;

		private final int LEFT = 0;
		private final int RIGHT = 2;

		@Override public void onPageSelected(int position) {
			mDirection = position;
		}

		@Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

		@Override public void onPageScrollStateChanged(int state) {

			if (state == ViewPager.SCROLL_STATE_IDLE) {
				do {
					switch (mDirection) {
					case LEFT:
						View lastView = mViews.getLast().getChildAt(0);
						mViews.getLast().removeAllViews();
						for (int i = mViews.size() - 1; i > 0; i--) {
							View view = mViews.get(i - 1).getChildAt(0);
							mViews.get(i - 1).removeAllViews();
							mViews.get(i).addView(view);
						}
						mViews.getFirst().addView(lastView);
						break;
					case RIGHT:
						View firstView = mViews.getFirst().getChildAt(0);
						mViews.getFirst().removeAllViews();
						for (int i = 0; i < mViews.size() - 1; i++) {
							View view = mViews.get(i + 1).getChildAt(0);
							mViews.get(i + 1).removeAllViews();
							mViews.get(i).addView(view);
						}
						mViews.getLast().addView(firstView);
					}
				} while (mViews.get(1).getChildAt(0).getTag() != null);
				InfinitePageView.this.setCurrentItem(1, false);
			}
		}
	};

	private PagerAdapter mPageViewAdapter = new PagerAdapter() {

		@Override public int getCount() {
			if (mViews.size() == 1 || mViews.size() == 2) {
				for (int i = 0; i < 2; i++) {
					Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
					mViews.get(i).getChildAt(0).layout(0, 0, display.getWidth(), display.getHeight());
					mViews.get(i).getChildAt(0).setDrawingCacheEnabled(true);
					mViews.get(i).getChildAt(0).buildDrawingCache();
					Bitmap b = mViews.get(i).getChildAt(0).getDrawingCache();
					String path = Environment.getExternalStorageDirectory() + "/.nicholasworkshop/cache/" + b.hashCode() + ".jpg";
					new File(Environment.getExternalStorageDirectory() + "/.nicholasworkshop/cache/").mkdirs();
					try {
						b.compress(CompressFormat.JPEG, 95, new FileOutputStream(path));
					}
					catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					ImageView imageView = new ImageView(getContext());
					imageView.setScaleType(ImageView.ScaleType.FIT_START);
					imageView.setLayoutParams(new LayoutParams(display.getWidth(), display.getHeight()));
					imageView.setImageDrawable(Drawable.createFromPath(path));
					imageView.setTag(i);
					addPage(imageView);
				}
			}
			return 3;
		}

		@Override public Object instantiateItem(View collection, int position) {
			Log.d("Nicholas", "Error" + position);
			if (mViews.size() == 0) return null;
			((ViewPager) collection).addView(mViews.get(position));
			return mViews.get(position);
		}

		@Override public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView((FrameLayout) view);
		}

		@Override public boolean isViewFromObject(View view, Object object) {
			return view == ((FrameLayout) object);
		}

		@Override public void finishUpdate(View arg0) {}

		@Override public void restoreState(Parcelable arg0, ClassLoader arg1) {}

		@Override public Parcelable saveState() {
			return null;
		}

		@Override public void startUpdate(View arg0) {}

	};

}
