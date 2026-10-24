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
 * Use the {@link InfoScreen#newInstance} factory method
 * to create an instance of this fragment.
 * 
 */
public class InfoScreen extends BaseScreen {
	public static InfoScreen newInstance() {
		InfoScreen fragment = new InfoScreen();
		return fragment;
	}

	public InfoScreen() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.screen_info, container, false);

		view.findViewById(R.id.screen_info_news).setOnClickListener(this);
		view.findViewById(R.id.screen_info_merc).setOnClickListener(this);
		view.findViewById(R.id.screen_info_special).setOnClickListener(this);
		
		return view;
	}
	
	@Override
	public void onSingleClick(View v) {
		getGameState().systemInformationFormHandleEvent(v.getId());

	}

	@Override
	public void onRefreshScreen() {
		getGameState().drawSystemInformationForm();
		
	}
	
	@Override
	public int getHelpTextResId() {
		return R.string.help_systeminformation;
	}

	@Override
	public ScreenType getType() {
		return ScreenType.INFO;
	}

	public static ScreenType.Creator<InfoScreen> CREATOR = new ScreenType.Creator<InfoScreen>() {

		@Override
		public InfoScreen newInstance() {
			return InfoScreen.newInstance();
		}
	};


}
