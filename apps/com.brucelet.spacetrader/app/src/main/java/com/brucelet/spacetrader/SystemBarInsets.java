/*
 *     Copyright (C) 2026 Space Trader Archville contributors
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
 */
package com.brucelet.spacetrader;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Android 15+ always draws apps edge-to-edge. Keep the UI clear of the system bars and display
 * cutouts: the status bar inset goes to the status_bar_padding views above the toolbar (so the
 * toolbar colour runs up behind the status bar), and the remaining insets pad the layout root.
 */
final class SystemBarInsets {

	private SystemBarInsets() {}

	static void apply(Activity activity) {
		View root = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
		ViewCompat.setOnApplyWindowInsetsListener(root, new OnApplyWindowInsetsListener() {
			@Override
			public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat windowInsets) {
				Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
				v.setPadding(insets.left, 0, insets.right, insets.bottom);
				setHeight(v.findViewById(R.id.status_bar_padding), insets.top);
				setHeight(v.findViewById(R.id.status_bar_tint), insets.top);
				return WindowInsetsCompat.CONSUMED;
			}
		});
	}

	private static void setHeight(View view, int height) {
		if (view == null) return;
		ViewGroup.LayoutParams lp = view.getLayoutParams();
		lp.height = height;
		view.setLayoutParams(lp);
	}
}
