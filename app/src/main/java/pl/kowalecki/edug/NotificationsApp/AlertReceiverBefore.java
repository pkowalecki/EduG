package pl.kowalecki.edug.NotificationsApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import pl.kowalecki.edug.Activities.HomeActivity;

public class AlertReceiverBefore extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);

        String titleNotifBefore = intent.getStringExtra("NOTIF_TITLE_BEFORE");
        String contentNotifBefore = intent.getStringExtra("NOTIF_CONTENT_BEFORE");
        NotificationCompat.Builder notificationBefore = notificationHelper.getChannelNotificationBefore(titleNotifBefore, contentNotifBefore);
        notificationHelper.getManager().notify(1, notificationBefore.build());
    }


}
