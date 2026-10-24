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

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewGameDialog extends BaseDialog implements OnSingleClickListener {
	private static final List<Integer> CLICKABLE_IDS;
	static {
		List<Integer> ids = new ArrayList<>();
		ids.add(R.id.dialog_newgame_difficultypicker_minus);
		ids.add(R.id.dialog_newgame_difficultypicker_plus);
		ids.add(R.id.dialog_newgame_pilotpicker_minus);
		ids.add(R.id.dialog_newgame_pilotpicker_plus);
		ids.add(R.id.dialog_newgame_fighterpicker_minus);
		ids.add(R.id.dialog_newgame_fighterpicker_plus);
		ids.add(R.id.dialog_newgame_traderpicker_minus);
		ids.add(R.id.dialog_newgame_traderpicker_plus);
		ids.add(R.id.dialog_newgame_engineerpicker_minus);
		ids.add(R.id.dialog_newgame_engineerpicker_plus);
		CLICKABLE_IDS = Collections.unmodifiableList(ids);
	}
	
	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @return A new instance of fragment NewGameFragment.
	 */
	public static NewGameDialog newInstance() {
		NewGameDialog fragment = new NewGameDialog();
		return fragment;
	}

	public NewGameDialog() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public final void onBuildDialog(Builder builder, LayoutInflater inflater, ViewGroup parent) {
		builder
		.setTitle(R.string.dialog_newgame)
		.setView(R.layout.dialog_newgame)
		.setPositiveButton(R.string.generic_ok)
		;
	}
	
	@Override
	public void onRefreshDialog() {
		for (int id : CLICKABLE_IDS) {
			getDialog().findViewById(id).setOnClickListener(this);
		}
		getGameState().showNewGameDialog();
	}
	
	@Override
	public void onClickPositiveButton() { 
		getGameState().newCommanderFormHandleEvent(AlertDialog.BUTTON_POSITIVE);

	}
	
	

	@Override
	public void onSingleClick(View v) {
		getGameState().newCommanderFormHandleEvent(v.getId());

	}

	@Override
	public int getHelpTextResId() {
		return R.string.help_newcommander;
	}
}
