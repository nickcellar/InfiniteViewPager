package com.nicholasworkshop.test;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.nicholasworkshop.ui.infinitepageview.InfinitePageView;

public class MainActivity extends Activity
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		InfinitePageView mInfinitePageView = (InfinitePageView) findViewById(R.id.InfinitePageView);

		for (int i = 0; i < 3; i++) {
			TextView textView = new TextView(this);
			textView.setText("Bonjour PAUG " + i);
			textView.setBackgroundColor(Color.BLUE);
			textView.setTextColor(Color.WHITE);
			textView.setTextSize(30);
			mInfinitePageView.addPage(textView);
		}
	}
}