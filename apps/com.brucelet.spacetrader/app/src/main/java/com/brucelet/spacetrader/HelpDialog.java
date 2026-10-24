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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HelpDialog extends BaseDialog {

	public static HelpDialog newInstance(int resId) {
		HelpDialog dialog = new HelpDialog();
		Bundle args = new Bundle();
		args.putInt("resId", resId);
		dialog.setArguments(args);
		return dialog;
	}
	
	public HelpDialog() {}

	@Override
	public final void onBuildDialog(Builder builder, LayoutInflater inflater, ViewGroup parent) {
		int resId = getArguments().getInt("resId");

		View view = inflater.inflate(R.layout.dialog_help, parent, false);
		String message = getGameState().getHelpText(resId);
		((TextView) view.findViewById(R.id.dialog_help_message)).setText(message);
		builder.setView(view);
		builder.setTitle(R.string.dialog_help_title);
		builder.setPositiveButton(R.string.generic_done);
	}
	
	public int getHelpTextResId() {
		return -1;
	}
}
