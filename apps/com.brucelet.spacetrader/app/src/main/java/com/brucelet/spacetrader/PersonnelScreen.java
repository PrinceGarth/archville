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

public class PersonnelScreen extends BaseScreen {
	private static final int[] BUTTON_IDS = {
		R.id.screen_personnel_merc1_fire,
		R.id.screen_personnel_merc2_fire,
		R.id.screen_personnel_merc3_hire,
	};

	public static PersonnelScreen newInstance() {
		return new PersonnelScreen();
	}

	public PersonnelScreen() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.screen_personnel, container, false);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		View view = getView();
		for (int id : BUTTON_IDS) {
			view.findViewById(id).setOnClickListener(this);
		}
	}
	
	@Override
	public void onRefreshScreen() {
		getGameState().drawPersonnelRoster();
	}


	@Override
	public void onSingleClick(View v) {
		getGameState().personnelRosterFormHandleEvent(v.getId());
	}
	
	@Override
	public int getHelpTextResId() {
		return R.string.help_personnelroster;
	}

	@Override
	public ScreenType getType() {
		return ScreenType.PERSONNEL;
	}

	public static ScreenType.Creator<PersonnelScreen> CREATOR = new ScreenType.Creator<PersonnelScreen>() {

		@Override
		public PersonnelScreen newInstance() {
			return PersonnelScreen.newInstance();
		}
	};
	
}
