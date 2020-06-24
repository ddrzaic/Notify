package com.example.manjaro.notify;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ForegroundService extends Service {
    ArrayList<String> alList = new ArrayList<>();

    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";

    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";

    private Intent intent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null)
        {
            String action = intent.getAction();

            switch (action)
            {
                case ACTION_START_FOREGROUND_SERVICE:
                    alList = (ArrayList<String>) intent.getExtras().get("lista");
                    startForegroundService();
                    break;
                case ACTION_STOP_FOREGROUND_SERVICE:
                    stopForegroundService();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startForegroundService() {
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,"Notify")
                .setSmallIcon(R.drawable.ic_notify)
                .setContentTitle("Notify")
                .setContentText(alList.get(alList.size()-1))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                // Set the intent that will fire when the user taps the notification
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(alList.get(alList.size()-1)))
                .setAutoCancel(false)
                .setVibrate(null)
                .setOngoing(true)

                ;


        Notification notification = mBuilder.build();
        startForeground(1, notification);
    }

    private void stopForegroundService()
    {
        // Stop foreground service and remove the notification.
        stopForeground(true);

        // Stop the foreground service.
        stopSelf();
    }
    public int createID(){
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss",  Locale.US).format(now));
        return id;
    }
}
