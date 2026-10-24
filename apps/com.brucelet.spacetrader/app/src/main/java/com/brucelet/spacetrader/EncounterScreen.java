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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.brucelet.spacetrader.enumtypes.ScreenType;

public class EncounterScreen extends BaseScreen {
	
	public static final List<Integer> BUTTONS;
	static {
		List<Integer> ids = new ArrayList<>();
		ids.add(R.id.screen_encounter_button1);
		ids.add(R.id.screen_encounter_button2);
		ids.add(R.id.screen_encounter_button3);
		ids.add(R.id.screen_encounter_button4);
		BUTTONS = Collections.unmodifiableList(ids);
	}
	
	public static final List<Integer> TRIBBLES;
	static {
		List<Integer> ids = new ArrayList<>();
		ids.add(R.id.screen_encounter_tribble0);
		ids.add(R.id.screen_encounter_tribble1);
		ids.add(R.id.screen_encounter_tribble2);
		ids.add(R.id.screen_encounter_tribble3);
		ids.add(R.id.screen_encounter_tribble4);
		ids.add(R.id.screen_encounter_tribble5);
		ids.add(R.id.screen_encounter_tribble6);
		ids.add(R.id.screen_encounter_tribble7);
		ids.add(R.id.screen_encounter_tribble8);
		ids.add(R.id.screen_encounter_tribble9);
		ids.add(R.id.screen_encounter_tribble10);
		ids.add(R.id.screen_encounter_tribble11);
		ids.add(R.id.screen_encounter_tribble12);
		ids.add(R.id.screen_encounter_tribble13);
		ids.add(R.id.screen_encounter_tribble14);
		ids.add(R.id.screen_encounter_tribble15);
		ids.add(R.id.screen_encounter_tribble16);
		ids.add(R.id.screen_encounter_tribble17);
		ids.add(R.id.screen_encounter_tribble18);
		ids.add(R.id.screen_encounter_tribble19);
		ids.add(R.id.screen_encounter_tribble20);
		ids.add(R.id.screen_encounter_tribble21);
		ids.add(R.id.screen_encounter_tribble22);
		ids.add(R.id.screen_encounter_tribble23);
		ids.add(R.id.screen_encounter_tribble24);
		ids.add(R.id.screen_encounter_tribble25);
		ids.add(R.id.screen_encounter_tribble26);
		ids.add(R.id.screen_encounter_tribble27);
		ids.add(R.id.screen_encounter_tribble28);
		ids.add(R.id.screen_encounter_tribble29);
		TRIBBLES = Collections.unmodifiableList(ids);
	}

	public static EncounterScreen newInstance() {
		return new EncounterScreen();
	}

	public EncounterScreen() {}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);		
//		if (getGameManager().getCurrentScreenId() == R.id.screen_encounter) inflater.inflate(R.menu.encounter, menu);
		inflater.inflate(R.menu.encounter, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		if (item.getItemId() == R.id.encounter_help) {
//			getGameManager().showDialogFragment(HelpDialog.creator(getHelpTextResId()));
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.screen_encounter, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		View view = getView();
		for (int id : BUTTONS) {
			view.findViewById(id).setOnClickListener(this);
		}
		for (int id : TRIBBLES) {
			view.findViewById(id).setOnClickListener(this);
		}
		view.findViewById(R.id.screen_encounter_surrender).setOnClickListener(this);
		view.findViewById(R.id.screen_encounter_continuous_interrupt).setOnClickListener(this);
	}
	
	@Override
	public void onRefreshScreen() {
		getGameState().drawEncounterForm();
		
	}

	@Override
	public void onSingleClick(View v) {
		getGameState().encounterFormHandleEvent(v.getId());
		
	}
	
	@Override
	public int getHelpTextResId() {
		return R.string.help_encounter;
	}

	@Override
	public ScreenType getType() {
		return ScreenType.ENCOUNTER;
	}

	public static ScreenType.Creator<EncounterScreen> CREATOR = new ScreenType.Creator<EncounterScreen>() {

		@Override
		public EncounterScreen newInstance() {
			return EncounterScreen.newInstance();
		}
	};


}
