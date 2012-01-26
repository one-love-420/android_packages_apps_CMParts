
package com.cyanogenmod.cmparts.services;

import java.io.File;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.cyanogenmod.cmparts.activities.Voltage;
import com.cyanogenmod.cmparts.activities.VoltageControl;
import com.cyanogenmod.cmparts.utils.CMDProcessor;

public class BootService extends Service {

    static final String TAG = "Extended Settings Service";
    private final BootService service = this;
    public static SharedPreferences preferences;
    private Thread bootThread;

    @Override
    public void onStart(Intent intent, int startId) {
        preferences = PreferenceManager.getDefaultSharedPreferences(service);
        super.onStart(intent, startId);
        Log.i(TAG, "Starting set-on-boot");
        bootThread = new Thread() {
            @Override
            public void run() {
                final CMDProcessor cmd = new CMDProcessor();                
                if (preferences.getBoolean(VoltageControl.KEY_APPLY_BOOT, false)) {
                    final List<Voltage> volts = VoltageControl.getVolts(preferences);
                    final StringBuilder sb = new StringBuilder();
                    String logInfo = "Setting Volts: ";
                    for (final Voltage volt : volts) {
                        sb.append(volt.getSavedMV() + " ");
                        logInfo += volt.getFreq() + "=" + volt.getSavedMV() + " ";
                    }
                    Log.i(TAG, logInfo);
                    new CMDProcessor().su.runWaitFor("busybox echo " + sb.toString() + " > "
                            + VoltageControl.MV_TABLE0);
                    if (new File(VoltageControl.MV_TABLE1).exists()) {
                        new CMDProcessor().su.runWaitFor("busybox echo " + sb.toString() + " > "
                                + VoltageControl.MV_TABLE1);
                    }
                }
            }
        };
        bootThread.start();
        // Stop the service
        stopSelf();
    }

    @Override
    public IBinder onBind(final Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}
