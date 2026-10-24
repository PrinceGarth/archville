/*
 *     Copyright (C) 2014 Russell Wolf, All Rights Reserved
 *     
 *     Based on code by Pieter Spronck
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *     
 *     You can contact the author at spacetrader@brucelet.com
 */
package com.brucelet.spacetrader;

import android.support.v4.view.ViewPager;
import android.util.Log;

import com.brucelet.spacetrader.datatypes.GameState;


public abstract class WarpSubScreen extends BaseScreen implements ViewPager.OnPageChangeListener {
//	protected abstract void setPageListeners(View view);	// These aren't getting called for no reason I can figure out. Currently implemented through xml instead.
	public abstract ViewPager getPager();
	protected abstract WarpSystemPagerAdapter createPagerAdapter();
	protected abstract void onRefreshWarpSubScreen();

//	public static final String KEY_MOTION_EVENT = "event";
//	private MotionEvent mEvent;
//	private View mToggleButton;
//
//	public void onViewCreated(View view, Bundle savedInstanceState) {
//		super.onViewCreated(view, savedInstanceState);
//		mToggleButton = view.findViewById(R.id.screen_warp_toggle);
//		if (savedInstanceState != null && savedInstanceState.containsKey(KEY_MOTION_EVENT)) {
//			mEvent = savedInstanceState.getParcelable(KEY_MOTION_EVENT);
//			mToggleButton.onTouchEvent(mEvent);
//		}
//		mToggleButton.setOnTouchListener(new View.OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				mEvent = event;
//				return false;
//			}
//		});
//
//	}
//
//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//
//		outState.putParcelable(KEY_MOTION_EVENT, mEvent);
//	}
	
	@Override
	public void onPageSelected(int position) {
		// Do nothing
		Log.d("WarpSubScreen","position="+position);
		
	}
	
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		// Do nothing
//		Log.v("WarpSubScreen.onPageScrolled()","position="+position+", "+"positionOffset="+positionOffset+", "+"positionOffsetPixels="+positionOffsetPixels);
	}
	
	@Override
	public void onPageScrollStateChanged(int state) {
		if (state == ViewPager.SCROLL_STATE_IDLE) {
			// Update the adapter so that we can continue scrolling.
			
			Log.d("WarpSubScreen","State idle");

			ViewPager pager = getPager();
			int currentItem = pager.getCurrentItem();
			boolean back;
			if (currentItem == WarpSystemPagerAdapter.POSITION_PREV) {
				back = true;
			} else if (currentItem == WarpSystemPagerAdapter.POSITION_NEXT) {
				back = false;
			} else {
				return;
			}

			GameState gameState = getGameState();
			
			gameState.scrollWarpSystem(back);
			onRefreshScreen();
//			gameState.setAdapterSystems(getPagerAdapter());
		}
	}

	@Override
	public final void onRefreshScreen() {
		onRefreshWarpSubScreen();

		ViewPager pager = getPager();
		WarpSystemPagerAdapter adapter = getPagerAdapter();
		
		if (adapter == null) {
			
			adapter = createPagerAdapter();
			
			pager.addOnPageChangeListener(this);
			pager.setOffscreenPageLimit(2);
			
			adapter.instantiateItem(pager, 0);
			adapter.instantiateItem(pager, 1);
			adapter.instantiateItem(pager, 2);
		}
		pager.setAdapter(adapter); // Logically this doesn't seem necessary on each refresh but it gets rid of an apparent flicker which is otherwise present when the adapter items change.
		pager.setCurrentItem(1, false);
		getGameState().setAdapterSystems(adapter);
//		setPageListeners(pager.getChildAt(0));
//		setPageListeners(pager.getChildAt(1));
//		setPageListeners(pager.getChildAt(2));

		
		Log.d("WarpSubScreen", "current item is "+pager.getCurrentItem());
	}
	
	public WarpSystemPagerAdapter getPagerAdapter() {
		return (WarpSystemPagerAdapter) getPager().getAdapter();
	}

	public void notifyAdapterChanged() {
		getPagerAdapter().notifyDataSetChanged();
	}
}
