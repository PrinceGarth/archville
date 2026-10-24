package com.brucelet.spacetrader.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MenuTouchInterceptor extends View {

	private final List<View> mForwardingViews = new ArrayList<>();
	private View mActiveView;

	public MenuTouchInterceptor(Context context) {
		super(context);
	}

	public MenuTouchInterceptor(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MenuTouchInterceptor(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public boolean addForwardingView(View view) {
		return mForwardingViews.add(view);
	}

	public void setActiveView(View view) {
		if (mForwardingViews.contains(view)) {
			mActiveView = view;
			mActiveView.invalidate();
		} else {
			throw new IllegalArgumentException();
		}
	}

	private static boolean isInView(View v, MotionEvent e) {
		float x = e.getX();
		float y = e.getY();
		return x > v.getLeft() && x < v.getRight() && y > v.getTop() && y < v.getBottom();
	}

	public void dispatchTouchToActiveView(MotionEvent event) {
		if (mActiveView == null) return;
		float x = event.getX();
		float y = event.getY();
		mActiveView.dispatchTouchEvent(MotionEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), x - mActiveView.getLeft(), y - mActiveView.getTop(), event.getMetaState()));
	}


	@Override
	public boolean onTouchEvent(@NonNull MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		// If we're inside our own bounds, update active child
		View newActiveView = mActiveView;
		if (isInView(this, event)) {
			newActiveView = null;
			for (View view : mForwardingViews) {
				if (isInView(view, event)) {
					newActiveView = view;
					break;
				}
			}
		}

		// If the active target has changed, send a cancel event to the old one.
		if (mActiveView != null && !mActiveView.equals(newActiveView)) {
			MotionEvent upEvent = MotionEvent.obtain(event.getDownTime(), event.getEventTime(), MotionEvent.ACTION_CANCEL, x - mActiveView.getLeft(), y - mActiveView.getTop(), event.getMetaState());
			mActiveView.dispatchTouchEvent(upEvent);
		}

		// Also send a down event to the new target so it goes into pressed state
		if (newActiveView != null && !newActiveView.equals(mActiveView)) {
			MotionEvent downEvent = MotionEvent.obtain(event.getDownTime(), event.getEventTime(), MotionEvent.ACTION_DOWN, x - newActiveView.getLeft(), y - newActiveView.getTop(), event.getMetaState());
			newActiveView.dispatchTouchEvent(downEvent);
		}

		mActiveView = newActiveView;

		// If we have an active target, pass the touch along
		if (mActiveView != null) {
			MotionEvent forwardedEvent = MotionEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), x - mActiveView.getLeft(), y - mActiveView.getTop(), event.getMetaState());
			mActiveView.dispatchTouchEvent(forwardedEvent);
		}

		return true;
	}


}
