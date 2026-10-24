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

public class YardScreen extends BaseScreen {
	private static final int[] BUTTON_IDS = {
		R.id.screen_yard_shipsbutton,
		R.id.screen_yard_podbutton,
		R.id.screen_yard_repairbutton,
		R.id.screen_yard_fullrepairbutton,
		R.id.screen_yard_fuelbutton,
		R.id.screen_yard_fullfuelbutton,
	};

	public static YardScreen newInstance() {
		return new YardScreen();
	}
	
	public YardScreen() {}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.screen_yard, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		for (int id : BUTTON_IDS) {
			getView().findViewById(id).setOnClickListener(this);
		}
	}
	
	@Override
	public void onRefreshScreen() {
		getGameState().showShipYard();
	}

	@Override
	public void onSingleClick(View v) {
		getGameState().shipYardFormHandleEvent(v.getId());
	}
	
	@Override
	public int getHelpTextResId() {
		return R.string.help_shipyard;
	}

	@Override
	public ScreenType getType() {
		return ScreenType.YARD;
	}

	public static ScreenType.Creator<YardScreen> CREATOR = new ScreenType.Creator<YardScreen>() {

		@Override
		public YardScreen newInstance() {
			return YardScreen.newInstance();
		}
	};

}
