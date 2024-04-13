package com.example.servicesoverview.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Objects;

public class TestReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Objects.equals(intent.getAction(), "TEST_ACTION")) {
            Toast.makeText(context, "Received test intent!", Toast.LENGTH_SHORT).show();
            Log.d("TEST RECEIVER", "Received test intent!");
        }
    }
}
