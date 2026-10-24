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
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brucelet.spacetrader.datatypes.SolarSystem;
import com.brucelet.spacetrader.enumtypes.ScreenType;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class WarpTargetScreen extends WarpSubScreen {
	
	private static final int[] BUTTON_IDS = {
		R.id.screen_warp_toggle,
		R.id.screen_warp_back_button,
		R.id.screen_warp_warp,
//		R.id.screen_warp_target_cost_specific,
		R.id.screen_warp_prev,
		R.id.screen_warp_next,
	};
	
	public static WarpTargetScreen newInstance() {
		WarpTargetScreen fragment = new WarpTargetScreen();
//		Bundle args = new Bundle();
//		fragment.setArguments(args);
		return fragment;
	}

	public WarpTargetScreen() {
		// Required empty public constructor
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		if (getArguments() != null) {
//		}
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.screen_warp_target, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		for (int id : BUTTON_IDS) {
			getView().findViewById(id).setOnClickListener(this);
		}
	}
	
	@Override
	public void onSingleClick(View v) {
		getGameState().executeWarpFormHandleEvent(v.getId());
	}
	
	@Override
	public int getHelpTextResId() {
		return R.string.help_executewarp;
	}

	@Override
	public ScreenType getType() {
		return ScreenType.TARGET;
	}

//	@Override
//	protected void setPageListeners(View view) {
//		view.findViewById(R.id.screen_warp_target_cost_specific).setOnClickListener(this);
//	}

	@Override
	public ViewPager getPager() {
		return (ViewPager) getView().findViewById(R.id.screen_warp_target_pager);
	}

	@Override
	protected WarpSystemPagerAdapter createPagerAdapter() {
		return new TargetPagerAdapter();
	}

	@Override
	protected void onRefreshWarpSubScreen() {
		getGameState().showExecuteWarp();
	}
		
	private class TargetPagerAdapter extends WarpSystemPagerAdapter {

		@Override
		protected void setNewSystem(SolarSystem system, View page) {
			Log.d("TargetPagerAdapter.setNewSystem", "Setting system "+system);
			getGameState().showExecuteWarpPage(system, page);
		}

		@Override
		protected int layoutResource() {
			return R.layout.viewpager_warp_target;
		}
		
	}

	public static ScreenType.Creator<WarpTargetScreen> CREATOR = new ScreenType.Creator<WarpTargetScreen>() {

		@Override
		public WarpTargetScreen newInstance() {
			return WarpTargetScreen.newInstance();
		}
	};

}
