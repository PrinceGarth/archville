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
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.brucelet.spacetrader.datatypes.GameState;
import com.brucelet.spacetrader.enumtypes.XmlString;

public abstract class BaseDialog extends DialogFragment implements ConvenienceMethods, OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Override given LayoutInflater. This is necessary for styles to be applied correctly to dialogs.
		Context context;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			TypedValue tv = new TypedValue();
			MainActivity activity = (MainActivity) getActivity();
			activity.getTheme().resolveAttribute(android.R.attr.dialogTheme, tv, true);
			context = new ContextThemeWrapper(activity, tv.resourceId);
		} else {
			context = getActivity();
		}
		inflater = LayoutInflater.from(context);
//		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
//			inflater = LayoutInflater.from(getActivity());
//		}

		TypedValue tv = new TypedValue();
		TypedArray ta;

		getActivity().getTheme().resolveAttribute(R.attr.dialogLayout, tv, true);
		int layoutRes = tv.resourceId;

		final View root = inflater.inflate(layoutRes, container, false);

		View topPanel = root.findViewById(R.id.topPanel);
		View contentPanel = root.findViewById(R.id.contentPanel);
		View buttonPanel = root.findViewById(R.id.buttonPanel);

		// Manually setting backgrounds from resources since we're not using real AlertDialogs
		getActivity().getTheme().resolveAttribute(R.attr.dialogBackgroundStyle, tv, true);
		ta = getActivity().getTheme().obtainStyledAttributes(tv.resourceId, R.styleable.dialog);
		topPanel.setBackgroundResource(ta.getResourceId(R.styleable.dialog_top, 0));
		contentPanel.setBackgroundResource(ta.getResourceId(R.styleable.dialog_middle, 0));
		buttonPanel.setBackgroundResource(ta.getResourceId(R.styleable.dialog_bottom, 0));
		ta.recycle();
		
		final ViewGroup content = (ViewGroup) contentPanel.findViewById(R.id.content);
		View message = inflater.inflate(R.layout.dialog_message, content, false);
		
		Builder builder = new Builder(getActivity());
		builder.setView(message);
		
		onBuildDialog(builder, inflater, content);
		((TextView)message.findViewById(R.id.dialog_message)).setText(builder.mMessage);
		
		// Title panel.
		((TextView)root.findViewById(R.id.alertTitle)).setText(builder.mTitle);
		
		// Content panel. This might be message or custom view.
		if (builder.mView == null) {
			builder.mView = inflater.inflate(builder.mViewId, container, false);
		}
		content.addView(builder.mView);

		// Button panel
		Button pos = (Button) root.findViewById(R.id.positive);
		Button neg = (Button) root.findViewById(R.id.negative);
		Button neu = (Button) root.findViewById(R.id.neutral);
		
		boolean showPos = builder.mPositiveButton != null && builder.mPositiveButton.length() > 0;
		boolean showNeg = builder.mNegativeButton != null && builder.mNegativeButton.length() > 0;
		boolean showNeu = builder.mNeutralButton != null && builder.mNeutralButton.length() > 0;
		
		if (showPos && (showNeu || showNeg)) {
			View view = root.findViewById(R.id.dividerPositive);
			if (view != null) view.setVisibility(View.VISIBLE);
		}
		if (showNeg && showNeu) {
			View view = root.findViewById(R.id.dividerNegative);
			if (view != null) view.setVisibility(View.VISIBLE);
		}
		// NB we do this asynchronously because otherwise canScrollVertically() always returns false.
		content.post(new Runnable() {
			@Override
			public void run() {
				View contentView = content.getChildAt(0);
				if (ViewCompat.canScrollVertically(contentView, 1)) {
					View dividerTop = root.findViewById(R.id.scrollDividerTop);
					View dividerBottom = root.findViewById(R.id.scrollDividerBottom);
					if (dividerTop != null) dividerTop.setVisibility(View.VISIBLE);
					if (dividerBottom != null) dividerBottom.setVisibility(View.VISIBLE);
				}

			}
		});

		if (showPos) {
			pos.setVisibility(View.VISIBLE);
			pos.setText(builder.mPositiveButton);
			pos.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (getGameManager() == null || getGameManager().isClicking()) return;
					
					getGameManager().startClick();
					onClickPositiveButton();
					getGameManager().finishClick();
				}
			});
		}
		if (showNeg) {
			neg.setVisibility(View.VISIBLE);
			neg.setText(builder.mNegativeButton);
			neg.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (getGameManager() == null || getGameManager().isClicking()) return;
					
					getGameManager().startClick();
					onClickNegativeButton();
					getGameManager().finishClick();
				}
			});
		}
		if (showNeu) {
			neu.setVisibility(View.VISIBLE);
			neu.setText(builder.mNeutralButton);
			neu.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (getGameManager() == null || getGameManager().isClicking()) return;
					
					getGameManager().startClick();
					onClickNeutralButton();
					getGameManager().finishClick();
				}
			});
		}
		
		ImageView infoButton = (ImageView) root.findViewById(R.id.alertHelp);
		if (getHelpTextResId() > 0) {
//			if (root.getContext().getTheme().resolveAttribute(R.attr.colorAccent, tv, true)) {
//				Drawable icon = infoButton.getDrawable();
//				DrawableCompat.setTint(icon, getResources().getColor(tv.resourceId));
//				infoButton.setImageDrawable(icon);
//			}
			infoButton.setVisibility(View.VISIBLE);
			infoButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (getGameManager() == null || getGameManager().isClicking()) return;
					
					getGameManager().startClick();
					onClickHelpButton();
					getGameManager().finishClick();
				}
			});
		} else {
			infoButton.setVisibility(View.INVISIBLE);
		}

		return root;
	}

