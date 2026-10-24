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
import android.view.View;
import android.view.ViewGroup;

import com.brucelet.spacetrader.enumtypes.ScreenType;

public class SellEqScreen extends BaseScreen {
	public static final List<Integer> WEAPON_IDS;
	static {
		List<Integer> ids = new ArrayList<>();
		ids.add(R.id.screen_selleq_weapon1);
		ids.add(R.id.screen_selleq_weapon2);
		ids.add(R.id.screen_selleq_weapon3);
		WEAPON_IDS = Collections.unmodifiableList(ids);
	}
	public static final List<Integer> WEAPON_BUTTON_IDS;
	static {
		List<Integer> ids = new ArrayList<>();
		ids.add(R.id.screen_selleq_weapon1_button);
		ids.add(R.id.screen_selleq_weapon2_button);
		ids.add(R.id.screen_selleq_weapon3_button);
		WEAPON_BUTTON_IDS = Collections.unmodifiableList(ids);
	}
	public static final List<Integer> WEAPON_TYPE_IDS;
	static {
		List<Integer> ids = new ArrayList<>();
		ids.add(R.id.screen_selleq_weapon1_type);
		ids.add(R.id.screen_selleq_weapon2_type);
		ids.add(R.id.screen_selleq_weapon3_type);
		WEAPON_TYPE_IDS = Collections.unmodifiableList(ids);
	}
	public static final List<Integer> WEAPON_PRICE_IDS;
	static {
		List<Integer> ids = new ArrayList<>();
		ids.add(R.id.screen_selleq_weapon1_price);
		ids.add(R.id.screen_selleq_weapon2_price);
		ids.add(R.id.screen_selleq_weapon3_price);
		WEAPON_PRICE_IDS = Collections.unmodifiableList(ids);
	}
	public static final List<Integer> SHIELD_IDS;
	static {
		List<Integer> ids = new ArrayList<>();
		ids.add(R.id.screen_selleq_shield1);
		ids.add(R.id.screen_selleq_shield2);
		ids.add(R.id.screen_selleq_shield3);
		SHIELD_IDS = Collections.unmodifiableList(ids);
	}
	public static final List<Integer> SHIELD_BUTTON_IDS;
	static {
		List<Integer> ids = new ArrayList<>();
		ids.add(R.id.screen_selleq_shield1_button);
		ids.add(R.id.screen_selleq_shield2_button);
		ids.add(R.id.screen_selleq_shield3_button);
		SHIELD_BUTTON_IDS = Collections.unmodifiableList(ids);
	}
	public static final List<Integer> SHIELD_TYPE_IDS;
	static {
		List<Integer> ids = new ArrayList<>();
		ids.add(R.id.screen_selleq_shield1_type);
		ids.add(R.id.screen_selleq_shield2_type);
		ids.add(R.id.screen_selleq_shield3_type);
		SHIELD_TYPE_IDS = Collections.unmodifiableList(ids);
	}
	public static final List<Integer> SHIELD_PRICE_IDS;
	static {
		List<Integer> ids = new ArrayList<>();
		ids.add(R.id.screen_selleq_shield1_price);
		ids.add(R.id.screen_selleq_shield2_price);
		ids.add(R.id.screen_selleq_shield3_price);
		SHIELD_PRICE_IDS = Collections.unmodifiableList(ids);
	}
	public static final List<Integer> GADGET_IDS;
	static {
		List<Integer> ids = new ArrayList<>();
		ids.add(R.id.screen_selleq_gadget1);
		ids.add(R.id.screen_selleq_gadget2);
		ids.add(R.id.screen_selleq_gadget3);
		GADGET_IDS = Collections.unmodifiableList(ids);
	}
	public static final List<Integer> GADGET_BUTTON_IDS;
	static {
		List<Integer> ids = new ArrayList<>();
		ids.add(R.id.screen_selleq_gadget1_button);
		ids.add(R.id.screen_selleq_gadget2_button);
		ids.add(R.id.screen_selleq_gadget3_button);
		GADGET_BUTTON_IDS = Collections.unmodifiableList(ids);
	}
	public static final List<Integer> GADGET_TYPE_IDS;
	static {
		List<Integer> ids = new ArrayList<>();
		ids.add(R.id.screen_selleq_gadget1_type);
		ids.add(R.id.screen_selleq_gadget2_type);
		ids.add(R.id.screen_selleq_gadget3_type);
		GADGET_TYPE_IDS = Collections.unmodifiableList(ids);
	}
	public static final List<Integer> GADGET_PRICE_IDS;
	static {
		List<Integer> ids = new ArrayList<>();
		ids.add(R.id.screen_selleq_gadget1_price);
		ids.add(R.id.screen_selleq_gadget2_price);
		ids.add(R.id.screen_selleq_gadget3_price);
		GADGET_PRICE_IDS = Collections.unmodifiableList(ids);
	}

	public static SellEqScreen newInstance() {
		return new SellEqScreen();
	}

	public SellEqScreen() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.screen_selleq, container, false);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		View view = getView();
		for (int id : WEAPON_BUTTON_IDS) {
			view.findViewById(id).setOnClickListener(this);
		}
		for (int id : SHIELD_BUTTON_IDS) {
			view.findViewById(id).setOnClickListener(this);
		}
		for (int id : GADGET_BUTTON_IDS) {
			view.findViewById(id).setOnClickListener(this);
		}
	}

	@Override
	public void onRefreshScreen() {
		getGameState().drawSellEquipment();

	}


	@Override
	public void onSingleClick(View v) {
		getGameState().sellEquipmentFormHandleEvent(v.getId());
		

	}
	
	@Override
	public int getHelpTextResId() {
		return R.string.help_sellequipment;
	}

	@Override
	public ScreenType getType() {
		return ScreenType.SELLEQ;
	}

	public static ScreenType.Creator<SellEqScreen> CREATOR = new ScreenType.Creator<SellEqScreen>() {

		@Override
		public SellEqScreen newInstance() {
			return SellEqScreen.newInstance();
		}
	};

}
