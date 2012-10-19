package com.android.tyeen.rotationswitch;

import android.app.Service;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

public class RotationSwitchService extends Service {
    // data
    private String TAG="RotationSwitchService";
    private IBinder mBinder = new RotationSwitchServiceBinder();
    private Uri mObservedUri = Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION);
    private RotationSettingObserver mRotationObserver = new RotationSettingObserver(this, new Handler());

    @Override
    public void onCreate() {
        //Log.d(TAG, "###  [onCreate()] called.");
        // Register a content observer to observe the change of ROTATION.
        if (mObservedUri == null)
            mObservedUri = Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION);

        this.getContentResolver().registerContentObserver(mObservedUri, false, mRotationObserver);
    }   

    @Override
    public void onDestroy() {
        //Log.d(TAG, "###  [onDestroy()] called.");
        this.getContentResolver().unregisterContentObserver(mRotationObserver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.d(TAG, "###  [onStartCommand()] intent=" + intent + " flags=" + flags + " startId=" + startId);
        // To continue running this service until it is explicitly stopped,
        // return STICKY
        return START_STICKY;
    }

    // Our ContentObserver
    // To observe the changes of Settings.System.ACCELEROMETER_ROTATION
    class RotationSettingObserver extends ContentObserver {
        private Context mContext;
        public RotationSettingObserver(Context context, Handler handler) {
            super(handler);
            mContext = context;
        }
        @Override
        public void onChange(boolean selfChanged) {
            int rotState = RotationSwitchAppConstant.ROTATION_STATE_ON;
            try {
                if (Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION) == 1) {
                    rotState = RotationSwitchAppConstant.ROTATION_STATE_ON;
                } else {
                    rotState = RotationSwitchAppConstant.ROTATION_STATE_OFF;
                }
            } catch (Settings.SettingNotFoundException e) {
                Log.e(TAG, "###  Settings: ACCELEROMETER_ROTATION not found.");
                e.printStackTrace();
            }
            Notification notification = RotationSwitchAppHelper.generateNotification(mContext, rotState);
            NotificationManager notiManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notiManager.notify(RotationSwitchAppConstant.ROTATION_SWITCH_NOTI_ID, notification);
        }
    }

    // -------------------------------
    // Something that we dont care...
    // -------------------------------
    private class RotationSwitchServiceBinder extends Binder {
        RotationSwitchService getService() {
            return RotationSwitchService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
