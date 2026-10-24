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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brucelet.spacetrader.enumtypes.TradeItem;

public class JettisonDialog extends BaseDialog implements OnSingleClickListener {
	private OnConfirmListener mListener;
	
	public static final Map<TradeItem, Integer> AMOUNT_IDS; 
	static {
		Map<TradeItem, Integer> ids = new EnumMap<>(TradeItem.class);
		ids.put(TradeItem.WATER,     R.id.dialog_plunder_water_amount);
		ids.put(TradeItem.FURS,      R.id.dialog_plunder_furs_amount);
		ids.put(TradeItem.FOOD,      R.id.dialog_plunder_food_amount);
		ids.put(TradeItem.ORE,       R.id.dialog_plunder_ore_amount);
		ids.put(TradeItem.GAMES,     R.id.dialog_plunder_games_amount);
		ids.put(TradeItem.FIREARMS,  R.id.dialog_plunder_firearms_amount);
		ids.put(TradeItem.MEDICINE,  R.id.dialog_plunder_medicine_amount);
		ids.put(TradeItem.MACHINERY, R.id.dialog_plunder_machinery_amount);
		ids.put(TradeItem.NARCOTICS, R.id.dialog_plunder_narcotics_amount);
		ids.put(TradeItem.ROBOTS,    R.id.dialog_plunder_robots_amount);
		AMOUNT_IDS = Collections.unmodifiableMap(ids);
	}
	public static final Map<TradeItem, Integer> ALL_IDS; 
	static {
		Map<TradeItem, Integer> ids = new EnumMap<>(TradeItem.class);
		ids.put(TradeItem.WATER,     R.id.dialog_plunder_water_all);
		ids.put(TradeItem.FURS,      R.id.dialog_plunder_furs_all);
		ids.put(TradeItem.FOOD,      R.id.dialog_plunder_food_all);
		ids.put(TradeItem.ORE,       R.id.dialog_plunder_ore_all);
		ids.put(TradeItem.GAMES,     R.id.dialog_plunder_games_all);
		ids.put(TradeItem.FIREARMS,  R.id.dialog_plunder_firearms_all);
		ids.put(TradeItem.MEDICINE,  R.id.dialog_plunder_medicine_all);
		ids.put(TradeItem.MACHINERY, R.id.dialog_plunder_machinery_all);
		ids.put(TradeItem.NARCOTICS, R.id.dialog_plunder_narcotics_all);
		ids.put(TradeItem.ROBOTS,    R.id.dialog_plunder_robots_all);
		ALL_IDS = Collections.unmodifiableMap(ids);
	}
	
	public static JettisonDialog newInstance(OnConfirmListener listener) {
		JettisonDialog dialog = new JettisonDialog();
		dialog.mListener = listener;
		return dialog;
	}

	public JettisonDialog() {}

	@Override
	public final void onBuildDialog(Builder builder, LayoutInflater inflater, ViewGroup parent) {
		builder.setView(R.layout.dialog_plunder);
		builder.setTitle(R.string.dialog_jettison_title);
		builder.setPositiveButton(R.string.generic_done);
	}
	
	@Override
	public void onClickPositiveButton() {
		if (mListener != null) mListener.onConfirm();
		dismiss();
	}
	
	@Override
	public void onClickNegativeButton() {	// Will be called if user hits back button
		if (mListener != null) mListener.onConfirm();
		dismiss();
	}
	
	@Override
	public void onSingleClick(View v) {
		getGameState().discardCargoFormHandleEvent(v.getId());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		for (TradeItem item : TradeItem.values()) {
			getDialog().findViewById(AMOUNT_IDS.get(item)).setOnClickListener(this);
			getDialog().findViewById(ALL_IDS.get(item)).setOnClickListener(this);
		}
		setViewVisibilityById(R.id.dialog_plunder_dump, false);
	}

	@Override
	public void onRefreshDialog() {
		getGameState().showDumpCargo();
	}

	@Override
	public int getHelpTextResId() {
		return R.string.help_dumpcargo;
	}
}
