/*
 * Copyright (C) 2011 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cyanogenmod.cmparts.activities;

import com.cyanogenmod.cmparts.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import java.io.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.Object;

/**
 * Performance Settings
 */
public class PerformanceSettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    private static final String COMPCACHE_PREF = "pref_compcache_size";

    private static final String COMPCACHE_PERSIST_PROP = "persist.service.compcache";

    private static final String COMPCACHE_DEFAULT = SystemProperties.get("ro.compcache.default");

    private static final String GENERAL_CATEGORY = "general_category";

    private static final String JIT_PREF = "pref_jit_mode";

    private static final String JIT_ENABLED = "int:jit";

    private static final String JIT_DISABLED = "int:fast";

    private static final String JIT_PERSIST_PROP = "persist.sys.jit-mode";

    private static final String JIT_PROP = "dalvik.vm.execution-mode";

    private static final String HEAPSIZE_PREF = "pref_heapsize";

    private static final String HEAPSIZE_PROP = "dalvik.vm.heapsize";

    private static final String HEAPSIZE_PERSIST_PROP = "persist.sys.vm.heapsize";

    private static final String HEAPSIZE_DEFAULT = "16m";

    private static final String USE_DITHERING_PREF = "pref_use_dithering";

    private static final String USE_DITHERING_PERSIST_PROP = "persist.sys.use_dithering";
    
    private static final String USE_DITHERING_DEFAULT = "1";

    private static final String USE_16BPP_ALPHA_PREF = "pref_use_16bpp_alpha";

    private static final String USE_16BPP_ALPHA_PROP = "persist.sys.use_16bpp_alpha";

    private static final String PURGEABLE_ASSETS_PREF = "pref_purgeable_assets";

    private static final String PURGEABLE_ASSETS_PERSIST_PROP = "persist.sys.purgeable_assets";

    private static final String PURGEABLE_ASSETS_DEFAULT = "0";

    private static final String DISABLE_BOOTANIMATION_PREF = "pref_disable_bootanimation";

    private static final String DISABLE_BOOTANIMATION_PERSIST_PROP = "persist.sys.nobootanimation";

    private static final String DISABLE_BOOTANIMATION_DEFAULT = "0";

    private static final String LOCK_HOME_PREF = "pref_lock_home";

    private static final String LOCK_MMS_PREF = "pref_lock_mms";

    private static final String WIFI_SCAN_PREF = "pref_wifi_scan_interval";

    private static final String WIFI_SCAN_PROP = "wifi.supplicant_scan_interval";

    private static final String WIFI_SCAN_PERSIST_PROP = "persist.wifi_scan_interval";

    private static final String WIFI_SCAN_DEFAULT = System.getProperty("wifi.supplicant_scan_interval");

    private static final int LOCK_HOME_DEFAULT = 0;

    private static final int LOCK_MMS_DEFAULT = 0;

    private ListPreference mCompcachePref;

    private CheckBoxPreference mJitPref;

    private CheckBoxPreference mUseDitheringPref;

    private CheckBoxPreference mUse16bppAlphaPref;

    private CheckBoxPreference mPurgeableAssetsPref;

    private CheckBoxPreference mDisableBootanimPref;

    private CheckBoxPreference mLockHomePref;

    private CheckBoxPreference mLockMmsPref;

    private ListPreference mHeapsizePref;

    private ListPreference mWifiScanPref;

    private AlertDialog alertDialog;

    private int swapAvailable = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.performance_settings_title_subhead);
        addPreferencesFromResource(R.xml.performance_settings);

        PreferenceScreen prefSet = getPreferenceScreen();
        
        PreferenceCategory generalCategory = (PreferenceCategory)prefSet.findPreference(GENERAL_CATEGORY);

        mCompcachePref = (ListPreference) prefSet.findPreference(COMPCACHE_PREF);
        if (isSwapAvailable()) {
	    if (SystemProperties.get(COMPCACHE_PERSIST_PROP) == "1")
                SystemProperties.set(COMPCACHE_PERSIST_PROP, COMPCACHE_DEFAULT);
            mCompcachePref.setValue(SystemProperties.get(COMPCACHE_PERSIST_PROP, COMPCACHE_DEFAULT));
            mCompcachePref.setOnPreferenceChangeListener(this);
        } else {
            generalCategory.removePreference(mCompcachePref);
        }

        mJitPref = (CheckBoxPreference) prefSet.findPreference(JIT_PREF);
        String jitMode = SystemProperties.get(JIT_PERSIST_PROP,
                SystemProperties.get(JIT_PROP, JIT_ENABLED));
        mJitPref.setChecked(JIT_ENABLED.equals(jitMode));

        mUseDitheringPref = (CheckBoxPreference) prefSet.findPreference(USE_DITHERING_PREF);
        String useDithering = SystemProperties.get(USE_DITHERING_PERSIST_PROP, USE_DITHERING_DEFAULT);
        mUseDitheringPref.setChecked("1".equals(useDithering));

        mUse16bppAlphaPref = (CheckBoxPreference) prefSet.findPreference(USE_16BPP_ALPHA_PREF);
        String use16bppAlpha = SystemProperties.get(USE_16BPP_ALPHA_PROP, "0");
        mUse16bppAlphaPref.setChecked("1".equals(use16bppAlpha));

        mPurgeableAssetsPref = (CheckBoxPreference) prefSet.findPreference(PURGEABLE_ASSETS_PREF);
        String purgeableAssets = SystemProperties.get(PURGEABLE_ASSETS_PERSIST_PROP, PURGEABLE_ASSETS_DEFAULT);
        mPurgeableAssetsPref.setChecked("1".equals(purgeableAssets));

        mHeapsizePref = (ListPreference) prefSet.findPreference(HEAPSIZE_PREF);
        mHeapsizePref.setValue(SystemProperties.get(HEAPSIZE_PERSIST_PROP,
                SystemProperties.get(HEAPSIZE_PROP, HEAPSIZE_DEFAULT)));
        mHeapsizePref.setOnPreferenceChangeListener(this);

        mDisableBootanimPref = (CheckBoxPreference) prefSet.findPreference(DISABLE_BOOTANIMATION_PREF);
        String disableBootanimation = SystemProperties.get(DISABLE_BOOTANIMATION_PERSIST_PROP, DISABLE_BOOTANIMATION_DEFAULT);
        mDisableBootanimPref.setChecked("1".equals(disableBootanimation));

        mLockHomePref = (CheckBoxPreference) prefSet.findPreference(LOCK_HOME_PREF);
        mLockHomePref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.LOCK_HOME_IN_MEMORY, LOCK_HOME_DEFAULT) == 1);

        mLockMmsPref = (CheckBoxPreference) prefSet.findPreference(LOCK_MMS_PREF);
        mLockMmsPref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.LOCK_MMS_IN_MEMORY, LOCK_MMS_DEFAULT) == 1);

        mWifiScanPref = (ListPreference) prefSet.findPreference(WIFI_SCAN_PREF);
        mWifiScanPref.setValue(SystemProperties.get(WIFI_SCAN_PERSIST_PROP,
                SystemProperties.get(WIFI_SCAN_PROP, WIFI_SCAN_DEFAULT)));
        mWifiScanPref.setOnPreferenceChangeListener(this);

        // Set up the warning
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.performance_settings_warning_title);
        alertDialog.setMessage(getResources().getString(R.string.performance_settings_warning));
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                getResources().getString(com.android.internal.R.string.ok),
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        alertDialog.show();
    }
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
	if (preference == mJitPref) {
            SystemProperties.set(JIT_PERSIST_PROP,
                    mJitPref.isChecked() ? JIT_ENABLED : JIT_DISABLED);
            return true;
        }

        if (preference == mUseDitheringPref) {
            SystemProperties.set(USE_DITHERING_PERSIST_PROP,
                    mUseDitheringPref.isChecked() ? "1" : "0");
            return true;
        }

        if (preference == mUse16bppAlphaPref) {
            SystemProperties.set(USE_16BPP_ALPHA_PROP,
                    mUse16bppAlphaPref.isChecked() ? "1" : "0");
            return true;
        }

        if (preference == mPurgeableAssetsPref) {
            SystemProperties.set(PURGEABLE_ASSETS_PERSIST_PROP,
                    mPurgeableAssetsPref.isChecked() ? "1" : "0");
            return true;
        }

        if (preference == mDisableBootanimPref) {
            SystemProperties.set(DISABLE_BOOTANIMATION_PERSIST_PROP,
                    mDisableBootanimPref.isChecked() ? "1" : "0");
            return true;
        }

        if (preference == mLockHomePref) {
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LOCK_HOME_IN_MEMORY, mLockHomePref.isChecked() ? 1 : 0);
            return true;
        }

        if (preference == mLockMmsPref) {
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LOCK_MMS_IN_MEMORY, mLockMmsPref.isChecked() ? 1 : 0);
            return true;
        }

        return false;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (newValue != null) {
            if (preference == mHeapsizePref) {
                SystemProperties.set(HEAPSIZE_PERSIST_PROP, (String)newValue);
                return true;
        }

            if (preference == mCompcachePref) {
                SystemProperties.set(COMPCACHE_PERSIST_PROP, (String)newValue);
                return true;
        }

            if (preference == mWifiScanPref) {
                SystemProperties.set(WIFI_SCAN_PERSIST_PROP, (String)newValue);

                /*
                 * read /system/build.prop then find and replace wifi.supplicant_scan_interval = ###
                 */

                try {
                    BufferedReader in = new BufferedReader(new FileReader("/system/build.prop"));
                    PrintWriter out = new PrintWriter(new File("/tmp/build.prop"));

                    String line;
                    String params[];

                    while ((line = in.readLine()) != null) {
                        params = line.split("="); // some devices have values in ' = ' format vs '='
                    if (params[0].equalsIgnoreCase("wifi.supplicant_scan_interval ") ||
                        params[0].equalsIgnoreCase("wifi.supplicant_scan_interval")) {
                        out.println("wifi.supplicant_scan_interval=" + newValue);
                    } else {
                        out.println(line);
                        }
                    }

                    in.close();
                    out.flush();
                    out.close();

                    // open su shell and write commands to the OutStream for execution
                    Process p = Runtime.getRuntime().exec("su");
                    PrintWriter pw = new PrintWriter(p.getOutputStream());
                    pw.println("busybox mount -o remount,rw /system");
                    pw.println("mv /tmp/build.prop /system/build.prop");
                    pw.close();

                }catch(Exception e) { e.printStackTrace(); }
                return true;
        }
        return false;
        }
    return false;
    }

    /**
     * Check if swap support is available on the system
     */
    private boolean isSwapAvailable() {
        if (swapAvailable < 0) {
            swapAvailable = new File("/proc/swaps").exists() ? 1 : 0;
        }
        return swapAvailable > 0;
    }

}
