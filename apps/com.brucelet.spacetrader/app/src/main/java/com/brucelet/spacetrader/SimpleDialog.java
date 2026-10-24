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

import android.view.LayoutInflater;
import android.view.ViewGroup;


public class SimpleDialog extends BaseDialog {
	private int mTitleId = -1;
	private int mMessageId = -1;
	private int mHelpId = -1;
	private CharSequence mTitle = "";
	private CharSequence mMessage = "";
	private OnConfirmListener mListener = null;
	private Object[] mArgs = null;
	
	public static SimpleDialog newInstance(final int titleId, final int messageId, final int helpId, final OnConfirmListener listener, final Object... args) {
		SimpleDialog dialog = new SimpleDialog();
		dialog.mTitleId = titleId;
		dialog.mMessageId = messageId;
		dialog.mHelpId = helpId;
		dialog.mListener = listener;
		dialog.mArgs = args;
		return dialog;
	}

	public static SimpleDialog newInstance(final int titleId, final int messageId, final int helpId, final Object... args) {
		SimpleDialog dialog = new SimpleDialog();
		dialog.mTitleId = titleId;
		dialog.mMessageId = messageId;
		dialog.mHelpId = helpId;
		dialog.mArgs = args;
		return dialog;
	}

	public static SimpleDialog newInstance(final int titleId, final int messageId, final int helpId, final OnConfirmListener listener) {
		SimpleDialog dialog = new SimpleDialog();
		dialog.mTitleId = titleId;
		dialog.mMessageId = messageId;
		dialog.mHelpId = helpId;
		dialog.mListener = listener;
		return dialog;
	}

	public static SimpleDialog newInstance(final CharSequence title, final CharSequence message, final int helpId, final OnConfirmListener listener) {
		SimpleDialog dialog = new SimpleDialog();
		dialog.mTitle = title;
		dialog.mMessage = message;
		dialog.mHelpId = helpId;
		dialog.mListener = listener;
		return dialog;
	}

	public static SimpleDialog newInstance(final int titleId, final int messageId, final int helpId) {
		SimpleDialog dialog = new SimpleDialog();
		dialog.mTitleId = titleId;
		dialog.mMessageId = messageId;
		dialog.mHelpId = helpId;
		return dialog;
	}

	public static SimpleDialog newInstance(final CharSequence title, final CharSequence message, final int helpId) {
		SimpleDialog dialog = new SimpleDialog();
		dialog.mTitle = title;
		dialog.mMessage = message;
		dialog.mHelpId = helpId;
		return dialog;
	}

	public SimpleDialog() {}
	
	@Override
	public final void onBuildDialog(Builder builder, LayoutInflater inflater, ViewGroup parent) {
		builder.setPositiveButton(R.string.generic_ok);
		if (mTitle != null && mTitle.length() > 0) builder.setTitle(mTitle);
		if (mMessage != null && mMessage.length() > 0) builder.setMessage(mMessage);
		if (mArgs != null) {
			if (mTitleId >= 0) builder.setTitle(mTitleId, mArgs);
			if (mMessageId >= 0) builder.setMessage(mMessageId, mArgs); 
		} else {
			if (mTitleId >= 0) builder.setTitle(mTitleId);
			if (mMessageId >= 0) builder.setMessage(mMessageId); 
		}
	}
	
	@Override
	public final void onClickPositiveButton() {
		dismiss(); 
		if (mListener != null) {
			mListener.onConfirm();
		}
	}
	
	@Override
	public final void onClickNegativeButton() {
		dismiss(); 
		if (mListener != null) {
			mListener.onConfirm();
		}
	}

	@Override
	public int getHelpTextResId() {
		return mHelpId;
	}
	
}
