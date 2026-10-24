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
import java.util.EnumMap;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brucelet.spacetrader.enumtypes.ScreenType;
import com.brucelet.spacetrader.enumtypes.ShipType;

public class BuyShipScreen extends BaseScreen {
	public static final Map<ShipType, Integer> BUY_IDS; 
	static {
		Map<ShipType, Integer> ids = new EnumMap<>(ShipType.class);
		ids.put(ShipType.FLEA,        R.id.screen_yard_buyship_flea_buy);
		ids.put(ShipType.GNAT,        R.id.screen_yard_buyship_gnat_buy);
		ids.put(ShipType.FIREFLY,     R.id.screen_yard_buyship_firefly_buy);
		ids.put(ShipType.MOSQUITO,    R.id.screen_yard_buyship_mosquito_buy);
		ids.put(ShipType.BUMBLEBEE,   R.id.screen_yard_buyship_bumblebee_buy);
		ids.put(ShipType.BEETLE,      R.id.screen_yard_buyship_beetle_buy);
		ids.put(ShipType.HORNET,      R.id.screen_yard_buyship_hornet_buy);
		ids.put(ShipType.GRASSHOPPER, R.id.screen_yard_buyship_grasshopper_buy);
		ids.put(ShipType.TERMITE,     R.id.screen_yard_buyship_termite_buy);
		ids.put(ShipType.WASP,        R.id.screen_yard_buyship_wasp_buy);
		BUY_IDS = Collections.unmodifiableMap(ids);
	}
	public static final Map<ShipType, Integer> INFO_IDS; 
	static {
		Map<ShipType, Integer> ids = new EnumMap<>(ShipType.class);
		ids.put(ShipType.FLEA,        R.id.screen_yard_buyship_flea_info);
		ids.put(ShipType.GNAT,        R.id.screen_yard_buyship_gnat_info);
		ids.put(ShipType.FIREFLY,     R.id.screen_yard_buyship_firefly_info);
		ids.put(ShipType.MOSQUITO,    R.id.screen_yard_buyship_mosquito_info);
		ids.put(ShipType.BUMBLEBEE,   R.id.screen_yard_buyship_bumblebee_info);
		ids.put(ShipType.BEETLE,      R.id.screen_yard_buyship_beetle_info);
		ids.put(ShipType.HORNET,      R.id.screen_yard_buyship_hornet_info);
		ids.put(ShipType.GRASSHOPPER, R.id.screen_yard_buyship_grasshopper_info);
		ids.put(ShipType.TERMITE,     R.id.screen_yard_buyship_termite_info);
		ids.put(ShipType.WASP,        R.id.screen_yard_buyship_wasp_info);
		INFO_IDS = Collections.unmodifiableMap(ids);
	}
	public static final Map<ShipType, Integer> PRICE_IDS; 
	static {
		Map<ShipType, Integer> ids = new EnumMap<>(ShipType.class);
		ids.put(ShipType.FLEA,        R.id.screen_yard_buyship_flea_price);
		ids.put(ShipType.GNAT,        R.id.screen_yard_buyship_gnat_price);
		ids.put(ShipType.FIREFLY,     R.id.screen_yard_buyship_firefly_price);
		ids.put(ShipType.MOSQUITO,    R.id.screen_yard_buyship_mosquito_price);
		ids.put(ShipType.BUMBLEBEE,   R.id.screen_yard_buyship_bumblebee_price);
		ids.put(ShipType.BEETLE,      R.id.screen_yard_buyship_beetle_price);
		ids.put(ShipType.HORNET,      R.id.screen_yard_buyship_hornet_price);
		ids.put(ShipType.GRASSHOPPER, R.id.screen_yard_buyship_grasshopper_price);
		ids.put(ShipType.TERMITE,     R.id.screen_yard_buyship_termite_price);
		ids.put(ShipType.WASP,        R.id.screen_yard_buyship_wasp_price);
		PRICE_IDS = Collections.unmodifiableMap(ids);
	}
	

	public static BuyShipScreen newInstance() {
		return new BuyShipScreen();
	}
	
	public BuyShipScreen() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.screen_yard_buyship, container, false);
	}
	
	@Override
	public void onRefreshScreen() {
		getGameState().drawBuyShipForm();
		View view = getView();

		for (ShipType ship : ShipType.buyableValues()) {
			view.findViewById(BUY_IDS.get(ship)).setOnClickListener(this);
			view.findViewById(INFO_IDS.get(ship)).setOnClickListener(this);
		
		}
	}

	@Override
	public void onSingleClick(View v) {
		getGameState().buyShipFormHandleEvent(v.getId());
	}
	
	@Override
	public int getHelpTextResId() {
		return R.string.help_buyship;
	}

	@Override
	public ScreenType getType() {
		return ScreenType.BUYSHIP;
	}

	public static ScreenType.Creator<BuyShipScreen> CREATOR = new ScreenType.Creator<BuyShipScreen>() {

		@Override
		public BuyShipScreen newInstance() {
			return BuyShipScreen.newInstance();
		}
	};

}