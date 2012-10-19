package com.android.tyeen.rotationswitch;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.app.PendingIntent;
import android.widget.RemoteViews;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

public class RotationSwitchReceiver extends BroadcastReceiver {
    private String TAG = "RotationSwitchReceiver";

    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(RotationSwitchAppConstant.EXTRA_KEY_NOTIFICATION)) {
            int rotationState = RotationSwitchAppConstant.ROTATION_STATE_ON;
            try {
                if (Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION) == 1) {
                    Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
                    rotationState = RotationSwitchAppConstant.ROTATION_STATE_OFF;
                } else {
                    Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);
                    rotationState = RotationSwitchAppConstant.ROTATION_STATE_ON;
                }
            } catch (Settings.SettingNotFoundException e) {
                Log.e(TAG, "###  Settings: ACCELEROMETER_ROTATION not found.");
                e.printStackTrace();
            }

            // Update notification.
            Notification notification = RotationSwitchAppHelper.generateNotification(context, rotationState);

            // Call NotificationManager.notify() to refresh the notification.
            NotificationManager notiManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notiManager.notify(RotationSwitchAppConstant.ROTATION_SWITCH_NOTI_ID, notification);
        }
    }
}
