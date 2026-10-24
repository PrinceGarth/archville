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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.brucelet.spacetrader.datatypes.GameState;
import com.brucelet.spacetrader.enumtypes.EndStatus;
import com.brucelet.spacetrader.enumtypes.ScreenType;
import com.brucelet.spacetrader.enumtypes.ThemeType;
import com.brucelet.spacetrader.widget.MenuDropDownWindow;
import com.brucelet.spacetrader.widget.MenuTouchInterceptor;
import com.brucelet.spacetrader.widget.ShortcutButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.Map;

/*
 * Some random notes:
 * 
 * List of modifications made to original code/behavior (All references to the original palm version refer to the final 1.2.2 release):
 *  - Quest systems can now be optionally randomized.
 *  - When selling cargo in orbit, the other trader will no longer buy more than he has room for.
 *  - During encounters, the opponent ship is now rotated to face the player.
 *  - Encounters can optionally display shield/hull percentages (as in original textual encounters mode) in addition to images.
 *  - Added a checkbox for the upgraded hull to the Cheat dialog.
 *  - Cheat codes now disable scoring as with loading a snapshot in the original. A confirmation window explaining this appears the first time a cheat is used.
 *  - Added a check in the New Commander screen to prevent the player from leaving the name blank.
 *  - It's now possible to restart the disease quest if your ship has been destroyed and the antidote lost. This makes the gameplay behavior consistent with in-game text which tells you to go get more.
 *  - Tribble positions are fully randomized over the entire screen. Clicking on one now causes it to jump to a new position so that buttons and text don't get blocked.
 *  - The customs police encounter is disabled if you are arrested or your ship is destroyed, so the player doesn't get in trouble on the next flight long after the cargo was lost.
 *  - Light, Dark, and Classic Palm OS themes.
 *  - [DISABLED] The Customs Police will no longer penalize you if you've discarded or lost the contraband they're looking for, as long as your record is clean.
 *  - Android button behavior:
 *    - Back button optionally returns to previous screen while docked.
 *    - Volume keys optionally scroll through systems on the galactic chart, target system, and average prices screens, behaving like the palm scroll buttons.
 *  - Some bugfixes, including:
 *    - Fixed issue where moon and tribble buyer news stories would only appear if the system had a status other than Uneventful.
 *    - Fixed issue where Bank screen would only print 'maximum' next to noclaim value on the first day it hits 90%.
 *    - Fix so that newspaper displays headlines about player with police record above hero score and not just equal to it. (This was listed in changelog for v1.2.2 of the original but not actually fixed)
 *    - The option to continuously attack fleeing ships which had no effect in the palm version now behaves as apparently intended by interrupting a continuous attack if the opponent starts fleeing and the option is unchecked.
 *    - Fixed a subtle bug where the wrong system's size was used to determine the amount of trade goods available.
 *    - The easter bunny message no longer displays unless the trade is successful.
 *    - Zeethibal now correctly has no pay.
 *    - The personnel roster now displays correctly if both Jarek and Wild are on board at the same time.
 *    - The Buy Ship screen now disallows buying a ship with only two crew quarters if both Jarek and Wild are aboard.
 *    - Fixed bug that could cause player to get negative money when transferring unique equipment to a new ship.
 *    - Fixed a bug where the wrong system index was used to seed the newspaper, so that changing warpsystem could cause the newspaper masthead to change.
 *    - Added a check so that criminal players are not paid bounties. In game text suggested this already was happening but the bounties were still paid.
 *    - Fixed a bug where stats which were greater than 10 could never see a decrease.
 *    - Fixed a minor bug in computing the value of a ship when the fuel compactor gadget is present.
 *    - News stories no longer display for Dragonfly or Scarab quests if the player doesn't meet the conditions for receiving those quests.
 *  
 * TODO list:
 *  - Bug: Overflowing text on small devices.
 *  - Clean up laggy features:
 *    - Warp subscreen viewpagers
 *  - Write: Tweak help texts for android
 *  - Code: Make quest updates optional? (add another checkbox to NewGameDialog)
 *    - Disease updates
 *    - Customs Police Fix
 *    - Trade in orbit capacity
 *  - Bug: There are instances where dialogs become blank when leaving and returning to app after long time. Look into fragment lifecycle to handle saving state better.
 *    - Use arguments in place of all instance fields? But how to handle listeners and arbitrarily-typed args? This may require a large change to remove anonymous inner class listeners
 *    - This looks ugly but is usually just a minor problem because user can close dialog and repeat whatever action originally spawned it.
 *    - It can be bad however if we're inside an ASyncTask
 *  - Xml/Code: Layout updates to make app organization more modernized?
 *    - Do something with bottom half of screen?
 *    - Separate activities for Title, End, Encounter, Docked
 * 
 * 
 * 
 */
public class MainActivity extends AppCompatActivity implements MenuDropDownWindow.OnDropDownItemClickListener, ActionMode.Callback {

	private static final int[] SHORTCUT_IDS = {
		R.id.menu_shortcut1,
		R.id.menu_shortcut2,
		R.id.menu_shortcut3,
		R.id.menu_shortcut4,
	};
	private char[] mShortcutKeys;

	// NB using LinkedList as a Deque, but Deque interface doesn't exist on android until API 9 so we must specify type as LinkedList for addFirst() and removeFirst() methods.
	private final LinkedList<ScreenType> mBackStack = new LinkedList<>();
	private final LinkedList<BaseDialog> mDialogQueue = new LinkedList<>();
	
