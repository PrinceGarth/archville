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

import android.app.Activity;
import android.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

public class ChartFindDialog extends BaseDialog implements TextView.OnEditorActionListener {

	public static ChartFindDialog newInstance() {
		return new ChartFindDialog();
	}
	
	public ChartFindDialog() {}
	
	@Override
	public final void onBuildDialog(Builder builder, LayoutInflater inflater, ViewGroup parent) {
		builder.setTitle(R.string.screen_chart_find_title);
		builder.setPositiveButton(R.string.generic_ok).setNegativeButton(R.string.generic_cancel);
		
		View view = inflater.inflate(R.layout.screen_chart_find, parent, false);
		AutoCompleteTextView input = (AutoCompleteTextView) view.findViewById(R.id.screen_chart_find_value);
		String[] names = getResources().getStringArray(R.array.solar_system_name);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(
				getActivity(), 
				R.layout.autocomplete_dropdown_item, 
				names);
		input.setAdapter(adapter);
		input.setOnEditorActionListener(this);
		
		builder.setView(view);
	}

	@Override
	public void onShowDialog() {
		View input = getDialog().findViewById(R.id.screen_chart_find_value);
		
		// Immediately request IMM so we're focused and see keyboard
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.showSoftInput(input, 0);
	}
		
	@Override
	public void onClickPositiveButton() {
		getGameState().findDialogHandleEvent(AlertDialog.BUTTON_POSITIVE);
	}
	
	@Override
	public void onClickNegativeButton() {
		dismiss();
	}

	@Override
	public int getHelpTextResId() {
		return R.string.help_findsystem;
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE) {
			onClickPositiveButton();
		}
		return false;
	}

}
