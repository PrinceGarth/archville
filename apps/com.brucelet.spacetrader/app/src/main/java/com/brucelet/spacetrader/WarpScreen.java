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

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.brucelet.spacetrader.enumtypes.ScreenType;

public class WarpScreen extends BaseScreen {

	public static WarpScreen newInstance() {
		return new WarpScreen();
	}

	public WarpScreen() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.screen_warp, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		View view = getView();
		WarpView warpview = (WarpView) view.findViewById(R.id.screen_warp_warpview);
		warpview.mFragment = this;
		
	}
	
	public WarpView getWarpView() {
		return (WarpView) getView().findViewById(R.id.screen_warp_warpview);
	}
	
	@Override
	public void onRefreshScreen() {
		getWarpView().invalidate();
	}
	
	public static class WarpView extends View {

		private WarpScreen mFragment;
        private GestureDetector mGestureDetector;

		public WarpView(Context context, AttributeSet attrs) {
			super(context, attrs);
			mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
				@Override
				public boolean onDown(MotionEvent e) {
					return mFragment.getGameState().warpFormHandleEvent(e.getX(), e.getY());
				}
			});
		}
		
		
		@Override
		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		    int size = MeasureSpec.getSize(widthMeasureSpec);
			setMeasuredDimension(size, size);
		}
		
		@Override
		public void onDraw(Canvas canvas) {
			mFragment.getGameState().drawShortRange(canvas);
		}

		@Override
		public boolean onTouchEvent(@NonNull MotionEvent event) {
			return mGestureDetector.onTouchEvent(event) | super.onTouchEvent(event);
		}
	}

	@Override
	public void onSingleClick(View v) {
		// WarpScreen has no clickable views; all activity is handled by onTouchEvent()
	}
	
	@Override
	public int getHelpTextResId() {
		return R.string.help_warp;
	}

	@Override
	public ScreenType getType() {
		return ScreenType.WARP;
	}

	public static ScreenType.Creator<WarpScreen> CREATOR = new ScreenType.Creator<WarpScreen>() {

		@Override
		public WarpScreen newInstance() {
			return WarpScreen.newInstance();
		}
	};
}