	private Toolbar mToolbar;
	private View mTitleView;
	private TextView mTitleText;
	private ImageView mTitleIcon;

	private LinearLayout mFooter;

	private final GameState mGameState = new GameState(this);
	
	private volatile boolean mClicking;
	private volatile boolean mShowingDialog;

	public static final String GAME_1 = "Game 1";
	public static final String GAME_2 = "Game 2";
	static final String SAVEFILE = "SpaceTraderSave";
	
	private String mCurrentGame;
	
	private boolean mWelcomeShown = false;
	
	private ActionMode mActionMode;

	private ScreenType mCurrentScreen = ScreenType.TITLE; // This will be overwritten but it can't be null

	private MenuTouchInterceptor mMenuTouchInterceptor;
	private boolean mDraggingMenuOpen;
	private boolean mBeginMenuDrag;
//	private boolean mMenuLongPress;
	private int[] mDragCoordHelper = new int[2];
	private MenuDropDownWindow[] mPopups = new MenuDropDownWindow[3];

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		mCurrentGame = getPreferences(MODE_PRIVATE).getString("currentGame", GAME_1);
		Log.d("onCreate()", "Current game is "+mCurrentGame);
				
		ThemeType theme;
		if (getIntent().hasExtra("theme")) {
			Log.d("onCreate()", "Setting theme from intent extra");
			int themeIndex = getIntent().getIntExtra("theme", 0);
			if (themeIndex < 0 || themeIndex >= ThemeType.values().length) themeIndex = 0;
			theme = ThemeType.values()[themeIndex];
			mWelcomeShown = true;
		} else {
			Log.d("onCreate()", "Setting theme from saved preferences");
			theme = getThemeType();
		}
		setTheme(theme.resId);
		
		Log.d("onCreate()", "Setting theme " + getResources().getResourceName(theme.resId));
		getSharedPreferences(mCurrentGame, MODE_PRIVATE).edit().putInt("theme", theme.ordinal()).commit();

		super.onCreate(savedInstanceState);

		// Trying this here instead of onPostResume() to avoid certain dialog issues
		mGameState.loadState(getSharedPreferences(mCurrentGame, MODE_PRIVATE));
		
		Log.d("onCreate()", "setContentView() called");
		setContentView(R.layout.activity_main);

