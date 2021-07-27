package pl.kowalecki.edug.NotificationsApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import pl.kowalecki.edug.Activities.HomeActivity;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String titleNotif = intent.getStringExtra("NOTIF_TITLE");
        String contentNotif = intent.getStringExtra("NOTIF_CONTENT");
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(titleNotif, contentNotif);
        notificationHelper.getManager().notify(1, nb.build());
    }


}
