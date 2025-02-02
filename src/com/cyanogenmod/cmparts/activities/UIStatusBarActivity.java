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

import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.widget.EditText;

import com.cyanogenmod.cmparts.R;
import com.cyanogenmod.cmparts.activities.ColorPickerDialog.OnColorChangedListener;

public class UIStatusBarActivity extends PreferenceActivity implements OnPreferenceChangeListener {

    private static final String PREF_STATUS_BAR_AM_PM = "pref_status_bar_am_pm";

    private static final String PREF_STATUS_BAR_CLOCK = "pref_status_bar_clock";

    private static final String PREF_STATUS_BAR_CM_BATTERY = "pref_status_bar_cm_battery";

    private static final String PREF_STATUS_BAR_CM_BATTERY_COLOR = "pref_status_bar_cm_battery_color";

    private static final String PREF_STATUS_BAR_CM_BATTERY_LOW_BATT = "pref_status_bar_cm_battery_low_batt";

    private static final String PREF_STATUS_BAR_CARRIER_LABEL = "pref_status_bar_carrier_label";

    private static final String PREF_STATUS_BAR_CARRIER_LABEL_CUSTOM = "pref_status_bar_carrier_label_custom";

    private static final String PREF_STATUS_BAR_COMPACT_CARRIER = "pref_status_bar_compact_carrier";

    private static final String PREF_STATUS_BAR_BRIGHTNESS_CONTROL = "pref_status_bar_brightness_control";

    private static final String PREF_STATUS_BAR_CM_SIGNAL = "pref_status_bar_cm_signal";

    private static final String PREF_STATUS_BAR_HEADSET = "pref_status_bar_headset";

    private static final String STATUSBAR_HIDE_ALARM = "statusbar_hide_alarm";

    private ListPreference mStatusBarAmPm;

    private ListPreference mStatusBarCmSignal;

    private ListPreference mStatusBarCmBattery;

    private ListPreference mStatusBarCmBatteryColor;

    private CheckBoxPreference mStatusBarCmBatteryLowBatt;

    private ListPreference mStatusBarCarrierLabel;

    private ListPreference mStatusBarClock;

    private CheckBoxPreference mStatusBarCompactCarrier;

    private CheckBoxPreference mStatusBarBrightnessControl;

    private CheckBoxPreference mStatusBarHeadset;

    private CheckBoxPreference mHideAlarm;