		// Prevent image screens (title and endgame) from scrolling, since they bleed past the bottom
		View fragmentContainer = findViewById(R.id.container);
		fragmentContainer.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return getCurrentScreenType().isImage;
			}
		});

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mTitleView = LayoutInflater.from(mToolbar.getContext()).inflate(R.layout.ab_title_main, mToolbar, false);
		mTitleText = (TextView) mTitleView.findViewById(R.id.title);
		mTitleIcon = (ImageView) mTitleView.findViewById(R.id.icon);
		mTitleView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onOptionsItemSelectedWithId(v.getId());
			}
		});
		mTitleView.setOnTouchListener(new OnTouchListener() {
			private GestureDetector gestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

				@Override
				public boolean onDown(MotionEvent e) {
					mBeginMenuDrag = true;
					return false;
				}

				@Override
				public boolean onScroll(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
					// If pointer scrolls beyond the bottom of the view, open the menu
					float x = e2.getX();
					float y = e2.getY();
//					if (mBeginMenuDrag && x > mTitleView.getLeft() && x < mTitleView.getRight() && y > mTitleView.getBottom()) {
					if (mBeginMenuDrag && (x > mTitleView.getRight() || y > mTitleView.getBottom())) {
						mDraggingMenuOpen = true;
						mBeginMenuDrag = false;
						startMenuActionMode();
//					} else if (x < mTitleView.getLeft() || x > mTitleView.getRight() || y < mTitleView.getTop()) {
					} else if (x < mTitleView.getLeft() || y < mTitleView.getTop()) {
						mBeginMenuDrag = false;
					}
					return false;
				}

//				@Override
//				public void onLongPress(MotionEvent e) {
//					mBeginMenuDrag = false;
//					mMenuLongPress = true;
//					mDraggingMenuOpen = true;
//					mTitleView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
//					startMenuActionMode();
//				}

			});
			{
				gestureDetector.setIsLongpressEnabled(false);
			}
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				boolean out = gestureDetector.onTouchEvent(event);
				if (mMenuTouchInterceptor != null) {
					// Forward touch event from TitleView to CommandMenuButton for drag-to-open handling
					mTitleView.getLocationOnScreen(mDragCoordHelper);
					int titleX = mDragCoordHelper[0];
					mMenuTouchInterceptor.getLocationOnScreen(mDragCoordHelper);
					int menuX = mDragCoordHelper[0];
//					if (mMenuLongPress) {
//						MotionEvent upEvent = MotionEvent.obtain(event.getDownTime(), event.getEventTime(), MotionEvent.ACTION_UP, event.getX() + titleX - menuX, event.getY(), event.getMetaState());
//						mMenuTouchInterceptor.dispatchTouchToActiveView(upEvent);
//						MotionEvent downEvent = MotionEvent.obtain(event.getDownTime(), event.getEventTime(), MotionEvent.ACTION_DOWN, event.getX() + titleX - menuX, event.getY(), event.getMetaState());
//						mMenuTouchInterceptor.dispatchTouchToActiveView(downEvent);
//						mMenuLongPress = false;
//					}
					MotionEvent forwardedEvent = MotionEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), event.getX() + titleX - menuX, event.getY(), event.getMetaState());
					mMenuTouchInterceptor.dispatchTouchEvent(forwardedEvent);
				}
				return out;
			}
		});
		mTitleIcon.setVisibility(View.GONE);
		mToolbar.setContentInsetsAbsolute(0, 0);
		mToolbar.addView(mTitleView);
		Toolbar.LayoutParams tbParams = (Toolbar.LayoutParams) mTitleView.getLayoutParams();
		tbParams.height = Toolbar.LayoutParams.MATCH_PARENT;
		mTitleView.setLayoutParams(tbParams);
		setSupportActionBar(mToolbar);

		ActionBar ab = getSupportActionBar();
		//noinspection ConstantConditions
		ab.setDisplayShowHomeEnabled(false);
		ab.setDisplayShowTitleEnabled(false);

		mFooter = (LinearLayout) findViewById(R.id.footer_shortcuts);
		final ScreenType[] screens = ScreenType.dropdownValues();
		mShortcutKeys = new char[screens.length];
		for (int i = 0; i < mShortcutKeys.length; i++) {
			final ScreenType screen = screens[i];
			mShortcutKeys[i] = Character.toLowerCase(getResources().getString(screen.shortcutId).charAt(0));

			ShortcutButton shortcut = new ShortcutButton(this, null, R.attr.footerButtonStyle);
			shortcut.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setCurrentScreenType(screen);
				}
			});
			shortcut.setTitles(getResources().getString(screen.titleId), getResources().getString(screen.shortcutId));
			shortcut.setPadding(0,0,0,0);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
			params.weight = 1;
			shortcut.setLayoutParams(params);
			mFooter.addView(shortcut);
		}

		// This will eat touch events to the screen when the Menu Action Mode is live.
		View touchBlocker = findViewById(R.id.action_mode_touch_blocker);
		touchBlocker.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (mActionMode == null) return false;
				finishMenuActionMode();
				return true;
			}
		});

		Log.d("onCreate()", "Finished");
	}
	
	public ThemeType getThemeType() {
		int themeIndex = getSharedPreferences(mCurrentGame, MODE_PRIVATE).getInt("theme", -1);
		if (themeIndex < 0 || themeIndex >= ThemeType.values().length) {
			return ThemeType.MATERIAL_LIGHT;
		}
		return ThemeType.values()[themeIndex];
	}

	public void setNewTheme(ThemeType theme) {

		Log.d("setNewTheme()", "Setting new theme "+getResources().getResourceName(theme.resId));

		finish();
		Intent intent = new Intent(this, getClass());
		intent.putExtra("theme", theme.ordinal());

		startActivity(intent);
//		overridePendingTransition(0, 0);
	}

	@Override
	protected void onPause() {
		autosave();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
//		mGameState.loadState(getSharedPreferences(mCurrentGame, MODE_PRIVATE));
	}

	@Override
	protected void onPostResume() {
		// NB Moved some stuff from onResume() into here to avoid committing Fragment transactions with state loss.
		super.onPostResume();
		loadCurrentScreen(getSharedPreferences(mCurrentGame, MODE_PRIVATE));

		if (mGameState.identifyStartup() && !mWelcomeShown) {
			// Make sure only one of these ever appears, in case onPostResume() is called again before the dialog is dismissed.
			if (getSupportFragmentManager().findFragmentByTag(IdentifyStartupDialog.class.getName()) == null) {
				showDialogFragment(IdentifyStartupDialog.newInstance());
			}
		}
	}

	public void autosave() {
		saveState(getSharedPreferences(mCurrentGame, MODE_PRIVATE));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		ActionBar ab = getSupportActionBar();
		TypedValue tv = new TypedValue();

		ScreenType screenType = mCurrentScreen;

		if (screenType == null) {
			// We're creating menu before the layouts are all ready, so stop and try again later
			return false;
		}

		if (screenType.docked) {
			getMenuInflater().inflate(R.menu.shortcuts, menu);

			if (getTheme().resolveAttribute(R.attr.actionBarBackgroundDocked, tv, true)) {
				if (tv.type >= TypedValue.TYPE_FIRST_COLOR_INT && tv.type <= TypedValue.TYPE_LAST_COLOR_INT) {
					ab.setBackgroundDrawable(new ColorDrawable(tv.data));
				} else {
					ab.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), tv.resourceId, getTheme()));
				}
			}

			ScreenType[] screens = ScreenType.dropdownValues();
			for (int i = 0; i < SHORTCUT_IDS.length; i++) {
				final MenuItem item = menu.findItem(SHORTCUT_IDS[i]);
				final int screenIndex = getGameState().getShortcut(i+1);
				final String title = getResources().getString(screens[screenIndex].titleId);
				final String shortcut = getResources().getString(screens[screenIndex].shortcutId);
				item.setTitle(title);
				item.setTitleCondensed(shortcut);
				item.setAlphabeticShortcut(shortcut.charAt(0));
				final ShortcutButton button = (ShortcutButton) ((FrameLayout) MenuItemCompat.getActionView(item)).getChildAt(0);
				button.setMenuItem(item);
				button.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						onOptionsItemSelected(item);
					}

				});
			}

			mTitleText.setText(screenType.titleId);
			mTitleIcon.setVisibility(View.GONE);

			getTheme().resolveAttribute(R.attr.actionBarTitleBackground, tv, true);
			mTitleView.setBackgroundResource(tv.resourceId);

		} else {

			if (getTheme().resolveAttribute(R.attr.actionBarBackgroundDefault, tv, true)) {
				if (tv.type >= TypedValue.TYPE_FIRST_COLOR_INT && tv.type <= TypedValue.TYPE_LAST_COLOR_INT) {
					ab.setBackgroundDrawable(new ColorDrawable(tv.data));
				} else {
					ab.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), tv.resourceId, getTheme()));
				}
			}

			if (getCurrentScreenType() == ScreenType.ENCOUNTER) {
				mTitleText.setText(R.string.screen_encounter);
				mTitleIcon.setVisibility(View.GONE);
			} else {
				mTitleText.setText(R.string.app_name);
				mTitleIcon.setVisibility(getThemeType().isMaterialTheme? View.GONE : View.VISIBLE);
			}

			getTheme().resolveAttribute(R.attr.actionBarTitleBackground, tv, true);
			mTitleView.setBackgroundResource(tv.resourceId);
		}

		refreshExtraShortcuts();

		boolean out = super.onCreateOptionsMenu(menu);
		Log.d("onCreateOptionsMenu()", "Options menu (re-)created!");
		return out;
	}

	public void refreshExtraShortcuts() {
		mFooter.setVisibility(getGameState().extraShortcuts() && getCurrentScreenType().docked ? View.VISIBLE : View.GONE);
	}

	private boolean hasWriteExternalPermission() {
		PackageManager pm = getPackageManager();
		int hasPerm = pm.checkPermission(
				android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
				getPackageName());
		return hasPerm == PackageManager.PERMISSION_GRANTED;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getGroupId() == R.id.menu_group_shortcuts) {
			for (int i = 0; i < mShortcutKeys.length; i++) {
				if (item.getAlphabeticShortcut() == mShortcutKeys[i]) {
					setCurrentScreenType(ScreenType.values()[i]);
					return true;
				}

			}
			return false;
		}
		return onOptionsItemSelectedWithId(item.getItemId()) || super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onDropDownItemClick(MenuItem item) {

		if (item == null) return false;

		if (item.getGroupId() == R.id.menu_group_command) {
			for (int i = 0; i < mShortcutKeys.length; i++) {
				if (item.getAlphabeticShortcut() == mShortcutKeys[i]) {
					setCurrentScreenType(ScreenType.values()[i]);
					return true;
				}

			}
			return true;
		}

		return onOptionsItemSelectedWithId(item.getItemId());
	}

	private boolean onOptionsItemSelectedWithId(int id) {

		Log.d("","Selecting menu item with id "+getResources().getResourceEntryName(id));

		switch (id) {
		case R.id.menu_keyboard:
			// NB developer mode only for debugging.
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

			return true;

		case R.id.menu_crash:
			// NB developer mode only for debugging.
			throw new Error("Intentional crash for stack trace debugging");

		case R.id.menu_options:

			showDialogFragment(OptionsDialog.newInstance());
			return true;
		case R.id.menu_shortcuts:

			showDialogFragment(ShortcutDialog.newInstance());

			return true;
		case R.id.menu_scores:
			getGameState().viewHighScores();
			return true;
		case R.id.menu_clearscores:
			showDialogFragment(ConfirmDialog.newInstance(
					R.string.dialog_clearscores_title,
					R.string.dialog_clearscores_message,
					R.string.help_cleartable,
					new OnConfirmListener() {
						@Override
						public void onConfirm() {
							mGameState.initHighScores();
						}
					}, null));
			return true;
		case R.id.menu_new:
			if (getCurrentScreenType() == ScreenType.TITLE) {
				showDialogFragment(NewGameDialog.newInstance());
			} else {
				showDialogFragment(ConfirmDialog.newInstance(
						R.string.dialog_newgame_confirm,
						R.string.dialog_newgame_confirm_message,
						R.string.help_confirmnew,
						new OnConfirmListener() {
							@Override
							public void onConfirm() {
								setCurrentScreenType(ScreenType.TITLE);
								showDialogFragment(NewGameDialog.newInstance());
							}
						}, null));
			}
			return true;
		case R.id.menu_switch:
			showDialogFragment(ConfirmDialog.newInstance(
					R.string.dialog_switchgame_title,
					R.string.dialog_switchgame_message,
					R.string.help_switchgame,
					new OnConfirmListener() {

						@Override
						public void onConfirm() {
							SharedPreferences currentPrefs = getSharedPreferences(mCurrentGame, MODE_PRIVATE);
							saveState(currentPrefs);

							final String otherGame = mCurrentGame.equals(GAME_1)? GAME_2 : GAME_1;
							SharedPreferences otherPrefs = getSharedPreferences(otherGame, MODE_PRIVATE);

							if (!otherPrefs.getBoolean("game started", false)) {
								// start new game
								setCurrentScreenType(ScreenType.TITLE);
								showDialogFragment(SimpleDialog.newInstance(
										R.string.dialog_switchtonew_title,
										R.string.dialog_switchtonew_message,
										R.string.help_switchtonew,
										new OnConfirmListener() {
											@Override
											public void onConfirm() {
												String defaultName = getString(otherGame.equals(GAME_1)? R.string.name_commander : R.string.name_commander2);
												mGameState.switchToNew(defaultName);
											}
										}));

							} else {
								// switch to existing game

								SharedPreferences.Editor otherEditor = otherPrefs.edit();
								if (otherPrefs.getBoolean("sharePreferences", mGameState.sharePreferences())) {
									// If other game shares preferences, then copy from current game.

									mGameState.copyPrefs(currentPrefs, otherEditor);
								}
								// NB For now, theme is always the same in both games so we don't need to reset the activity here.
								// TODO Clean up switch so we can change theme here
								otherEditor.putInt("theme", getThemeType().ordinal());
								otherEditor.commit();

								loadState(otherPrefs);

								showDialogFragment(SimpleDialog.newInstance(
										R.string.dialog_switched_title,
										R.string.dialog_switched_message,
										R.string.help_switched,
										getGameState().nameCommander()));
							}

							mCurrentGame = otherGame;
							getPreferences(MODE_PRIVATE).edit().putString("currentGame", mCurrentGame).commit();
						}
					},
					null));
			return true;
		case R.id.menu_retire:
			showDialogFragment(ConfirmDialog.newInstance(
					R.string.dialog_retire_title,
					R.string.dialog_retire_message,
					R.string.help_retire,
					new OnConfirmListener() {
						@Override
						public void onConfirm() {
							mGameState.showEndGameScreen(EndStatus.RETIRED);
						}
					}, null));
			return true;


		case R.id.menu_savegame:
			saveSnapshot();
			return true;


		case R.id.menu_help_about:
			showDialogFragment(AboutDialog.newInstance());
			return true;

		case R.id.menu_help_acknowledgements:
			showDialogFragment(HelpDialog.newInstance(R.string.help_acknowledgements));
			return true;

		case R.id.menu_help_current:
			showDialogFragment(HelpDialog.newInstance(getCurrentScreen().getHelpTextResId()));
			return true;

		case R.id.menu_help_firststeps:
			showDialogFragment(HelpDialog.newInstance(R.string.help_firststeps));
			return true;

		case R.id.menu_help_howtoplay:
			showDialogFragment(HelpDialog.newInstance(R.string.help_howtoplay));
			return true;

		case R.id.menu_help_helponmenu:
			showDialogFragment(HelpDialog.newInstance(R.string.help_helponmenu));
			return true;

		case R.id.menu_help_skills:
			showDialogFragment(HelpDialog.newInstance(R.string.help_skills));
			return true;

		case R.id.menu_help_shipequipment:
			showDialogFragment(HelpDialog.newInstance(R.string.help_shipequipment));
			return true;

		case R.id.menu_help_trading:
			showDialogFragment(HelpDialog.newInstance(R.string.help_trading));
			return true;

		case R.id.menu_help_traveling:
			showDialogFragment(HelpDialog.newInstance(R.string.help_travelling));
			return true;

		case R.id.menu_help_documentation:
			// Show the documentation html file from the original game, in a webview
			Intent intent = new Intent(this, DocumentationActivity.class);
			intent.putExtra("theme", getThemeType());
			startActivity(intent);
			return true;

		case R.id.home:
		case android.R.id.home:
			startMenuActionMode();
			return true;
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mActionMode != null) {
			switch (MotionEventCompat.getActionMasked(event)) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_POINTER_DOWN:
					finishMenuActionMode();
					return true;
			}
		}

		return super.onTouchEvent(event);


	}


	@Override
	public void onBackPressed() {
		if (finishMenuActionMode()) return;

		if (mGameState.recallScreens() && mBackStack.size() > 0) {
			setCurrentScreenType(mBackStack.removeFirst(), false);
			Log.d("onBackPressed()", "Popping back stack. Remaining states: " + mBackStack.size());
		}
		else {
			showExitDialog();
		}
	}

	private void showExitDialog() {
		BaseDialog dialog = ConfirmDialog.newInstance(
				R.string.dialog_exit_title,
				R.string.dialog_exit_message,
				R.string.help_exit,
				new OnConfirmListener() {
			@Override
			public void onConfirm() {
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						MainActivity.super.onBackPressed();
					}
				});
			}
		}, null);
		showDialogFragment(dialog);
	}

	public GameState getGameState() {
		return mGameState;
	}

	public void clearBackStack() {
		mBackStack.clear();
	}

	public void showDialogFragment(BaseDialog dialog) {
		
		String tag = dialog.getClass().getName();
		Log.d("showDialogFragment()", "Showing fragment "+tag);
		
		if (mShowingDialog) {
			Log.d("showDialogFragment()", "Previous dialog display already in progress!");
			if (!mDialogQueue.contains(dialog)) mDialogQueue.addFirst(dialog);
			return;
		}
		
		mShowingDialog = true;
		
		FragmentManager fm = getSupportFragmentManager();
//		FragmentTransaction ft = fm.beginTransaction();
//		Fragment prev = fm.findFragmentByTag(tag);
//		if (prev != null) {
//			ft.remove(prev);
//		}
//		ft.addToBackStack(null);

		if (getCurrentScreenType() == ScreenType.ENCOUNTER) getGameState().clearButtonAction();
		
		// Show the dialog.
		dialog.show(fm, tag);
	}
	
	private void startMenuActionMode() {

		if (mActionMode != null) {
			return;
		}
		
		if (getCurrentScreenType() == ScreenType.ENCOUNTER) getGameState().clearButtonAction();
		
		mActionMode = startSupportActionMode(this);
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		Log.d("Menu Item Click","Preparing Menu ActionMode");
		// Do nothing
		return false;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		Log.d("Menu Item Click", "Destroying Menu ActionMode");
		mActionMode = null;
		mMenuTouchInterceptor = null;
		mDraggingMenuOpen = false;

		for (int i = 0; i < mPopups.length; i++) {
			MenuDropDownWindow popup = mPopups[i];
			if (popup != null) popup.dismiss();
			mPopups[i] = null;
		}
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		Log.d("Menu Item Click","Creating Menu ActionMode");
		Context context = MainActivity.this;//getSupportActionBar().getThemedContext();
		@SuppressLint("InflateParams") // We have no parent view to pass to inflate()
		View view = LayoutInflater.from(context).inflate(R.layout.menu_action_mode_dropdowns, null);
		final View command =  view.findViewById(R.id.menu_command);
		final View game = view.findViewById(R.id.menu_game);
		final View help = view.findViewById(R.id.menu_help);

		// Remove outlines which appear in lollipop because they're being drawn for buttons and these need to look like spinners
		removeOutline(command, game, help);


		final MenuTouchInterceptor intercepter = (MenuTouchInterceptor) view.findViewById(R.id.touch_intercepter);
		intercepter.requestFocus();


		final boolean showCommand = getCurrentScreenType().docked;
		if (showCommand) {
			intercepter.addForwardingView(command);

			Menu commandMenu = new MenuBuilder(context);
			getMenuInflater().inflate(R.menu.command, commandMenu);
			final MenuDropDownWindow commandDropdown = new MenuDropDownWindow(MainActivity.this, command, commandMenu);
			commandDropdown.setOnDropDownItemClickListener(this);
			mPopups[0] = commandDropdown;

			// If we call commandDropdown,show() directly, the anchor view isn't ready yet, but this works.
			if (!mDraggingMenuOpen) {
				command.post(new Runnable() {
					@Override
					public void run() {
						commandDropdown.show();
					}
				});
			}

		} else {
			command.setVisibility(View.GONE);
		}

		intercepter.addForwardingView(game);
		Menu gameMenu = new MenuBuilder(context);
		getMenuInflater().inflate(R.menu.game, gameMenu);
		if (!getGameState().developerMode())
			gameMenu.removeGroup(R.id.menu_group_extra);    // Dev options eg call keyboard for testing.
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT && !hasWriteExternalPermission()) {
			gameMenu.removeItem(R.id.menu_savegame);
		}
		if (!getCurrentScreenType().docked) {
			gameMenu.removeItem(R.id.menu_retire);
		}
		final MenuDropDownWindow gameDropdown = new MenuDropDownWindow(MainActivity.this, game, gameMenu);
		gameDropdown.setOnDropDownItemClickListener(this);
		mPopups[1] = gameDropdown;

		intercepter.addForwardingView(help);
		Menu helpMenu = new MenuBuilder(context);
		getMenuInflater().inflate(R.menu.help, helpMenu);
		final MenuDropDownWindow helpDropdown = new MenuDropDownWindow(MainActivity.this, help, helpMenu);
		helpDropdown.setOnDropDownItemClickListener(this);
		mPopups[2] = helpDropdown;


		intercepter.post(new Runnable() {
			@Override
			public void run() {
				intercepter.setActiveView(showCommand? command : game);
				mMenuTouchInterceptor = intercepter;
			}
		});


		mode.setCustomView(view);
		view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.abc_fade_in));

