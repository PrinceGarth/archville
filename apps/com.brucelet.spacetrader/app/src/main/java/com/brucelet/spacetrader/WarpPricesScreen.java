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
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brucelet.spacetrader.datatypes.SolarSystem;
import com.brucelet.spacetrader.enumtypes.ScreenType;
import com.brucelet.spacetrader.enumtypes.TradeItem;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class WarpPricesScreen extends WarpSubScreen {
	
	public static final Map<TradeItem, Integer> LABEL_IDS; 
	static {
		Map<TradeItem, Integer> ids = new EnumMap<>(TradeItem.class);
		ids.put(TradeItem.WATER,     R.id.screen_warp_avgprices_water_label);
		ids.put(TradeItem.FURS,      R.id.screen_warp_avgprices_furs_label);
		ids.put(TradeItem.FOOD,      R.id.screen_warp_avgprices_food_label);
		ids.put(TradeItem.ORE,       R.id.screen_warp_avgprices_ore_label);
		ids.put(TradeItem.GAMES,     R.id.screen_warp_avgprices_games_label);
		ids.put(TradeItem.FIREARMS,  R.id.screen_warp_avgprices_firearms_label);
		ids.put(TradeItem.MEDICINE,  R.id.screen_warp_avgprices_medicine_label);
		ids.put(TradeItem.MACHINERY, R.id.screen_warp_avgprices_machinery_label);
		ids.put(TradeItem.NARCOTICS, R.id.screen_warp_avgprices_narcotics_label);
		ids.put(TradeItem.ROBOTS,    R.id.screen_warp_avgprices_robots_label);
		LABEL_IDS = Collections.unmodifiableMap(ids);
	}
	public static final Map<TradeItem, Integer> PRICE_IDS;
	static {
		Map<TradeItem, Integer> ids = new EnumMap<>(TradeItem.class);
		ids.put(TradeItem.WATER,     R.id.screen_warp_avgprices_water_price);
		ids.put(TradeItem.FURS,      R.id.screen_warp_avgprices_furs_price);
		ids.put(TradeItem.FOOD,      R.id.screen_warp_avgprices_food_price);
		ids.put(TradeItem.ORE,       R.id.screen_warp_avgprices_ore_price);
		ids.put(TradeItem.GAMES,     R.id.screen_warp_avgprices_games_price);
		ids.put(TradeItem.FIREARMS,  R.id.screen_warp_avgprices_firearms_price);
		ids.put(TradeItem.MEDICINE,  R.id.screen_warp_avgprices_medicine_price);
		ids.put(TradeItem.MACHINERY, R.id.screen_warp_avgprices_machinery_price);
		ids.put(TradeItem.NARCOTICS, R.id.screen_warp_avgprices_narcotics_price);
		ids.put(TradeItem.ROBOTS,    R.id.screen_warp_avgprices_robots_price);
		PRICE_IDS = Collections.unmodifiableMap(ids);
	}
	public static final Map<TradeItem, Integer> CLICKABLE_IDS;
	static {
		Map<TradeItem, Integer> ids = new EnumMap<>(TradeItem.class);
		ids.put(TradeItem.WATER,     R.id.screen_warp_avgprices_water_layout);
		ids.put(TradeItem.FURS,      R.id.screen_warp_avgprices_furs_layout);
		ids.put(TradeItem.FOOD,      R.id.screen_warp_avgprices_food_layout);
		ids.put(TradeItem.ORE,       R.id.screen_warp_avgprices_ore_layout);
		ids.put(TradeItem.GAMES,     R.id.screen_warp_avgprices_games_layout);
		ids.put(TradeItem.FIREARMS,  R.id.screen_warp_avgprices_firearms_layout);
		ids.put(TradeItem.MEDICINE,  R.id.screen_warp_avgprices_medicine_layout);
		ids.put(TradeItem.MACHINERY, R.id.screen_warp_avgprices_machinery_layout);
		ids.put(TradeItem.NARCOTICS, R.id.screen_warp_avgprices_narcotics_layout);
		ids.put(TradeItem.ROBOTS,    R.id.screen_warp_avgprices_robots_layout);
		CLICKABLE_IDS = Collections.unmodifiableMap(ids);
	}

	private static final int[] BUTTON_IDS = {
		R.id.screen_warp_toggle,
		R.id.screen_warp_back_button,
		R.id.screen_warp_warp,
		R.id.screen_warp_avgprices_diffbutton,
		R.id.screen_warp_prev,
		R.id.screen_warp_next,
	};
	
	public static WarpPricesScreen newInstance() {
		WarpPricesScreen fragment = new WarpPricesScreen();
//		Bundle args = new Bundle();
//		fragment.setArguments(args);
		return fragment;
	}

	public WarpPricesScreen() {
		// Required empty public constructor
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		if (getArguments() != null) {
//		}
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.screen_warp_avgprices, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		for (TradeItem item : TradeItem.values()) {
//			getView().findViewById(LABEL_IDS.get(item)).setOnClickListener(this);
//			getView().findViewById(PRICE_IDS.get(item)).setOnClickListener(this);
//		}
		for (int id : BUTTON_IDS) {
			getView().findViewById(id).setOnClickListener(this);
		}
	}

	@Override
	public void onRefreshWarpSubScreen() {
		getGameState().showAveragePrices();
	}
	
	@Override
	public void onSingleClick(View v) {
		getGameState().averagePricesFormHandleEvent(v.getId());
	}
	
	@Override
	public int getHelpTextResId() {
		return R.string.help_averageprices;
	}

	@Override
	public ScreenType getType() {
		return ScreenType.AVGPRICES;
	}
	
//	@Override
//	protected void setPageListeners(View view) {
//		for (TradeItem item : TradeItem.values()) {
//			view.findViewById(WarpPricesScreen.LABEL_IDS.get(item)).setOnClickListener(this);
//			view.findViewById(WarpPricesScreen.PRICE_IDS.get(item)).setOnClickListener(this);
//		}
//	}

	@Override
	public ViewPager getPager() {
		return (ViewPager) getView().findViewById(R.id.screen_warp_avgprices_pager);
	}

	@Override
	protected WarpSystemPagerAdapter createPagerAdapter() {
		return new PricesPagerAdapter();
	}
		
	private class PricesPagerAdapter extends WarpSystemPagerAdapter {

		@Override
		protected void setNewSystem(SolarSystem system, View page) {
			Log.d("PricesPagerAdapter.setNewSystem", "Setting system "+system);
			getGameState().showAveragePricesPage(system, page);
		}

		@Override
		protected int layoutResource() {
			return R.layout.viewpager_warp_avgprices;
		}
		
	}

	public static ScreenType.Creator<WarpPricesScreen> CREATOR = new ScreenType.Creator<WarpPricesScreen>() {

		@Override
		public WarpPricesScreen newInstance() {
			return WarpPricesScreen.newInstance();
		}
	};

}
