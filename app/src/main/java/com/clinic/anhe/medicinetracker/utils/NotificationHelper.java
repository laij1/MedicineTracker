package com.clinic.anhe.medicinetracker.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.clinic.anhe.medicinetracker.R;

public class NotificationHelper extends ContextWrapper {

    public static final String channelID = "Channel1ID";
    public static final String channelName = "Channel 1";
    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createChannel() {
        NotificationChannel channel1 = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.colorPrimaryDark);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);


        getNotificationManager().createNotificationChannel(channel1);
    }

    public NotificationManager getNotificationManager() {
        if(mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getChannel1Notification(String title, String message){
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setSmallIcon(R.drawable.ic_account)
                .setContentTitle(title)
                .setContentText(message);

    }

}
