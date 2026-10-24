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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brucelet.spacetrader.enumtypes.Gadget;
import com.brucelet.spacetrader.enumtypes.Purchasable;
import com.brucelet.spacetrader.enumtypes.ScreenType;
import com.brucelet.spacetrader.enumtypes.Shield;
import com.brucelet.spacetrader.enumtypes.Weapon;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class StatusShipScreen extends BaseScreen {
	public static final Map<Purchasable, Integer> EQUIPMENT_IDS; 
	static {
		Map<Purchasable, Integer> ids = new HashMap<>();
		ids.put(Weapon.PULSE,            R.id.screen_status_ship_equip_pulse);
		ids.put(Weapon.BEAM,             R.id.screen_status_ship_equip_beam);
		ids.put(Weapon.MILITARY,         R.id.screen_status_ship_equip_military);
		ids.put(Weapon.MORGAN,           R.id.screen_status_ship_equip_morgan);

		ids.put(Shield.ENERGY,           R.id.screen_status_ship_equip_energy);
		ids.put(Shield.REFLECTIVE,       R.id.screen_status_ship_equip_reflective);
		ids.put(Shield.LIGHTNING,        R.id.screen_status_ship_equip_lightning);
		
		ids.put(Gadget.EXTRABAYS,        R.id.screen_status_ship_equip_extrabays);
		ids.put(Gadget.AUTOREPAIRSYSTEM, R.id.screen_status_ship_equip_autorepair);
		ids.put(Gadget.NAVIGATINGSYSTEM, R.id.screen_status_ship_equip_navigating);
		ids.put(Gadget.TARGETINGSYSTEM,  R.id.screen_status_ship_equip_targeting);
		ids.put(Gadget.CLOAKINGDEVICE,   R.id.screen_status_ship_equip_cloaking);
		ids.put(Gadget.FUELCOMPACTOR,    R.id.screen_status_ship_equip_fuelcompactor);
		EQUIPMENT_IDS = Collections.unmodifiableMap(ids);
	}
	
	public static StatusShipScreen newInstance() {
		StatusShipScreen fragment = new StatusShipScreen();
		return fragment;
	}

	public StatusShipScreen() {
		// Required empty public constructor
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getView().findViewById(R.id.screen_status_back_button).setOnClickListener(this);
		getView().findViewById(R.id.screen_status_quests_button).setOnClickListener(this);
		getView().findViewById(R.id.screen_status_cargo_button).setOnClickListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.screen_status_ship, container, false);
		return view;
	}
	
	@Override
	public void onRefreshScreen() {
		getGameState().drawCurrentShipForm();
	}
	
	@Override
	public void onSingleClick(View v) {
		getGameState().currentShipFormHandleEvent(v.getId());
	}
	
	@Override
	public int getHelpTextResId() {
		return R.string.help_currentship;
	}

	@Override
	public ScreenType getType() {
		return ScreenType.SHIP;
	}

	public static ScreenType.Creator<StatusShipScreen> CREATOR = new ScreenType.Creator<StatusShipScreen>() {

		@Override
		public StatusShipScreen newInstance() {
			return StatusShipScreen.newInstance();
		}
	};

}
