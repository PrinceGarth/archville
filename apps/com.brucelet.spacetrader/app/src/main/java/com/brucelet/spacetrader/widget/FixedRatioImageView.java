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
import android.util.AttributeSet;
import android.widget.ImageView;

// Used by Title and Endgame screens to fit to screen width and crop the bottom of images by spilling the ImageView past the screen bottom.
public class FixedRatioImageView extends ImageView {

	public FixedRatioImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		if (getDrawable() == null) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			return;
		}
		int intWidth = getDrawable().getIntrinsicWidth();
		int intHeight = getDrawable().getIntrinsicHeight();
		if (intWidth <= 0 || intHeight <= 0) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			return;
		}
		double hwratio = 1.0 * intHeight / intWidth;

		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = (int) (width * hwratio + 0.5);
		setMeasuredDimension(width, height);

	}

}
