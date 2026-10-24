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


import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.brucelet.spacetrader.datatypes.GameState;
import com.brucelet.spacetrader.enumtypes.ScreenType;

public class ShortcutDialog extends BaseDialog {
	private static final int[] SPINNER_IDS = {
		R.id.dialog_shortcut_selector1,
		R.id.dialog_shortcut_selector2,
		R.id.dialog_shortcut_selector3,
		R.id.dialog_shortcut_selector4,
	};

	public static ShortcutDialog newInstance() {
		ShortcutDialog fragment = new ShortcutDialog();
		return fragment;
	}

	public ShortcutDialog() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onRefreshDialog() {
		
		final ScreenType[] screens = ScreenType.dropdownValues();

		SpinnerAdapter adapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_dropdown_item_shortcut, new String[screens.length]) {

			@Override
			public View getView(int position, View row, ViewGroup parent)
			{   
				if(row == null)
				{
					LayoutInflater inflater = getActivity().getLayoutInflater();
					row = inflater.inflate(R.layout.spinner_selected_item_shortcut, parent, false);
				}

				setSpinnerText(position, row);

				return row;
			}
			@Override
			public View getDropDownView(int position, View row, ViewGroup parent)
			{
				if(row == null)
				{
					LayoutInflater inflater = getActivity().getLayoutInflater();
					row = inflater.inflate(R.layout.spinner_dropdown_item_shortcut, parent, false);
				}

				setSpinnerText(position, row);

				return row;
			}

			private void setSpinnerText(int position, View row) {
				TextView text1 = (TextView) row.findViewById(R.id.spinner_shortcut);
				text1.setText(screens[position].shortcutId);
				text1.setTypeface(Typeface.MONOSPACE);	// NB Lollipop is not setting this from xml so we need this here.
				TextView text2 = (TextView) row.findViewById(R.id.spinner_text);
				text2.setText(screens[position].spinnerId);
			}
		};

		for (int i = 0; i < SPINNER_IDS.length; i++) {
			Spinner spinner = (Spinner) getDialog().findViewById(SPINNER_IDS[i]);
			spinner.setAdapter(adapter);
			spinner.setSelection(getGameState().getShortcut(i+1));
		}

	}

	@Override
	public final void onBuildDialog(Builder builder, LayoutInflater inflater, ViewGroup parent) {
		builder
		.setTitle(R.string.dialog_shortcut_title)
		.setPositiveButton(R.string.generic_ok)
		.setView(R.layout.dialog_shortcut);
	}

	@Override
	public void onClickPositiveButton() {
		GameState gameState = getGameState();
		for (int i = 0; i < SPINNER_IDS.length; i++) {
			Spinner spinner = (Spinner) getDialog().findViewById(SPINNER_IDS[i]);
			gameState.setShortcut(i+1, spinner.getSelectedItemPosition());
		}
		getGameManager().supportInvalidateOptionsMenu();
		
		dismiss();
	}

	@Override
	public int getHelpTextResId() {
		return R.string.help_shortcutprefs;
	}


}
