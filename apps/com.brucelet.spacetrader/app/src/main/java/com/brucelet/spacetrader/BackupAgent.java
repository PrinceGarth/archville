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

import java.io.IOException;

import android.annotation.TargetApi;
import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.SharedPreferencesBackupHelper;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.FROYO)
public class BackupAgent extends BackupAgentHelper {
	
	private static final String sTag = "Space Trader Backup";

	// Allocate a helper and add it to the backup agent
	@Override
	public void onCreate() {
		Log.i(sTag, "Creating BackupAgent.");
		SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, MainActivity.GAME_1, MainActivity.GAME_2);
		addHelper("prefs backup", helper);
	}

	@Override
	public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) throws IOException {
		Log.i(sTag, "Backing up data...");
		super.onBackup(oldState, data, newState);
		Log.i(sTag, "Backup complete.");

	}

	@Override
	public void onRestore (BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState) throws IOException {
		Log.i(sTag, "Restoring data...");
		super.onRestore(data, appVersionCode, newState);
		Log.i(sTag, "Restore complete.");
	}
}
