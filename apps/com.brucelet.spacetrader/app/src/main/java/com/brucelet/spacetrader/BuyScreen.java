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
import com.brucelet.spacetrader.enumtypes.TradeItem;

public class BuyScreen extends BaseScreen {
	public static final Map<TradeItem, Integer> AMOUNT_IDS; 
	static {
		Map<TradeItem, Integer> ids = new EnumMap<>(TradeItem.class);
		ids.put(TradeItem.WATER,     R.id.screen_buy_water_amount);
		ids.put(TradeItem.FURS,      R.id.screen_buy_furs_amount);
		ids.put(TradeItem.FOOD,      R.id.screen_buy_food_amount);
		ids.put(TradeItem.ORE,       R.id.screen_buy_ore_amount);
		ids.put(TradeItem.GAMES,     R.id.screen_buy_games_amount);
		ids.put(TradeItem.FIREARMS,  R.id.screen_buy_firearms_amount);
		ids.put(TradeItem.MEDICINE,  R.id.screen_buy_medicine_amount);
		ids.put(TradeItem.MACHINERY, R.id.screen_buy_machinery_amount);
		ids.put(TradeItem.NARCOTICS, R.id.screen_buy_narcotics_amount);
		ids.put(TradeItem.ROBOTS,    R.id.screen_buy_robots_amount);
		AMOUNT_IDS = Collections.unmodifiableMap(ids);
	}
	public static final Map<TradeItem, Integer> MAX_IDS; 
	static {
		Map<TradeItem, Integer> ids = new EnumMap<>(TradeItem.class);
		ids.put(TradeItem.WATER,     R.id.screen_buy_water_max);
		ids.put(TradeItem.FURS,      R.id.screen_buy_furs_max);
		ids.put(TradeItem.FOOD,      R.id.screen_buy_food_max);
		ids.put(TradeItem.ORE,       R.id.screen_buy_ore_max);
		ids.put(TradeItem.GAMES,     R.id.screen_buy_games_max);
		ids.put(TradeItem.FIREARMS,  R.id.screen_buy_firearms_max);
		ids.put(TradeItem.MEDICINE,  R.id.screen_buy_medicine_max);
		ids.put(TradeItem.MACHINERY, R.id.screen_buy_machinery_max);
		ids.put(TradeItem.NARCOTICS, R.id.screen_buy_narcotics_max);
		ids.put(TradeItem.ROBOTS,    R.id.screen_buy_robots_max);
		MAX_IDS = Collections.unmodifiableMap(ids);
	}
	public static final Map<TradeItem, Integer> PRICE_IDS; 
	static {
		Map<TradeItem, Integer> ids = new EnumMap<>(TradeItem.class);
		ids.put(TradeItem.WATER,     R.id.screen_buy_water_price);
		ids.put(TradeItem.FURS,      R.id.screen_buy_furs_price);
		ids.put(TradeItem.FOOD,      R.id.screen_buy_food_price);
		ids.put(TradeItem.ORE,       R.id.screen_buy_ore_price);
		ids.put(TradeItem.GAMES,     R.id.screen_buy_games_price);
		ids.put(TradeItem.FIREARMS,  R.id.screen_buy_firearms_price);
		ids.put(TradeItem.MEDICINE,  R.id.screen_buy_medicine_price);
		ids.put(TradeItem.MACHINERY, R.id.screen_buy_machinery_price);
		ids.put(TradeItem.NARCOTICS, R.id.screen_buy_narcotics_price);
		ids.put(TradeItem.ROBOTS,    R.id.screen_buy_robots_price);
		PRICE_IDS = Collections.unmodifiableMap(ids);
	}


	public static BuyScreen newInstance() {
		return new BuyScreen();
	}

	public BuyScreen() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.screen_buy, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		View view = getView();
		for (TradeItem item : TradeItem.values()) {
			view.findViewById(AMOUNT_IDS.get(item)).setOnClickListener(this);
			view.findViewById(MAX_IDS.get(item)).setOnClickListener(this);
		}
	}

	@Override
	public void onRefreshScreen() {
		getGameState().drawBuyCargoForm();
	}

	@Override
	public void onSingleClick(View v) {
		getGameState().buyCargoFormHandleEvent(v.getId());
	}
	
	@Override
	public int getHelpTextResId() {
		return R.string.help_buycargo;
	}

	@Override
	public ScreenType getType() {
		return ScreenType.BUY;
	}

	public static ScreenType.Creator<BuyScreen> CREATOR = new ScreenType.Creator<BuyScreen>() {

		@Override
		public BuyScreen newInstance() {
			return BuyScreen.newInstance();
		}
	};
}