//		// This doesn't work because the initial dropdown.show() is not lined up correctly
//		MenuItem dropdowns = menu.add("");
//		MenuItemCompat.setActionView(dropdowns, view);

		return true;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		Log.d("Menu Item Click","Clicking Menu ActionMode");
		// Do nothing
		return false;
	}
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private static void removeOutline(View... views) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			for (View view : views) {
				view.setOutlineProvider(null);
				view.setStateListAnimator(null);
			}
		}
	}
	
	boolean finishMenuActionMode() {
		Log.d("Menu Item Click", "Finishing Menu ActionMode");
		if (mActionMode != null) {
			mActionMode.finish();
			mActionMode = null;
			return true;
		}
		return false;
	}
	
	public BaseScreen getCurrentScreen() {
		return (BaseScreen) getSupportFragmentManager().findFragmentByTag(getResources().getString(mCurrentScreen.titleId));
	}

	public ScreenType getCurrentScreenType() {
		return mCurrentScreen;
	}
	
	void showQueuedDialog() {
		if (mDialogQueue.isEmpty()) return;
		
		showDialogFragment(mDialogQueue.removeFirst());
	}

	public void setCurrentScreenType(ScreenType type) {
		setCurrentScreenType(type, true);
	}

	private void setCurrentScreenType(ScreenType type, boolean addToBackStack) {
		Log.i(GameState.LOG_TAG,"Showing screen: "+type.toXmlString(getResources()));

		ScreenType prevType = getCurrentScreenType();
		finishMenuActionMode();

		BaseScreen next = type.creator.newInstance();

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.container, next, getResources().getString(type.titleId));
//		if (type != prevType) ft.setTransition(addToBackStack? FragmentTransaction.TRANSIT_FRAGMENT_OPEN : FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
		ft.commit();

		if (addToBackStack && prevType != null && type.docked && prevType.docked && type != prevType) {
			mBackStack.addFirst(prevType);
			Log.d("setCurrentScreen()", "Adding " + prevType + " to back stack. Total size is " + mBackStack.size());
		} else if (addToBackStack && type != prevType) {
			mBackStack.clear();
			Log.d("setCurrentScreen()", "Clearing back stack.");
		}

		mCurrentScreen = type;
		
		if (prevType == null || !type.docked || !prevType.docked) supportInvalidateOptionsMenu();
		else mTitleText.setText(type.titleId);	// If we don't recreate menu than we must update title here instead.

	}

	public BaseDialog findDialogByClass(Class<? extends BaseDialog> tag) {
		return ((BaseDialog) getSupportFragmentManager().findFragmentByTag(tag.getName()));
	}

	@Override
	public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
		int keyCode = event.getKeyCode();
		boolean keyDown = event.getMetaState() == 0 && event.getAction() == KeyEvent.ACTION_DOWN;
