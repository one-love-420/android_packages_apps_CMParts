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

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

import com.cyanogenmod.cmparts.R;

public class UIColorsActivity extends PreferenceActivity {
    
    private static final String COLOR_CLOCK = "color_clock";
    private static final String COLOR_DATE = "color_date";
    private static final String COLOR_NOTIFICATION_TICKER_TEXT = "color_ticker_text";
    private static final String COLOR_BATTERY_PERCENT = "color_battery_percent";
    private static final String COLOR_SIGNALTEXT_VALUE = "color_signaltext_value";
    private static final String COLOR_LABEL_PLMN = "color_label_plmn";
    private static final String COLOR_NOTIFICATION_NONE = "color_notification_none";
    private static final String COLOR_NOTIFICATION_LATEST = "color_notification_latest";
    private static final String COLOR_NOTIFICATION_ONGOING = "color_notification_ongoing";
    private static final String COLOR_NOTIFICATION_CLEAR_BUTTON = "color_clear_button";
    private static final String COLOR_NOTIFICATION_ITEM_TITLE = "color_notification_item_title";
    private static final String COLOR_NOTIFICATION_ITEM_TEXT = "color_notification_item_text";
    private static final String COLOR_NOTIFICATION_ITEM_TIME = "color_notification_item_time";

    private Preference mClockColorPref;
    private Preference mDateColorPref;
    private Preference mNotifTickerColorPref;
    private Preference mBattpercentColorPref;
    private Preference mSignaltextColorPref;
    private Preference mCarrierColorPref;
    private Preference mNoNotifColorPref;
    private Preference mLatestNotifColorPref;
    private Preference mOngoingNotifColorPref;
    private Preference mClearLabelColorPref;
    private Preference mNotifItemTitlePref;
    private Preference mNotifItemTextPref;
    private Preference mNotifItemTimePref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.ui_colors_title);
        addPreferencesFromResource(R.xml.ui_colors);

        PreferenceScreen prefSet = getPreferenceScreen();
  
        mClockColorPref = prefSet.findPreference(COLOR_CLOCK);
        mDateColorPref = prefSet.findPreference(COLOR_DATE);
        mNotifTickerColorPref = prefSet.findPreference(COLOR_NOTIFICATION_TICKER_TEXT);
        mBattpercentColorPref = prefSet.findPreference(COLOR_BATTERY_PERCENT);
        mSignaltextColorPref = prefSet.findPreference(COLOR_SIGNALTEXT_VALUE);
        mCarrierColorPref = prefSet.findPreference(COLOR_LABEL_PLMN);
        mNoNotifColorPref = prefSet.findPreference(COLOR_NOTIFICATION_NONE);
        mLatestNotifColorPref = prefSet.findPreference(COLOR_NOTIFICATION_LATEST);
        mOngoingNotifColorPref = prefSet.findPreference(COLOR_NOTIFICATION_ONGOING);
        mClearLabelColorPref = prefSet.findPreference(COLOR_NOTIFICATION_CLEAR_BUTTON);
        mNotifItemTitlePref = prefSet.findPreference(COLOR_NOTIFICATION_ITEM_TITLE);
        mNotifItemTextPref = prefSet.findPreference(COLOR_NOTIFICATION_ITEM_TEXT);
        mNotifItemTimePref = prefSet.findPreference(COLOR_NOTIFICATION_ITEM_TIME);
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;
        if (preference == mClockColorPref) {
            ColorPickerDialog cp = new ColorPickerDialog(this,
                mClockFontColorListener,
                readClockFontColor());
            cp.show();          
        }

        if (preference == mDateColorPref) {
            ColorPickerDialog cp = new ColorPickerDialog(this,
                mDateFontColorListener,
                readDateFontColor());
            cp.show();          
        }

        if (preference == mNotifTickerColorPref) {
            ColorPickerDialog cp = new ColorPickerDialog(this,
                mTickerFontColorListener,
                readTickerFontColor());
            cp.show();          
        }

        if (preference == mBattpercentColorPref) {
            ColorPickerDialog cp = new ColorPickerDialog(this,
                mBattpercentFontColorListener,
                readBattpercentFontColor());
            cp.show();          
        }

        if (preference == mSignaltextColorPref) {
            ColorPickerDialog cp = new ColorPickerDialog(this,
                mSignaltextFontColorListener,
                readSignaltextFontColor());
            cp.show();          
        }

        if (preference == mCarrierColorPref) {
            ColorPickerDialog cp = new ColorPickerDialog(this,
                mCarrierFontColorListener,
                readCarrierFontColor());
            cp.show();          
        }

        if (preference == mNoNotifColorPref) {
            ColorPickerDialog cp = new ColorPickerDialog(this,
                mNoneFontColorListener,
                readNoneFontColor());
            cp.show();          
        }

        if (preference == mLatestNotifColorPref) {
            ColorPickerDialog cp = new ColorPickerDialog(this,
                mLatestFontColorListener,
                readLatestFontColor());
            cp.show();          
        }

        if (preference == mOngoingNotifColorPref) {
            ColorPickerDialog cp = new ColorPickerDialog(this,
                mOngoingFontColorListener,
                readOngoingFontColor());
            cp.show();          
        }

        if (preference == mClearLabelColorPref) {
            ColorPickerDialog cp = new ColorPickerDialog(this,
                mClearFontColorListener,
                readClearFontColor());
            cp.show();          
        }

        if (preference == mNotifItemTitlePref) {
            ColorPickerDialog cp = new ColorPickerDialog(this,
                mNotifTitleFontColorListener,
                readNotifTitleFontColor());
            cp.show();          
        }

        if (preference == mNotifItemTextPref) {
            ColorPickerDialog cp = new ColorPickerDialog(this,
                mNotifItemFontColorListener,
                readNotifItemFontColor());
            cp.show();          
        }

        if (preference == mNotifItemTimePref) {
            ColorPickerDialog cp = new ColorPickerDialog(this,
                mNotifTimeFontColorListener,
                readNotifTimeFontColor());
            cp.show();          
        }
        return true;
    }

    private int readClockFontColor() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.COLOR_CLOCK);
        }
        catch (SettingNotFoundException e) {
            return -1;
        }
    }
    ColorPickerDialog.OnColorChangedListener mClockFontColorListener = 
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.COLOR_CLOCK, color);
            }
            public void colorUpdate(int color) {
            }
    };

    private int readDateFontColor() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.COLOR_DATE);
        }
        catch (SettingNotFoundException e) {
            return -1;
        }
    }
    ColorPickerDialog.OnColorChangedListener mDateFontColorListener = 
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.COLOR_DATE, color);
            }
            public void colorUpdate(int color) {
            }
    };

    private int readTickerFontColor() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_TICKER_TEXT);
        }
        catch (SettingNotFoundException e) {
            return -1;
        }
    }
    ColorPickerDialog.OnColorChangedListener mTickerFontColorListener = 
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_TICKER_TEXT, color);
            }
            public void colorUpdate(int color) {
            }
    };

    private int readBattpercentFontColor() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.COLOR_BATTERY_PERCENT);
        }
        catch (SettingNotFoundException e) {
            return -1;
        }
    }
    ColorPickerDialog.OnColorChangedListener mBattpercentFontColorListener = 
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.COLOR_BATTERY_PERCENT, color);
            }
            public void colorUpdate(int color) {
            }
    };

    private int readSignaltextFontColor() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.COLOR_SIGNALTEXT_VALUE);
        }
        catch (SettingNotFoundException e) {
            return -1;
        }
    }
    ColorPickerDialog.OnColorChangedListener mSignaltextFontColorListener = 
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.COLOR_SIGNALTEXT_VALUE, color);
            }
            public void colorUpdate(int color) {
            }
    };

    private int readCarrierFontColor() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.COLOR_LABEL_PLMN);
        }
        catch (SettingNotFoundException e) {
            return -1;
        }
    }
    ColorPickerDialog.OnColorChangedListener mCarrierFontColorListener = 
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.COLOR_LABEL_PLMN, color);
            }
            public void colorUpdate(int color) {
            }
    };

    private int readNoneFontColor() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_NONE);
        }
        catch (SettingNotFoundException e) {
            return -1;
        }
    }
    ColorPickerDialog.OnColorChangedListener mNoneFontColorListener = 
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_NONE, color);
            }
            public void colorUpdate(int color) {
            }
    };

    private int readLatestFontColor() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_LATEST);
        }
        catch (SettingNotFoundException e) {
            return -1;
        }
    }
    ColorPickerDialog.OnColorChangedListener mLatestFontColorListener = 
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_LATEST, color);
            }
            public void colorUpdate(int color) {
            }
    };

    private int readOngoingFontColor() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_ONGOING);
        }
        catch (SettingNotFoundException e) {
            return -1;
        }
    }
    ColorPickerDialog.OnColorChangedListener mOngoingFontColorListener = 
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_ONGOING, color);
            }
            public void colorUpdate(int color) {
            }
    };

    private int readClearFontColor() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_CLEAR_BUTTON);
        }
        catch (SettingNotFoundException e) {
            return -16777216;
        }
    }
    ColorPickerDialog.OnColorChangedListener mClearFontColorListener = 
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_CLEAR_BUTTON, color);
            }
            public void colorUpdate(int color) {
            }
    };

    private int readNotifTitleFontColor() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_ITEM_TITLE);
        }
        catch (SettingNotFoundException e) {
            return -16777216;
        }
    }
    ColorPickerDialog.OnColorChangedListener mNotifTitleFontColorListener = 
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_ITEM_TITLE, color);
            }
            public void colorUpdate(int color) {
            }
    };

    private int readNotifItemFontColor() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_ITEM_TEXT);
        }
        catch (SettingNotFoundException e) {
            return -16777216;
        }
    }
    ColorPickerDialog.OnColorChangedListener mNotifItemFontColorListener = 
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_ITEM_TEXT, color);
            }
            public void colorUpdate(int color) {
            }
    };

    private int readNotifTimeFontColor() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_ITEM_TIME);
        }
        catch (SettingNotFoundException e) {
            return -16777216;
        }
    }
    ColorPickerDialog.OnColorChangedListener mNotifTimeFontColorListener = 
        new ColorPickerDialog.OnColorChangedListener() {
            public void colorChanged(int color) {
                Settings.System.putInt(getContentResolver(), Settings.System.COLOR_NOTIFICATION_ITEM_TIME, color);
            }
            public void colorUpdate(int color) {
            }
    };
}
