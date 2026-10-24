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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.brucelet.spacetrader.enumtypes.ScreenType;

public class TitleScreen extends BaseScreen {

	public static TitleScreen newInstance() {
		return new TitleScreen();
	}

	public TitleScreen() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.screen_title, container, false);
		view.findViewById(R.id.screen_title_image).setOnClickListener(this);
		return view;
	}

	@Override
	public void onSingleClick(View v) {
		BaseDialog dialog = NewGameDialog.newInstance();
		getGameManager().showDialogFragment(dialog);
	}
	
	@Override
	public void onRefreshScreen() {
//		((ImageView) getGameManager().findScreenById(R.id.screen_endofgame).getView().findViewById(R.id.screen_endofgame_image)).setImageDrawable(null);
		((ImageView)getView().findViewById(R.id.screen_title_image)).setImageResource(R.drawable.title);
	}
	
	@Override
	public int getHelpTextResId() {
		return R.string.help_howtoplay;			// NB this is the help text used in original, but seems a little out-of-place.
	}

	@Override
	public ScreenType getType() {
		return ScreenType.TITLE;
	}

	public static ScreenType.Creator<TitleScreen> CREATOR = new ScreenType.Creator<TitleScreen>() {

		@Override
		public TitleScreen newInstance() {
			return TitleScreen.newInstance();
		}
	};

}
