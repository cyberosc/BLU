package com.acktos.blu.receivers;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by Acktos on 7/17/15.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(getClass().getSimpleName(), "Entry onReceive GCM broadcast");
        Log.i(getClass().getSimpleName(), intent.getExtras().toString());

        // find what service will handle the intent.
        Bundle extras=intent.getExtras();



        ComponentName comp = new ComponentName(context.getPackageName(),GcmIntentService.class.getName());

        startWakefulService(context, (intent.setComponent(comp)));
        //setResultCode(Activity.RESULT_OK);



    }
}