//	@Override
//	public void onViewCreated(View view, Bundle savedInstanceState) {
//		super.onViewCreated(view, savedInstanceState);
//
//		ViewGroup content = (ViewGroup) view.findViewById(R.id.content);
//		View contentView = content.getChildAt(0);
//		if (ViewCompat.canScrollVertically(contentView, 1)) {
//			View dividerTop = view.findViewById(R.id.scrollDividerTop);
//			View dividerBottom = view.findViewById(R.id.scrollDividerBottom);
//			if (dividerTop != null) dividerTop.setVisibility(View.VISIBLE);
//			if (dividerBottom != null) dividerBottom.setVisibility(View.VISIBLE);
//		}
//	}

	@Override
	public void onAttach(Activity activity) {
		if (!(activity instanceof MainActivity)) {
			throw new IllegalArgumentException(BaseDialog.class.getSimpleName()+" Fragment must attach to spacetrader "+MainActivity.class.getSimpleName()+" class");
		}
		super.onAttach(activity);
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);			// Must call super here for proper backstack management. This was a really annoying bug.
		
		if (getGameManager() != null) getGameManager().showQueuedDialog();
	}	
	
	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		onClickNegativeButton(); // NB This will protect against lockups due to dialog dismissal in asynctasks as long as dialogs all use negative button for dismissal/unlock
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getGameManager().finishMenuActionMode();
		onRefreshDialog();
	}

	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		TypedValue tv = new TypedValue();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && getActivity().getTheme().resolveAttribute(android.R.attr.dialogTheme, tv, true)) {
			Log.d("BaseDialog", "Setting Dialog Theme");
			setStyle(STYLE_NO_FRAME, tv.resourceId);
		} else {
			Log.d("BaseDialog", "Not setting Dialog Theme");
			setStyle(STYLE_NO_FRAME, 0);
		}

		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.setOnShowListener(new DialogInterface.OnShowListener() { // NB OnShowListener requires API 8
			
			@Override
			public void onShow(DialogInterface dialog) {
				Log.d("onShow()", "Dialog "+this+" is showing.");
				onShowDialog();
				getGameManager().reportDialogShown();
			}
		});

		// STYLE_NO_FRAME removes animation style so add it back in
		Window window = dialog.getWindow();
		WindowManager.LayoutParams windowAttributes = window.getAttributes();
		windowAttributes.windowAnimations = android.R.style.Animation_Dialog;
		
		// Also missing background dimming since not using standard AlertDialog so throw that up too
		window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		windowAttributes.dimAmount = 0.6f; // NB Just hardcoding this right now although it might be technically more correct to grab it from style.
		
		window.setAttributes(windowAttributes);

		return dialog;
	}
	
	public void setTitle(CharSequence title) {
		((TextView)getDialog().findViewById(R.id.alertTitle)).setText(title);
	}
	
	@Override
	public MainActivity getGameManager() {
		return ((MainActivity) getActivity());
	}

	@Override
	public GameState getGameState() {
		return ((MainActivity) getActivity()).getGameState();
	}

	@Override
	public void setViewTextById(int viewId, int textId) {
		((TextView) getDialog().findViewById(viewId)).setText(textId);
	}

	@Override
	public void setViewTextById(int viewId, int textId, Object... args) {
		XmlString.Static.processXmlArguements(args, getResources());
		((TextView) getDialog().findViewById(viewId)).setText(getResources().getString(textId, args));
	}

	@Override
	public void setViewTextById(int viewId, CharSequence text) {
		((TextView) getDialog().findViewById(viewId)).setText(text);
	}

	@Override
	public void setViewTextById(int viewId, XmlString item) {
		((TextView) getDialog().findViewById(viewId)).setText(item.toXmlString(getResources()));
	}

	@Override
	public void setViewVisibilityById(int viewId, boolean visOrInvis, boolean invisOrGone) {
		getDialog().findViewById(viewId).setVisibility(visOrInvis ? View.VISIBLE : invisOrGone ? View.INVISIBLE : View.GONE);
	}

	@Override
	public void setViewVisibilityById(int viewId, boolean visOrInvis) {
		setViewVisibilityById(viewId, visOrInvis, true);
	}

	@Override
	public final void onClick(View view) {
		if (!(this instanceof OnSingleClickListener)) return;
		if (getGameManager() == null || getGameManager().isClicking()) return;
		
		getGameManager().startClick();
		((OnSingleClickListener)this).onSingleClick(view);
		getGameManager().finishClick();
	}
	
	public final void onClickHelpButton() {
		getGameManager().showDialogFragment(HelpDialog.newInstance(getHelpTextResId()));
	}

	// Hooks to be used by derived classes
	public void onShowDialog() {}
	public void onRefreshDialog() {}
	public void onBuildDialog(Builder builder, LayoutInflater inflater, ViewGroup parent) {}
	public void onClickPositiveButton() { dismiss(); }
	public void onClickNeutralButton() { dismiss(); }
	public void onClickNegativeButton() { dismiss(); }

	public abstract int getHelpTextResId();
	
	public static class Builder {
		private final Context mContext;
		private CharSequence mMessage;
		private CharSequence mTitle;
		private CharSequence mPositiveButton;
		private CharSequence mNeutralButton;
		private CharSequence mNegativeButton;
		private View mView;
		private int mViewId;
		
		private Builder(Context context) {
			mContext = context;
		}
		
		public Builder setPositiveButton(int textId) {
			mPositiveButton = mContext.getString(textId);
			return this;
		}
		
		public Builder setPositiveButton(CharSequence text) {
			mPositiveButton = text;
			return this;
		}
		
		public Builder setNeutralButton(int textId) {
			mNeutralButton = mContext.getString(textId);
			return this;
		}
		
		public Builder setNeutralButton(CharSequence text) {
			mNeutralButton = text;
			return this;
		}
		
		public Builder setNegativeButton(int textId) {
			mNegativeButton = mContext.getString(textId);
			return this;
		}
		
		public Builder setNegativeButton(CharSequence text) {
			mNegativeButton = text;
			return this;
		}

		public Builder setTitle(int titleId) {
			mTitle = mContext.getResources().getString(titleId);
			return this;
		}

		public Builder setTitle(int titleId, Object... args) {
			XmlString.Static.processXmlArguements(args, mContext.getResources());
			mTitle = mContext.getResources().getString(titleId, args);
			return this;
		}
		
		public Builder setTitle(CharSequence title) {
			mTitle = title;
			return this;
		}
		
		public Builder setView(View view) {
			mView = view;
			mViewId = 0;
			return this;
		}
		
		public Builder setView(int viewId) {
			mViewId = viewId;
			mView = null;
			return this;
		}
		
		public Builder setMessage(int messageId) {
			mMessage = mContext.getResources().getString(messageId);
			return this;
		}
		
		public Builder setMessage(int messageId, Object... args) {
			XmlString.Static.processXmlArguements(args, mContext.getResources());
			mMessage = mContext.getResources().getString(messageId, args);
			return this;
		}
		
		public Builder setMessage(CharSequence message) {
			mMessage = message;
			return this;
		}
		
	}

}
