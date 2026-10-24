package com.brucelet.spacetrader;

import android.util.Log;

import com.brucelet.spacetrader.datatypes.GameState;

public class Application extends android.app.Application {

	static { Log.w(GameState.LOG_TAG, "Space Trader for Android"); }

	public static final boolean DEVELOPER_MODE = BuildConfig.DEBUG;
	static { if (DEVELOPER_MODE) Log.w(GameState.LOG_TAG, "Developer Mode is active"); }

//	RefWatcher refWatcher;

	@Override
	public void onCreate() {
		super.onCreate();
//		refWatcher = LeakCanary.install(this);
	}

}
