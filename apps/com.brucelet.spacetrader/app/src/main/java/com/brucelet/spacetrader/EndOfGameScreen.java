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

import com.brucelet.spacetrader.enumtypes.ScreenType;

public class EndOfGameScreen extends BaseScreen {

	public static EndOfGameScreen newInstance() {
		return new EndOfGameScreen();
	}

	public EndOfGameScreen() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.screen_endofgame, container, false);
		view.findViewById(R.id.screen_endofgame_image).setOnClickListener(this);
		return view;
	}
	
	@Override
	public void onRefreshScreen() {
		getGameState().drawEndGameScreen();
	}

	@Override
	public void onSingleClick(View v) {
		getGameState().endOfGame();
	}
	
	@Override
	public int getHelpTextResId() {
		return R.string.help_retiredestroyedutopia;
	}

	@Override
	public ScreenType getType() {
		return ScreenType.ENDGAME;
	}

	public static ScreenType.Creator<EndOfGameScreen> CREATOR = new ScreenType.Creator<EndOfGameScreen>() {

		@Override
		public EndOfGameScreen newInstance() {
			return EndOfGameScreen.newInstance();
		}
	};

}
