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
package com.brucelet.spacetrader.enumtypes;

import com.brucelet.spacetrader.R;

public enum ThemeType {
	HOLO_LIGHT     ( R.style.ActivityTheme_Light,          false ),
	HOLO_DARK      ( R.style.ActivityTheme_Dark,           false ),
	HOLO_PALM      ( R.style.ActivityTheme_Palm,           false ),
	MATERIAL_LIGHT ( R.style.ActivityTheme_Material_Light, true  ),
	MATERIAL_DARK  ( R.style.ActivityTheme_Material_Dark,  true  ),
	;
	
	public final int resId;
	public final boolean isMaterialTheme;
	
	ThemeType(int resId, boolean isMaterialTheme) {
		this.resId = resId;
		this.isMaterialTheme = isMaterialTheme;
	}
}
