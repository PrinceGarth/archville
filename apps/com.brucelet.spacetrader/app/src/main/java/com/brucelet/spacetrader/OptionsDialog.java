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
import android.view.ViewGroup;

public class OptionsDialog extends BaseDialog {
	


	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @return A new instance of fragment OptionsDialog.
	 */
	public static OptionsDialog newInstance() {
		OptionsDialog fragment = new OptionsDialog();
		return fragment;
	}

	public OptionsDialog() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onRefreshDialog() {
		getGameState().showOptions();
	}
	
	@Override
	public final void onBuildDialog(Builder builder, LayoutInflater inflater, ViewGroup parent) {
		builder
		.setTitle(R.string.dialog_options_title)
		.setPositiveButton(R.string.generic_done)
		.setView(R.layout.dialog_options)
		;
	}

	@Override
	public void onClickPositiveButton() {
		getGameState().dismissOptions();
	}

	@Override
	public int getHelpTextResId() {
		return R.string.help_options;
	}


}
