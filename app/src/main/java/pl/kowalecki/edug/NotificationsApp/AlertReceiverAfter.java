package pl.kowalecki.edug.NotificationsApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlertReceiverAfter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        String titleNotifAfter = intent.getStringExtra("NOTIF_TITLE_AFTER");
        String contentNotifAfter = intent.getStringExtra("NOTIF_CONTENT_AFTER");
        NotificationCompat.Builder notificationAfter = notificationHelper.getChannelNotificationAfter(titleNotifAfter, contentNotifAfter);
        notificationHelper.getManager().notify(2, notificationAfter.build());
    }


}
