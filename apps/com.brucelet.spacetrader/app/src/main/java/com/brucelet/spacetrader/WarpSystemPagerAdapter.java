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



import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brucelet.spacetrader.datatypes.SolarSystem;

public abstract class WarpSystemPagerAdapter extends PagerAdapter {
	
	private View mCurrentView;
	private View mPrevView;
	private View mNextView;
	
	public WarpSystemPagerAdapter() {}

	public static final int POSITION_CURRENT = 1;
	public static final int POSITION_PREV = 0;
	public static final int POSITION_NEXT = 2;
	
	
	protected abstract void setNewSystem(SolarSystem system, View page);
	protected abstract int layoutResource();
	
	private void setSystem(SolarSystem system, int position) {
		View view;
		if (position == POSITION_CURRENT) {
			view = mCurrentView;
		} else if (position == POSITION_PREV) {
			view = mPrevView;
		} else if (position == POSITION_NEXT) {
			view = mNextView;
		} else {
			throw new IllegalArgumentException("Invalid position "+position);
		}

		Log.d("WarpSystemPagerAdapter.setSystem()","position="+position);
		setNewSystem(system, view);
	}
	
	public void setSystems(SolarSystem current, SolarSystem prev, SolarSystem next) {
		setSystem(current, POSITION_CURRENT);
		setSystem(prev, POSITION_PREV);
		setSystem(next, POSITION_NEXT);
		notifyDataSetChanged();
	}
		
	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		LayoutInflater.from(container.getContext()).inflate(layoutResource(), container);
		View view = container.getChildAt(position);
		
//		if (view == null) throw new NullPointerException();
		
		if (position == POSITION_CURRENT) {
			mCurrentView = view;
		} else if (position == POSITION_PREV) {
			mPrevView = view;
		} else if (position == POSITION_NEXT) {
			mNextView = view;
		} else {
			throw new IllegalArgumentException("Invalid position "+position);
		}
		return view;
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View)object);
	}

}
