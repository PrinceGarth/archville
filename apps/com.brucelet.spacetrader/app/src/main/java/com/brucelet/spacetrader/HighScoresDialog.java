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

public class HighScoresDialog extends BaseDialog {
	public static final List<Integer> NAME_IDS;
	static {
		List<Integer> ids = new ArrayList<>();
		ids.add(R.id.dialog_highscores_1_name);
		ids.add(R.id.dialog_highscores_2_name);
		ids.add(R.id.dialog_highscores_3_name);
		NAME_IDS = Collections.unmodifiableList(ids);
	}
	public static final List<Integer> PCT_IDS;
	static {
		List<Integer> ids = new ArrayList<>();
		ids.add(R.id.dialog_highscores_1_percent);
		ids.add(R.id.dialog_highscores_2_percent);
		ids.add(R.id.dialog_highscores_3_percent);
		PCT_IDS = Collections.unmodifiableList(ids);
	}
	public static final List<Integer> DESC_IDS;
	static {
		List<Integer> ids = new ArrayList<>();
		ids.add(R.id.dialog_highscores_1_description);
		ids.add(R.id.dialog_highscores_2_description);
		ids.add(R.id.dialog_highscores_3_description);
		DESC_IDS = Collections.unmodifiableList(ids);
	}
	
	public static HighScoresDialog newInstance() {
		return new HighScoresDialog();
	}
	
	public HighScoresDialog() {}
	
	@Override
	public final void onBuildDialog(Builder builder, LayoutInflater inflater, ViewGroup parent) {
		builder.setTitle(R.string.dialog_highscores_title);
		builder.setView(R.layout.dialog_highscores);
		builder.setPositiveButton(R.string.generic_ok);
	}
	
	@Override
	public void onRefreshDialog() {
		getGameState().showHighScores();
		
	}

	@Override
	public int getHelpTextResId() {
		return R.string.help_highscoretable;
	}

}
