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

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class StatusScreen extends BaseScreen{

	public static final int[] BUTTON_IDS = {
		R.id.screen_status_cheat,
		R.id.screen_status_quests_button,
		R.id.screen_status_ship_button,
		R.id.screen_status_cargo_button,
	};
	
	public static StatusScreen newInstance() {
		return new StatusScreen();
	}

	public StatusScreen() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.screen_status, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		View view = getView();
		for (int id : BUTTON_IDS) {
			view.findViewById(id).setOnClickListener(this);
		}
	}

	@Override
	public void onSingleClick(View v) {
		getGameState().commanderStatusFormHandleEvent(v.getId());
	}

	@Override
	public void onRefreshScreen() {
		getGameState().showCommanderStatus();
		
	}
	
	@Override
	public int getHelpTextResId() {
		return R.string.help_commanderstatus;
	}

	@Override
	public ScreenType getType() {
		return ScreenType.STATUS;
	}

	public static ScreenType.Creator<StatusScreen> CREATOR = new ScreenType.Creator<StatusScreen>() {

		@Override
		public StatusScreen newInstance() {
			return StatusScreen.newInstance();
		}
	};

}
