package pl.kowalecki.edug.NotificationsApp;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import pl.kowalecki.edug.Activities.HomeActivity;
import pl.kowalecki.edug.R;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID_before= "startMission";
    public static final String channelID_after = "endMission";
    public static final String channelName_before = "EdugStartMission";
    public static final String channelName_after = "EdugEndMission";

    private NotificationManager mManager;
    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channelBefore = new NotificationChannel(channelID_before, channelName_before, NotificationManager.IMPORTANCE_HIGH);
        NotificationChannel channelAfter = new NotificationChannel(channelID_after, channelName_after, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channelBefore);
        getManager().createNotificationChannel(channelAfter);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotificationBefore(String title, String content) {
        Intent resultIntent = new Intent(this, HomeActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 1 ,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Builder(getApplicationContext(), channelID_before)
                .setContentTitle(title) //Nazwa misji
                .setContentText(content) //Zaproszenie do udziału w misji xd
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);
    }

    public NotificationCompat.Builder getChannelNotificationAfter(String title, String content) {
        Intent resultIntent = new Intent(this, HomeActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 1 ,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Builder(getApplicationContext(), channelID_after)
                .setContentTitle(title) //Nazwa misji
                .setContentText(content) //Zaproszenie do udziału w misji xd
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);
    }
}
