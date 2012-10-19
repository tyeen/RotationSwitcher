package com.android.tyeen.rotationswitch;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class RotationSwitchAppHelper {
    static private int mNotiBarIcon;
    static private int mNotiViewCheckboxIcon;

    static private void updateIconByRotationState(int rotationState) {
        mNotiBarIcon = (rotationState == RotationSwitchAppConstant.ROTATION_STATE_ON) 
            ? R.drawable.ic_noti_bar_rotate_state_on 
            : R.drawable.ic_noti_bar_rotate_state_off;
        mNotiViewCheckboxIcon = (rotationState == RotationSwitchAppConstant.ROTATION_STATE_ON) 
            ? R.drawable.ic_checkbox_checked 
            : R.drawable.ic_checkbox_unchecked;
    }

    static Notification generateNotification(Context context, int rotationState) {
        updateIconByRotationState(rotationState);

        Notification notification = new Notification(mNotiBarIcon, "Switch screen auto-rotation", System.currentTimeMillis());
        notification.flags |= Notification.FLAG_NO_CLEAR;

        // Create remote view.
        RemoteViews notiView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
        notiView.setImageViewResource(R.id.rotator_icon, R.drawable.ic_rotate_noti_item);
        notiView.setTextViewText(R.id.txt_title, "Auto-rotate screen");
        notiView.setImageViewResource(R.id.rotation_check, mNotiViewCheckboxIcon);

        // Create click intent.
        Intent clickIntent = new Intent(RotationSwitchAppConstant.ACTION_ROTATION_CHANGED);
        clickIntent.putExtra(RotationSwitchAppConstant.EXTRA_KEY_NOTIFICATION, notification);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, 0);
        notiView.setOnClickPendingIntent(R.id.rotation_check, clickPendingIntent);

        notification.contentIntent = clickPendingIntent;
        notification.contentView = notiView;

        return notification;
    }
}
