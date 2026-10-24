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
public class StatusCargoScreen extends BaseScreen {
	
	public static StatusCargoScreen newInstance() {
		StatusCargoScreen fragment = new StatusCargoScreen();
		return fragment;
	}

	public StatusCargoScreen() {
		// Required empty public constructor
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getView().findViewById(R.id.screen_status_back_button).setOnClickListener(this);
		getView().findViewById(R.id.screen_status_ship_button).setOnClickListener(this);
		getView().findViewById(R.id.screen_status_quests_button).setOnClickListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.screen_status_cargo, container, false);
		return view;
	}
	
	@Override
	public void onRefreshScreen() {
		getGameState().drawSpecialCargoForm();
	}
	
	@Override
	public void onSingleClick(View v) {
		getGameState().specialCargoFormHandleEvent(v.getId());
	}
	
	@Override
	public int getHelpTextResId() {
		return R.string.help_specialcargo; // NB Original displays help_main here which makes no sense. We use a new help text instead.
	}

	@Override
	public ScreenType getType() {
		return ScreenType.CARGO;
	}

	public static ScreenType.Creator<StatusCargoScreen> CREATOR = new ScreenType.Creator<StatusCargoScreen>() {

		@Override
		public StatusCargoScreen newInstance() {
			return StatusCargoScreen.newInstance();
		}
	};
}
