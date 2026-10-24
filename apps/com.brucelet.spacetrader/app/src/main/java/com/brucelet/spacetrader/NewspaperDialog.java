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

import android.view.LayoutInflater;
import android.view.ViewGroup;

public class NewspaperDialog extends BaseDialog {
	public static final List<Integer> HEADLINE_IDS;
	static {
		List<Integer> ids = new ArrayList<>();
		ids.add(R.id.dialog_newspaper_headline1);
		ids.add(R.id.dialog_newspaper_headline2);
		ids.add(R.id.dialog_newspaper_headline3);
		ids.add(R.id.dialog_newspaper_headline4);
		ids.add(R.id.dialog_newspaper_headline5);
		HEADLINE_IDS = Collections.unmodifiableList(ids);
	}
	
	public static NewspaperDialog newInstance() {
		return new NewspaperDialog();
	}
	
	public NewspaperDialog() {}
	
	@Override
	public final void onBuildDialog(Builder builder, LayoutInflater inflater, ViewGroup parent) {
		builder.setTitle(getGameState().newspaperTitle());
		builder.setView(R.layout.dialog_newspaper);
		builder.setPositiveButton(R.string.generic_done);
	}
	
	@Override
	public void onClickPositiveButton() {
		dismiss();
	}
	
	@Override
	public void onRefreshDialog() {
		getGameState().drawNewspaperForm();
	}

	@Override
	public int getHelpTextResId() {
		return R.string.help_newspaperhelp;
	}
}