//		boolean keyDown = event.getAction() == KeyEvent.ACTION_DOWN;

		if (getCurrentScreenType().docked && keyDown) {
			char shortcut = event.getMatch(mShortcutKeys);
			if (shortcut > 0) for (int i = 0; i < mShortcutKeys.length; i++) {
				if (shortcut == mShortcutKeys[i]) {
					setCurrentScreenType(ScreenType.values()[i]);
					return true;
				}
			}
		}
		switch (keyCode) {
		case KeyEvent.KEYCODE_O:
			
			if (keyDown) showDialogFragment(OptionsDialog.newInstance());
			return true;
			
		case KeyEvent.KEYCODE_H:
			if (keyDown) showDialogFragment(HelpDialog.newInstance(getCurrentScreen().getHelpTextResId()));
			return true;

		case KeyEvent.KEYCODE_VOLUME_UP:
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			if (!getGameState().volumeScroll()) break;

			switch (getCurrentScreenType()) {
			case TARGET:
			case AVGPRICES:
			case CHART:
				if (keyDown) return mGameState.scrollSystem(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN);
				else return true;
			default:
				// Drop to super call for default volume-change behavior
				break;
			}
			break;
			
		case KeyEvent.KEYCODE_BACK:
			if (event.isLongPress() && keyDown) {
				findViewById(R.id.dummy_back_button).performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
				showExitDialog();
				return true;
			}
			// Otherwise, we use default key-handling, and push through to onBackPressed().
			break;
			
		case KeyEvent.KEYCODE_MENU:
			if (getCurrentScreenType() == ScreenType.ENCOUNTER) getGameState().clearButtonAction();
			if (event.getAction() == KeyEvent.ACTION_UP && event.getRepeatCount() == 0) {
				if (mActionMode == null) {
					startMenuActionMode();
				} else {
					finishMenuActionMode();
				}
			}
			return true;

		}
		return super.dispatchKeyEvent(event);
	}

	private void saveState(SharedPreferences prefs) {
		SharedPreferences.Editor editor = prefs.edit();
		
		editor.clear();	// We're going to rewrite everything, so make sure we're starting from a blank slate.

		editor.putInt("theme", getThemeType().ordinal());

		editor.putInt("screen type", getCurrentScreenType().ordinal());

		editor.putBoolean("game started", !(getCurrentScreenType() == ScreenType.TITLE || getCurrentScreenType() == ScreenType.ENDGAME));
		
		int s = mBackStack.size();
		editor.putInt("backstack size", s);
		for (int i = 0; i < s; i++) {
			editor.putInt("backstack_"+i, mBackStack.get(i).ordinal());
		}
		
		mGameState.saveState(editor);
		
		editor.commit();
		
		new BackupManager(this).dataChanged();
	}
	
	private void loadState(SharedPreferences prefs) {
		Log.d("loadState()","Begin loading state");
		mGameState.loadState(prefs);
		loadCurrentScreen(prefs);
		Log.d("loadState()","Finished loading state");
	}

	private void loadCurrentScreen(SharedPreferences prefs) {

		if (Application.DEVELOPER_MODE) {

			// Give user choice to avoid losing savegame in the event of an error. In public release will just error normally without try/catch.
			try {
				setCurrentScreenType(ScreenType.values()[prefs.getInt("screen type", ScreenType.TITLE.ordinal())], false);
			} catch (final Exception e) {
				e.printStackTrace();

				showDialogFragment(ConfirmDialog.newInstance(
						"Load Game Failed!",
						"An error occured loading savegame. Would you like to scrap it and start over?",
						-1,
						new OnConfirmListener() {

							@Override
							public void onConfirm() {
								setCurrentScreenType(ScreenType.TITLE);
							}
						},
						new OnCancelListener() {

							@Override
							public void onCancel() {
								throw new RuntimeException(e);
							}
						}));

			}

		} else {
			setCurrentScreenType(ScreenType.values()[prefs.getInt("screen type", ScreenType.TITLE.ordinal())], false);
		}

		mBackStack.clear();
		for (int i = 0, s = prefs.getInt("backstack size", 0); i < s; i++) {
			mBackStack.add(ScreenType.values()[prefs.getInt("backstack_"+i, 0)]);
		}

		supportInvalidateOptionsMenu();
	}

	void startClick() {
		mClicking = true;
//		Log.v("click","Start click");
	}

	void finishClick() {
		mClicking = false;
//		Log.v("click","Finish click");
	}
	
	boolean isClicking() {
		return mShowingDialog || mClicking;
	}
	
	void reportDialogShown() {
		mShowingDialog = false;
	}

