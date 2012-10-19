package com.android.tyeen.rotationswitch;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.provider.Settings;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import android.util.Log;

public class RotationSwitchActivity extends Activity
{
    // Data
    private final String TAG = "RotationSwitchActivity";
    private Notification mNotification;
    private NotificationManager mNotiManager;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Crate notification
        int rotationState = RotationSwitchAppConstant.ROTATION_STATE_ON;
        try {
            if (Settings.System.getInt(this.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION) == 1) {
                rotationState = RotationSwitchAppConstant.ROTATION_STATE_ON;
            } else {
                rotationState = RotationSwitchAppConstant.ROTATION_STATE_OFF;
            }
        } catch(Settings.SettingNotFoundException e) {
            Log.e(TAG, "###  Settings: ACCELEROMETER_ROTATION not found.");
            e.printStackTrace();
        }
        
        mNotification = RotationSwitchAppHelper.generateNotification(this, rotationState);

        mNotiManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);

        Button btnStart = (Button)findViewById(R.id.btn_start_notification);
        btnStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mNotiManager.notify(RotationSwitchAppConstant.ROTATION_SWITCH_NOTI_ID, mNotification);
                Intent intent = new Intent();
                intent.setClass(RotationSwitchActivity.this, RotationSwitchService.class);
                startService(intent);
                Toast toast = Toast.makeText(getApplicationContext(),
                    "Switch is added to status bar", Toast.LENGTH_LONG);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

        Button btnFinish = (Button)findViewById(R.id.btn_finish_notification);
        btnFinish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mNotiManager.cancel(RotationSwitchAppConstant.ROTATION_SWITCH_NOTI_ID);
                Intent intent = new Intent();
                intent.setClass(RotationSwitchActivity.this, RotationSwitchService.class);
                stopService(intent);
                Toast toast = Toast.makeText(getApplicationContext(),
                    "Switch is removed from status bar", Toast.LENGTH_LONG);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }
}
