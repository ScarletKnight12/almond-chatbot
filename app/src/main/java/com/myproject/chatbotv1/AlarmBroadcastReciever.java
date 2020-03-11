package com.myproject.chatbotv1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.widget.Toast;

public class AlarmBroadcastReciever extends BroadcastReceiver {
        @Override

        public void onReceive(Context context, Intent intent) {

            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
            wl.acquire();

            // Put here YOUR code.
            MediaPlayer mp = MediaPlayer.create(context, R.raw.wake_up);
            mp.start();
            Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example

            wl.release();


        }
    }



