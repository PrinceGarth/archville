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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class InputDialog extends BaseDialog implements TextView.OnEditorActionListener {
	private int mTitleId = -1;
	private int mMessageId = -1;
	private int mPositiveId = -1;
	private int mNeutralId = -1;
	private int mNegativeId = -1;
	private int mHelpId = -1;
	private OnPositiveListener mPositiveListener;
	private OnNeutralListener mNeutralListener;
	private OnNegativeListener mNegativeListener;
	private Object[] mArgs = null;
	
	public static InputDialog newInstance(int titleId, int messageId, int positiveId, int neutralId, int negativeId, int helpId, OnPositiveListener positive, OnNeutralListener neutral, Object... args) {
		InputDialog dialog = new InputDialog();
		dialog.mTitleId = titleId;
		dialog.mMessageId = messageId;
		dialog.mPositiveId = positiveId;
		dialog.mNeutralId = neutralId;
		dialog.mNegativeId = negativeId;
		dialog.mHelpId = helpId;
		dialog.mPositiveListener = positive;
		dialog.mNeutralListener = neutral;
		dialog.mArgs = args;
		return dialog;
	}
	
	public static InputDialog newInstance(int titleId, int messageId, int positiveId, int neutralId, int negativeId, int helpId, OnPositiveListener positive, OnNeutralListener neutral, OnNegativeListener negative, Object... args) {
		InputDialog dialog = new InputDialog();
		dialog.mTitleId = titleId;
		dialog.mMessageId = messageId;
		dialog.mPositiveId = positiveId;
		dialog.mNeutralId = neutralId;
		dialog.mNegativeId = negativeId;
		dialog.mHelpId = helpId;
		dialog.mPositiveListener = positive;
		dialog.mNeutralListener = neutral;
		dialog.mNegativeListener = negative;
		dialog.mArgs = args;
		return dialog;
	}
	
	public InputDialog() {}
	
	@Override
	public final void onBuildDialog(Builder builder, LayoutInflater inflater, ViewGroup parent) {
		View view = inflater.inflate(R.layout.dialog_input, parent, false);
		builder.setView(view);
		String message = "";
		if (mArgs != null) {
			if (mTitleId >= 0) builder.setTitle(mTitleId, mArgs);
			if (mMessageId >= 0) message = getResources().getString(mMessageId, mArgs);//creator.setMessage(mMessageId, mArgs);
		} else {
			if (mTitleId >= 0) builder.setTitle(mTitleId);
			if (mMessageId >= 0) message = getResources().getString(mMessageId);//creator.setMessage(mMessageId);
		}
		if (mPositiveId >= 0) builder.setPositiveButton(mPositiveId);
		if (mNeutralId >= 0) builder.setNeutralButton(mNeutralId);
		if (mNegativeId >= 0) builder.setNegativeButton(mNegativeId);
		((TextView) view.findViewById(R.id.dialog_input_message)).setText(message);
		((EditText) view.findViewById(R.id.dialog_input_value)).setOnEditorActionListener(this);
	}
	
	@Override
	public void onShowDialog() {
		// Immediately request IMM so we're focused and see keyboard
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.showSoftInput(getDialog().findViewById(R.id.dialog_input_value), 0);
	}
	
	private CharSequence getInputText() {
		EditText input = (EditText) getDialog().findViewById(R.id.dialog_input_value);
		return input.getText();
	}
	
	@Override
	public final void onClickPositiveButton() {
		int value;
		try {
			value = Integer.parseInt(getInputText().toString());
		} catch (NumberFormatException e) {
			value = 0;
		}
		mPositiveListener.onClickPositiveButton(value);
		dismiss();
	}
	
	@Override
	public final void onClickNeutralButton() {
		mNeutralListener.onClickNeutralButton();
		dismiss();
	}
	
	@Override
	public final void onClickNegativeButton() {
		if (mNegativeListener != null) {
			mNegativeListener.onClickNegativeButton();
		}
		dismiss();
	}
	
	public interface OnPositiveListener {
		void onClickPositiveButton(int value);
	}

	public interface OnNeutralListener {
		void onClickNeutralButton();
	}
	
	public interface OnNegativeListener {
		void onClickNegativeButton();
	}

	@Override
	public int getHelpTextResId() {
		return mHelpId;
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE) {
			onClickPositiveButton();
		}
		return false;
	}

}
