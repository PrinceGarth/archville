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
package com.brucelet.spacetrader.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.brucelet.spacetrader.R;

// NB This class exists so that Lollipop will correctly set monospace typeface for action buttons
// It also allows us to display titles on longpress since default actionbuttons disable this when displaying text instead of icons
public class ShortcutButton extends Button implements View.OnLongClickListener {

	private CharSequence mTitle;

	public ShortcutButton(Context context) {
		this(context, null);
	}

	public ShortcutButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ShortcutButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		TypedValue tv = new TypedValue();
		context.getTheme().resolveAttribute(R.attr.actionButtonStyle, tv, true);
		int actionButtonStyle = tv.resourceId;
		TypedArray ta = context.getTheme().obtainStyledAttributes(actionButtonStyle,
				new int[] {R.attr.actionMenuTextAppearance, R.attr.actionButtonMarginTop, R.attr.actionButtonMarginBottom});
		setTextAppearance(context, ta.getResourceId(0, 0));
		int marginTop = ta.getDimensionPixelOffset(1, 0);
		int marginBottom = ta.getDimensionPixelOffset(2, 0);
		ta.recycle();
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, marginTop, 0, marginBottom);
		setLayoutParams(params);
		setTypeface(Typeface.MONOSPACE);
		setOnLongClickListener(this);
	}
	
	public void setMenuItem(MenuItem item) {
		mTitle = item.getTitle();
		setText(item.getTitleCondensed());
	}

	public void setTitles(CharSequence title, CharSequence titleCondensed) {
		mTitle = title;
		setText(titleCondensed);
	}

	@Override
	// Based on android.support.v7.internal.view.menu.ActionMenuItemView.onLongClick()
	public boolean onLongClick(View v) {
        final int[] screenPos = new int[2];
        final Rect displayFrame = new Rect();
        getLocationOnScreen(screenPos);
        getWindowVisibleDisplayFrame(displayFrame);

		// This is an extra customization so that behavior is consistent for material themes,
		// which have a translucent status bar and therefore a larger display frame.
		TypedValue tv = new TypedValue();
		getContext().getTheme().resolveAttribute(R.attr.statusBarPadding, tv, true);
		float statusBarPadding = tv.getDimension(getResources().getDisplayMetrics());

        final Context context = getContext();
        final int width = getWidth();
        final int height = getHeight();
        final int midy = screenPos[1] + height / 2;
        int referenceX = screenPos[0] + width / 2;
        if (ViewCompat.getLayoutDirection(v) == ViewCompat.LAYOUT_DIRECTION_LTR) {
            final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            referenceX = screenWidth - referenceX; // mirror
        }
        Toast cheatSheet = Toast.makeText(context, mTitle, Toast.LENGTH_SHORT);
        if (midy < displayFrame.height() - statusBarPadding) {
            // Show along the top; follow action buttons
            cheatSheet.setGravity(Gravity.TOP | GravityCompat.END, referenceX,
					screenPos[1] + height - displayFrame.top);
        } else {
            // Show along the bottom center
            cheatSheet.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, height);
        }
        cheatSheet.show();
        return true;
	}
}
