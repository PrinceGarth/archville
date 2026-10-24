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
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.brucelet.spacetrader.datatypes.GameState;
import com.brucelet.spacetrader.enumtypes.ScreenType;

public class ChartScreen extends BaseScreen {

	public static final int DOWN = 0;
	public static final int UP = 1;
	public static final int MOVE = 2;
	public static final int LONG = 3;

	public static ChartScreen newInstance() {
		return new ChartScreen();
	}

	public ChartScreen() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.screen_chart, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		View view = getView();
		view.findViewById(R.id.screen_chart_find).setOnClickListener(this);
		view.findViewById(R.id.screen_chart_jump).setOnClickListener(this);
		ChartView chartView = (ChartView) view.findViewById(R.id.screen_chart_chartview);
		chartView.mFragment = this;
	}
	
	public ChartView getChartView() {
		return (ChartView) getView().findViewById(R.id.screen_chart_chartview);
	}
	
	@Override
	public void onShowScreen() {
		getGameState().showGalaxy();
	}
	
	@Override
	public void onRefreshScreen() {
		getChartView().invalidate();
	}
	
	@Override
	public void onSingleClick(View v) {
		getGameState().galacticChartFormHandleEvent(v.getId());
	}
	
	public static class ChartView extends FrameLayout {
		private ChartScreen mFragment;
		private GestureDetector mGestureDetector;
		private ScaleGestureDetector mScaleGestureDetector;

		private float mScale = 1;
		private float mScrollX = 0;
		private float mScrollY = 0;

		private static final float MAX_SCALE = 1.5f;

		public ChartView(Context context, AttributeSet attrs) {
			super(context, attrs);
			if (getForeground() == null) setForeground(ResourcesCompat.getDrawable(getResources(), R.drawable.foreground_shadow_bottom, context.getTheme()));
			getForeground().setAlpha(0);
			mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
				@Override
				public boolean onDown(MotionEvent e) {
					float x = mScale == 1? e.getX() : (e.getX() - getWidth()/2)/mScale + getWidth()/2 + mScrollX;
					float y = mScale == 1? e.getY() : (e.getY() - getHeight()/2)/mScale + getHeight()/2 + mScrollY;
					return mFragment.getGameState().galacticChartFormHandleEvent(x, y, DOWN);
				}

				@Override
				public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
										float distanceY) {
					if (mScale == 1) {
						return mFragment.getGameState().galacticChartFormHandleEvent(e2.getX(), e2.getY(), MOVE);
					} else if (mFragment.getGameState().zoomGalaxy()) {

						mScrollX += distanceX/mScale;
						mScrollY += distanceY/mScale;

						checkScrollBounds();

						invalidate();
						return true;
					} else {
						return false;
					}
				}

				@Override
				public boolean onSingleTapUp(MotionEvent e) {
					float x = mScale == 1? e.getX() : (e.getX() - getWidth()/2)/mScale + getWidth()/2 + mScrollX;
					float y = mScale == 1? e.getY() : (e.getY() - getHeight()/2)/mScale + getHeight()/2 + mScrollY;
					return mFragment.getGameState().galacticChartFormHandleEvent(x, y, UP);
				}

				public void onLongPress(MotionEvent e) {
					if (mFragment.getGameState().trackLongPress()) {
						float x = mScale == 1 ? e.getX() : (e.getX() - getWidth() / 2) / mScale + getWidth() / 2 + mScrollX;
						float y = mScale == 1 ? e.getY() : (e.getY() - getHeight() / 2) / mScale + getHeight() / 2 + mScrollY;
						mFragment.getGameState().galacticChartFormHandleEvent(x, y, LONG);
						performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
					}
					else if (mFragment.getGameState().zoomGalaxy()) {
						mScale = mScale == MAX_SCALE ? 1 : MAX_SCALE;
						if (mScale > 1) {
							mScale = MAX_SCALE;
							mScrollX += (e.getX() - getWidth()/2)/mScale;
							mScrollY += (e.getY() - getHeight()/2)/mScale;
						}

						checkScrollBounds();
						getForeground().setAlpha(mScale == 1? 0x00 : 0xff);
						invalidate();
						performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
					}

				}

			});
			mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
				@Override
				public boolean onScale(ScaleGestureDetector detector) {
					if (mFragment.getGameState().zoomGalaxy()) {
						float scaleFactor = detector.getScaleFactor();
						mScale *= scaleFactor;
						checkScrollBounds();

						getForeground().setAlpha(mScale == 1? 0 : 0xff);
						invalidate();

						return true;
					} else {
						if (mScale != 1) {
							mScale = 1;
							checkScrollBounds();
							getForeground().setAlpha(0);
							invalidate();
							return true;
						}
						return false;
					}
				}
			});
		}

		private void checkScrollBounds() {
			mScale = (mScale > MAX_SCALE ? MAX_SCALE : mScale < 1.01 ? 1 : mScale);

			float left = (getLeft() - getWidth()/2)/mScale + getWidth()/2 + mScrollX;
			float right = (getRight() - getWidth()/2)/mScale + getWidth()/2 + mScrollX;
			float top = (getTop() - getHeight()/2)/mScale + getHeight()/2 + mScrollY;
			float bottom = (getBottom() - getHeight()/2)/mScale + getHeight()/2 + mScrollY;

			if (left < getLeft()) mScrollX += getLeft() - left;
			if (right > getRight()) mScrollX -= right - getRight();
			if (top < getTop()) mScrollY += getTop() - top;
			if (bottom > getBottom()) mScrollY -= bottom - getBottom();

		}
		
		@Override
		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			int size = MeasureSpec.getSize(widthMeasureSpec);
			setMeasuredDimension(size, (int) (size * GameState.GALAXYHEIGHT * 1f / GameState.GALAXYWIDTH) );
		}
		
		@Override
		public void onDraw(Canvas canvas) {
			int saveCount = canvas.save();
			canvas.translate(getWidth()/2, getHeight()/2);
			canvas.scale(mScale, mScale);
			canvas.translate(-getWidth() / 2, -getHeight() / 2);
			canvas.translate(-mScrollX, -mScrollY);
			mFragment.getGameState().drawGalaxy(canvas);
			canvas.restoreToCount(saveCount);
		}

		@Override
		public boolean onTouchEvent(@NonNull MotionEvent event) {
			return mScaleGestureDetector.onTouchEvent(event) | mGestureDetector.onTouchEvent(event) | super.onTouchEvent(event);
		}
		
	}
	
	@Override
	public int getHelpTextResId() {
		return R.string.help_galacticchart;
	}

	@Override
	public ScreenType getType() {
		return ScreenType.CHART;
	}

	public static ScreenType.Creator<ChartScreen> CREATOR = new ScreenType.Creator<ChartScreen>() {

		@Override
		public ChartScreen newInstance() {
			return ChartScreen.newInstance();
		}
	};
	
}
