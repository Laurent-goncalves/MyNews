package com.g.laurent.mynews.Models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    private static callbackAlarm mcallbackAlarm;

    @Override
    public void onReceive(Context context, Intent intent) {

        if(mcallbackAlarm!=null)
            mcallbackAlarm.notification_request_checking();
    }

    public interface callbackAlarm {
        void notification_request_checking();
    }


    public void createCallbackAlarm(callbackAlarm mcallbackAlarm){
        AlarmReceiver.mcallbackAlarm = mcallbackAlarm;
    }
}
