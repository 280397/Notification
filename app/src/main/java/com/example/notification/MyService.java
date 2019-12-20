package com.example.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationCompat;

import static android.content.ContentValues.TAG;

public class MyService extends FirebaseMessagingService {


    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private NotificationManager mNotifyManager;
    private static final int NOTIFICATION_ID = 0;

    private static final String ACTION_UPDATE_NOTIFICATION =
            "com.example.android.notifyme.ACTION_UPDATE_NOTIFICATION";

    private NotificationReceiver mReceiver = new NotificationReceiver();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            createNotificationChannel();
            sendNotification();

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.

            } else {
                // Handle message within 10 seconds

            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    public void sendNotification() {
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());

    }

    public void updateNotification() {
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();

        Bitmap androidImage = BitmapFactory
                .decodeResource(getResources(), R.drawable.mascot_1);
        notifyBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(androidImage)
                .setBigContentTitle("Notification Updated!"));

        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());

    }

    public void cancelNotification() {
        mNotifyManager.cancel(NOTIFICATION_ID);
    }

    public void createNotificationChannel() {
        mNotifyManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            // Create a NotificationChannel
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Mascot Notification", NotificationManager
                    .IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder() {
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
//               setelah notif di klik, mengarah ke halaman notifikasi
                .setContentIntent(notificationPendingIntent)
//                hapus notifikasi setelah di klik
                .setAutoCancel(true)
//                menampilkan prioritas notifikasi
                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                mengatur getaran dan nada dering
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentTitle("You've been notified!")
                .setContentText("This is your notification text.")
                .setSmallIcon(R.drawable.notification_icon);

        return notifyBuilder;
    }

//    void setNotificationButtonState(Boolean isNotifyEnabled,
//                                    Boolean isUpdateEnabled,
//                                    Boolean isCancelEnabled) {
//        button_notify.setEnabled(isNotifyEnabled);
//        button_update.setEnabled(isUpdateEnabled);
//        button_cancel.setEnabled(isCancelEnabled);
//    }

    //    lanjutan
    public class NotificationReceiver extends BroadcastReceiver {

        public NotificationReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // Update the notification
            updateNotification();
        }

    }

}
