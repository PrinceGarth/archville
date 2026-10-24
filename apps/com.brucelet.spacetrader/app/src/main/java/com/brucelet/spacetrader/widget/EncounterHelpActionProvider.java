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
package com.brucelet.spacetrader.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v4.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;

import com.brucelet.spacetrader.HelpDialog;
import com.brucelet.spacetrader.MainActivity;
import com.brucelet.spacetrader.R;

public class EncounterHelpActionProvider extends ActionProvider implements View.OnClickListener {
	
	public EncounterHelpActionProvider(Context context) {
		super(context);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateActionView() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.button_encounter_help, null);
		view.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
//		// TODO Is there a cleaner way to do this?
		
		Context context = getContext();
		while (context instanceof ContextWrapper) {
			Context base = ((ContextWrapper) context).getBaseContext();
			if (base.equals(context)) break; // Since I'm paranoid, make sure we don't get into an infinite loop.
			context = base;
			if (context instanceof MainActivity) break; // Don't go too far because Activity is a ContextWrapper 
		}
		if (context instanceof MainActivity) {
			((MainActivity) context).showDialogFragment(HelpDialog.newInstance(R.string.help_encounter));
		} else {
			throw new IllegalStateException(context.getClass().getName());
		}
	}

}
