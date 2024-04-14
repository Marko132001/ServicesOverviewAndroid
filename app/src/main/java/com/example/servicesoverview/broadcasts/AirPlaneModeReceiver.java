package com.example.servicesoverview.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.util.Objects;

public class AirPlaneModeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Objects.equals(intent.getAction(), Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
            boolean isTurnedOn = false;
            try {
                isTurnedOn = Settings.Global.getInt(
                        context.getContentResolver(),
                        Settings.Global.AIRPLANE_MODE_ON
                ) != 0;
            } catch (Settings.SettingNotFoundException e) {
                throw new RuntimeException(e);
            }
            Toast.makeText(context, "Airplane mode enabled? " + isTurnedOn,
                    Toast.LENGTH_SHORT).show();
            Log.d("AIRPLANE MODE", "Airplane mode enabled? " + isTurnedOn);
        }
    }
}
