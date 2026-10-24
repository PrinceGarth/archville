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

import android.support.v4.app.Fragment;

import com.brucelet.spacetrader.datatypes.GameState;
import com.brucelet.spacetrader.enumtypes.XmlString;

/**
 * An interface for useful shortcut methods to be implemented by BaseScreen and BaseDialog.
 * Mostly this is used to reduce verbosity of calls to findViewById().
 * @author Russell
 *
 */
interface ConvenienceMethods {
	
	/**
	 * Access the {@link GameManager} interface this fragment is attached to.
	 * @return {@link Fragment#getActivity()} cast as a {@link GameManager}.
	 */
	MainActivity getGameManager();
	
	/**
	 * Convenience method to call getGameState() on the attached {@link GameManager}
	 * @return {@link GameManager#getGameState()} called on {@link getGameManager()}
	 */
	GameState getGameState();
	
	/**
	 * Convenience method for setting text of a TextView with the specified id
	 * @param viewId The id of the TextView to update
	 * @param textId The string resource id of the text to place in that view
	 */
	void setViewTextById(int viewId, int textId);

	/**
	 * Convenience method for setting text of a TextView with the specified id
	 * @param viewId The id of the TextView to update
	 * @param textId The string resource id of the text to place in that view
	 * @param args Optional formatting arguments. Implementation should handle
	 * the use of {@link XmlString} enums in place of String objects by calling
	 * toXmlString() instead of toString().
	 */
	void setViewTextById(int viewId, int textId, Object... args);
	
	/**
	 * Convenience method for setting text of a TextView with the specified id
	 * @param viewId The id of the TextView to update
	 * @param text The text to place in that view
	 */
	void setViewTextById(int viewId, CharSequence text);
	
	/**
	 * Convenience method for setting text of a TextView with the specified id
	 * @param viewId The id of the TextView to update
	 * @param item An XmlString enum to use as content for the TextView
	 */
	void setViewTextById(int viewId, XmlString item);
	
	/**
	 * Convenience method for setting visibility of a View with the specified id.
	 * If the first argument is true, the View will be set to VISIBLE. Otherwise, 
	 * if the second argument is true the View will be set to INVISIBLE, and if 
	 * it is false the View will be set to GONE.
	 * @param viewId The id of the View to update
	 */
	void setViewVisibilityById(int viewId, boolean visOrInvis, boolean invisOrGone);
	
	/**
	 * Convenience method for setting visibility of a View with the specified id
	 * @param viewId The id of the View to update
	 * @param visOrInvis View visibility will be set to VISIBLE if this is true and INVISIBLE otherwise
	 */
	void setViewVisibilityById(int viewId, boolean visOrInvis);
}