//	public void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//
//		SharedPreferences prefs = getSharedPreferences(mCurrentGame, MODE_PRIVATE);
//		saveState(prefs);
//	}
//
//	public void onRestoreInstanceState(Bundle savedInstanceState) {
//		super.onRestoreInstanceState(savedInstanceState);
//
//		SharedPreferences prefs = getSharedPreferences(mCurrentGame, MODE_PRIVATE);
//		loadState(prefs);
//	}
	
//	private void saveStateToPreferences(Bundle state, SharedPreferences prefs) {
//		SharedPreferences.Editor editor = prefs.edit();
//
//		for (String key : state.keySet()) {
//			Object obj = state.get(key);
//
//			if (obj instanceof Integer) {
//				editor.putInt(key, (Integer) obj);
//			} else if (obj instanceof Boolean) {
//				editor.putBoolean(key, (Boolean) obj);
//			} else if (obj instanceof String) {
//				editor.putString(key, (String) obj);
//			} else {
//				throw new IllegalArgumentException("Illegal type while saving state: key="+key+", obj="+obj);
//			}
//		}
//
//		editor.commit();
//	}
//
//	private void loadStateFromPreferences(Bundle state, SharedPreferences prefs) {
//		Map<String, ?> map = prefs.getAll();
//
//		for (String key : map.keySet()) {
//			Object obj = map.get(key);
//
//			if (obj instanceof Integer) {
//				state.putInt(key, (Integer) obj);
//			} else if (obj instanceof Boolean) {
//				state.putBoolean(key, (Boolean) obj);
//			} else if (obj instanceof String) {
//				state.putString(key, (String) obj);
//			} else {
//				throw new IllegalArgumentException("Illegal type while loading state: key="+key+", obj="+obj);
//			}
//		}
//	}
	
	private void saveSnapshot() {
		if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			showDialogFragment(SimpleDialog.newInstance(R.string.dialog_cannotsave_title, R.string.dialog_cannotsave_message, R.string.help_cannotsave));
			return;
		}
		
		SharedPreferences prefs = getSharedPreferences(mCurrentGame, MODE_PRIVATE);
		saveState(prefs);
		Map<String, ?> map = prefs.getAll();
		
		File file = new File(ActivityCompat.getExternalFilesDirs(this, null)[0], SAVEFILE);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			for (String key : map.keySet()) {
				bw.write(map.get(key).getClass().getSimpleName()+","+key+","+map.get(key)+"\n");
			}
			bw.close();
			osw.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			showDialogFragment(SimpleDialog.newInstance(R.string.dialog_cannotsave_title, R.string.dialog_cannotsave_message, R.string.help_cannotsave));
			return;
		}
		
		showDialogFragment(SimpleDialog.newInstance(R.string.dialog_gamesaved_title, R.string.dialog_gamesaved_message, R.string.help_gamesaved));
	}
	
	public boolean loadSnapshot() {
		if (!(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState()))) {
			showDialogFragment(SimpleDialog.newInstance(R.string.dialog_cannotload_title, R.string.dialog_cannotload_message, R.string.help_cannotload));
			return false;
		}
		
		SharedPreferences prefs = getSharedPreferences(mCurrentGame, MODE_PRIVATE);
		File file = new File(ActivityCompat.getExternalFilesDirs(this, null)[0], SAVEFILE);
		
		try {
			SharedPreferences.Editor editor = prefs.edit();
			
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String line = br.readLine();
			while (line != null) {
				String[] tokens = line.split(",",3);
				
				String type = tokens[0];
				String key = tokens[1];
				String value = tokens.length == 3? tokens[2] : "";
				
				if (Integer.class.getSimpleName().equals(type)) {
					editor.putInt(key, Integer.valueOf(value));
				} else if (Boolean.class.getSimpleName().equals(type)) {
					editor.putBoolean(key, Boolean.valueOf(value));
				} else if (String.class.getSimpleName().equals(type)) {
					editor.putString(key, value);
				} else {
					Log.e(GameState.LOG_TAG, "tokens[0] has invalid value "+tokens[0]);
					showDialogFragment(SimpleDialog.newInstance(R.string.dialog_cannotload_title, R.string.dialog_cannotload_message, R.string.help_cannotload));
					br.close();
					isr.close();
					fis.close();
					return false;
				}
				
				line = br.readLine();
			}
			br.close();
			isr.close();
			fis.close();
			editor.commit();
			
			loadState(prefs);
			
		} catch (FileNotFoundException e) {
			showDialogFragment(SimpleDialog.newInstance(R.string.dialog_cannotload_title, R.string.dialog_cannotload_message, R.string.help_cannotload));
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			showDialogFragment(SimpleDialog.newInstance(R.string.dialog_cannotload_title, R.string.dialog_cannotload_message, R.string.help_cannotload));
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public void pagerClick(View view) {
		int id = view.getId();
		if (id == R.id.screen_warp_target_cost_specific) {
			mGameState.executeWarpFormHandleEvent(id);
			return;
		}
//		if (WarpPricesScreen.LABEL_IDS.containsValue(id) || WarpPricesScreen.PRICE_IDS.containsValue(id)) {
		if (WarpPricesScreen.CLICKABLE_IDS.containsValue(id)) {
			mGameState.averagePricesFormHandleEvent(id);
			return;
		}
		
	}
}
