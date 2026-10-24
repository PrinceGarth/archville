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

public class BuyEqScreen extends BaseScreen {
	public static final Map<Purchasable, Integer> BUTTON_IDS; 
	static {
		Map<Purchasable, Integer> ids = new HashMap<>();
		ids.put(Weapon.PULSE,            R.id.screen_buyeq_pulse_button);
		ids.put(Weapon.BEAM,             R.id.screen_buyeq_beam_button);
		ids.put(Weapon.MILITARY,         R.id.screen_buyeq_military_button);
		ids.put(Shield.ENERGY,           R.id.screen_buyeq_energy_button);
		ids.put(Shield.REFLECTIVE,       R.id.screen_buyeq_reflective_button);
		ids.put(Gadget.EXTRABAYS,        R.id.screen_buyeq_extrabays_button);
		ids.put(Gadget.AUTOREPAIRSYSTEM, R.id.screen_buyeq_autorepair_button);
		ids.put(Gadget.NAVIGATINGSYSTEM, R.id.screen_buyeq_navigating_button);
		ids.put(Gadget.TARGETINGSYSTEM,  R.id.screen_buyeq_targeting_button);
		ids.put(Gadget.CLOAKINGDEVICE,   R.id.screen_buyeq_cloaking_button);
		BUTTON_IDS = Collections.unmodifiableMap(ids);
	}
	public static final Map<Purchasable, Integer> PRICE_IDS; 
	static {
		Map<Purchasable, Integer> ids = new HashMap<>();
		ids.put(Weapon.PULSE,            R.id.screen_buyeq_pulse_price);
		ids.put(Weapon.BEAM,             R.id.screen_buyeq_beam_price);
		ids.put(Weapon.MILITARY,         R.id.screen_buyeq_military_price);
		ids.put(Shield.ENERGY,           R.id.screen_buyeq_energy_price);
		ids.put(Shield.REFLECTIVE,       R.id.screen_buyeq_reflective_price);
		ids.put(Gadget.EXTRABAYS,        R.id.screen_buyeq_extrabays_price);
		ids.put(Gadget.AUTOREPAIRSYSTEM, R.id.screen_buyeq_autorepair_price);
		ids.put(Gadget.NAVIGATINGSYSTEM, R.id.screen_buyeq_navigating_price);
		ids.put(Gadget.TARGETINGSYSTEM,  R.id.screen_buyeq_targeting_price);
		ids.put(Gadget.CLOAKINGDEVICE,   R.id.screen_buyeq_cloaking_price);
		PRICE_IDS = Collections.unmodifiableMap(ids);
	}

	public static BuyEqScreen newInstance() {
		return new BuyEqScreen();
	}

	public BuyEqScreen() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.screen_buyeq, container, false);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		View view = getView();
		for (int id : BUTTON_IDS.values()) {
			view.findViewById(id).setOnClickListener(this);
		}
	}

	@Override
	public void onRefreshScreen() {
		getGameState().drawBuyEquipmentForm();
	}


	@Override
	public void onSingleClick(View v) {
		getGameState().buyEquipmentFormHandleEvent(v.getId());
	}
	
	@Override
	public int getHelpTextResId() {
		return R.string.help_buyequipment;
	}

	@Override
	public ScreenType getType() {
		return ScreenType.BUYEQ;
	}

	public static ScreenType.Creator<BuyEqScreen> CREATOR = new ScreenType.Creator<BuyEqScreen>() {

		@Override
		public BuyEqScreen newInstance() {
			return BuyEqScreen.newInstance();
		}
	};

}
