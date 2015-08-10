package com.acktos.blu.receivers;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.acktos.blu.R;
import com.acktos.blu.controller.BaseController;
import com.acktos.blu.presentation.ServiceListActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by Acktos on 7/17/15.
 */
public class GcmIntentService extends IntentService {

    private GoogleCloudMessaging gcm;
    private NotificationManager mNotificationManager;

    //ATTRIBUTES
    private Intent intent;
    private String message;

    public static final int NOTICATION_NEW_SERVICE_ID=45;
    private final static int MAX_VOLUME = 100;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        gcm = GoogleCloudMessaging.getInstance(this);

    }

    @Override
    public void onHandleIntent(Intent intent) {

        Log.i(BaseController.TAG, "Entry to onHandleIntent");
        this.intent=intent;
        Bundle extras = intent.getExtras();
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        Log.i("extras", extras.toString());
        String messageType = gcm.getMessageType(intent);

        Log.i(BaseController.TAG, "message type:" + messageType);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
            * Filter messages based on message type. Since it is likely that GCM
            * will be extended in the future with new message types, just ignore
            * any message types you're not interested in, or that you don't
            * recognize.
            */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                message="Message type send error: " + extras.toString();

            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {

                message="Deleted messages on server: " +extras.toString();

                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

               sendNotification();
                playSound();
            }
        }else{

            message="Extras information not found";
        }

        Log.i(BaseController.TAG, "message:" + message);
        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }


    private void sendNotification(){

        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, ServiceListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_map_tracker)
                        .setContentTitle("Tienes un nuevo servicio.")
                        .setAutoCancel(true)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Te han asignado un servicio nuevo"))
                        .setContentText("Te han asignado un servicio nuevo");

        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(NOTICATION_NEW_SERVICE_ID, mBuilder.build());
        Log.i(BaseController.TAG, "sound play");


    }

    private void playSound(){

        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.alarmarutaplus);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        final float volume = (float) (1 - (Math.log(MAX_VOLUME - 90) / Math.log(MAX_VOLUME)));
        mediaPlayer.setVolume(volume, volume);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer arg0) {
                mediaPlayer.start();

            }
        });
    }

}