    private EditTextPreference mStatusBarCarrierLabelCustom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.ui_status_bar_title);
        addPreferencesFromResource(R.xml.ui_status_bar);

        PreferenceScreen prefSet = getPreferenceScreen();

        mStatusBarCompactCarrier = (CheckBoxPreference) prefSet
                .findPreference(PREF_STATUS_BAR_COMPACT_CARRIER);
        mStatusBarCmBattery = (ListPreference) prefSet.findPreference(PREF_STATUS_BAR_CM_BATTERY);
        mStatusBarCmBatteryColor = (ListPreference) prefSet
                .findPreference(PREF_STATUS_BAR_CM_BATTERY_COLOR);
        mStatusBarCmBatteryLowBatt = (CheckBoxPreference) prefSet
                .findPreference(PREF_STATUS_BAR_CM_BATTERY_LOW_BATT);
        mStatusBarBrightnessControl = (CheckBoxPreference) prefSet
                .findPreference(PREF_STATUS_BAR_BRIGHTNESS_CONTROL);
        mStatusBarHeadset = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_HEADSET);
        mHideAlarm = (CheckBoxPreference) prefSet.findPreference(STATUSBAR_HIDE_ALARM);

        mStatusBarCompactCarrier.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_COMPACT_CARRIER, 0) == 1));
        mStatusBarBrightnessControl.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_BRIGHTNESS_TOGGLE, 0) == 1));
        mStatusBarHeadset.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_HEADSET, 1) == 1));
        mHideAlarm.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.STATUSBAR_HIDE_ALARM, 0) == 1));

        try {
            if (Settings.System
                    .getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                mStatusBarBrightnessControl.setEnabled(false);
                mStatusBarBrightnessControl.setSummary(R.string.ui_status_bar_toggle_info);
            }
        } catch (SettingNotFoundException e) {
        }

        mStatusBarClock = (ListPreference) prefSet.findPreference(PREF_STATUS_BAR_CLOCK);
        mStatusBarAmPm = (ListPreference) prefSet.findPreference(PREF_STATUS_BAR_AM_PM);
        mStatusBarCmSignal = (ListPreference) prefSet.findPreference(PREF_STATUS_BAR_CM_SIGNAL);

        int statusBarClock = Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_CLOCK, 1);
        mStatusBarClock.setValue(String.valueOf(statusBarClock));
        mStatusBarClock.setOnPreferenceChangeListener(this);

        int statusBarAmPm = Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_AM_PM, 2);
        mStatusBarAmPm.setValue(String.valueOf(statusBarAmPm));
        mStatusBarAmPm.setOnPreferenceChangeListener(this);

        int signalStyle = Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_CM_SIGNAL_TEXT, 0);
        mStatusBarCmSignal.setValue(String.valueOf(signalStyle));
        mStatusBarCmSignal.setOnPreferenceChangeListener(this);

        int statusBarCmBattery = Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_CM_BATTERY, 0);
        mStatusBarCmBattery.setValue(String.valueOf(statusBarCmBattery));
        mStatusBarCmBattery.setOnPreferenceChangeListener(this);

        mStatusBarCmBatteryColor.setValue(Settings.System.getString(getContentResolver(),
                Settings.System.STATUS_BAR_CM_BATTERY_COLOR));
        mStatusBarCmBatteryColor.setOnPreferenceChangeListener(this);
        mStatusBarCmBatteryColor.setEnabled(statusBarCmBattery == 2);

        mStatusBarCmBatteryLowBatt.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_CM_BATTERY_LOW_BATT, 1) == 1);
        mStatusBarCmBatteryLowBatt.setEnabled(statusBarCmBattery == 2);

        mStatusBarCarrierLabel = (ListPreference) prefSet
                .findPreference(PREF_STATUS_BAR_CARRIER_LABEL);
        mStatusBarCarrierLabelCustom = (EditTextPreference) prefSet
                .findPreference(PREF_STATUS_BAR_CARRIER_LABEL_CUSTOM);

        if (mStatusBarCarrierLabelCustom != null) {
            EditText carrierEditText = mStatusBarCarrierLabelCustom.getEditText();

            if (carrierEditText != null) {
                InputFilter lengthFilter = new InputFilter.LengthFilter(20);
                carrierEditText.setFilters(new InputFilter[]{lengthFilter});
                carrierEditText.setSingleLine(true);
            }
        }

        int statusBarCarrierLabel = Settings.System.getInt(getContentResolver(),
                Settings.System.CARRIER_LABEL_TYPE, 0);
        String statusBarCarrierLabelCustom = Settings.System.getString(getContentResolver(),
                Settings.System.CARRIER_LABEL_CUSTOM_STRING);

        if (statusBarCarrierLabelCustom == null) {
            statusBarCarrierLabelCustom = "CyanogenMod 7";
            Settings.System.putString(getContentResolver(),
                    Settings.System.CARRIER_LABEL_CUSTOM_STRING,
		    statusBarCarrierLabelCustom);
	}

        mStatusBarCarrierLabel.setValue(String.valueOf(statusBarCarrierLabel));
        mStatusBarCarrierLabel.setOnPreferenceChangeListener(this);

        mStatusBarCarrierLabelCustom.setText(statusBarCarrierLabelCustom);
        mStatusBarCarrierLabelCustom.setOnPreferenceChangeListener(this);
        mStatusBarCarrierLabelCustom.setEnabled(
                statusBarCarrierLabel == 3);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mStatusBarClock) {
            int statusBarClock = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_CLOCK,
                    statusBarClock);
            return true;
        } else if (preference == mStatusBarAmPm) {
            int statusBarAmPm = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_AM_PM,
                    statusBarAmPm);
            return true;
        } else if (preference == mStatusBarCmSignal) {
            int signalStyle = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_CM_SIGNAL_TEXT,
                    signalStyle);
            return true;
        } else if (preference == mStatusBarCmBattery) {
            int statusBarCmBattery = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_CM_BATTERY,
                    statusBarCmBattery);
            mStatusBarCmBatteryColor.setEnabled(statusBarCmBattery == 2);
            mStatusBarCmBatteryLowBatt.setEnabled(statusBarCmBattery == 2);
            return true;
        } else if (preference == mStatusBarCmBatteryColor) {
            String statusBarCmBatteryColor = (String) newValue;
            if ("custom".equals(statusBarCmBatteryColor)) {
                int color = -1;
                String colorString = Settings.System.getString(getContentResolver(),
                        Settings.System.STATUS_BAR_CM_BATTERY_COLOR);
                if (!TextUtils.isEmpty(colorString)) {
                    try {
                        color = Color.parseColor(colorString);
                    } catch (IllegalArgumentException e) {
                    }
                }
                new ColorPickerDialog(this, mColorListener, color).show();
            } else {
                Settings.System.putString(getContentResolver(),
                        Settings.System.STATUS_BAR_CM_BATTERY_COLOR, statusBarCmBatteryColor);
            }
            return true;
        } else if (preference == mStatusBarCarrierLabel) {
            int carrierLabelType = Integer.valueOf((String) newValue);
            mStatusBarCarrierLabelCustom.setEnabled(carrierLabelType == 3);
            Settings.System.putInt(getContentResolver(), Settings.System.CARRIER_LABEL_TYPE,
                    carrierLabelType);
            return true;
        } else if (preference == mStatusBarCarrierLabelCustom) {
            String carrierLabelCustom = String.valueOf(newValue);
            Settings.System.putString(getContentResolver(),
                    Settings.System.CARRIER_LABEL_CUSTOM_STRING,
                    carrierLabelCustom);
            return true;
        }
        return false;
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;

        /* Preference Screens */
        if (preference == mStatusBarCompactCarrier) {
            value = mStatusBarCompactCarrier.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.STATUS_BAR_COMPACT_CARRIER, value ? 1 : 0);
            return true;
        } else if (preference == mStatusBarBrightnessControl) {
            value = mStatusBarBrightnessControl.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.STATUS_BAR_BRIGHTNESS_TOGGLE, value ? 1 : 0);
            return true;
        } else if (preference == mStatusBarHeadset) {
            value = mStatusBarHeadset.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_HEADSET,
                    value ? 1 : 0);
            return true;
        } else if (preference == mHideAlarm) {
            value = mHideAlarm.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.STATUSBAR_HIDE_ALARM,
                    value ? 1 : 0);
            return true;
        } else if (preference == mStatusBarCmBatteryLowBatt) {
            value = mStatusBarCmBatteryLowBatt.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_CM_BATTERY_LOW_BATT,
                    value ? 1 : 0);
            return true;
        }
        return false;
    }

    private OnColorChangedListener mColorListener = new OnColorChangedListener() {
        @Override
        public void colorUpdate(int color) {
            // no-op
        }

        @Override
        public void colorChanged(int color) {
            String colorString = String.format("#%02x%02x%02x", Color.red(color),
                    Color.green(color), Color.blue(color));
            Settings.System.putString(getContentResolver(),
                    Settings.System.STATUS_BAR_CM_BATTERY_COLOR, colorString);
        }
    };
}
