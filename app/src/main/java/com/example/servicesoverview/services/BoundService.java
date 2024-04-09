package com.example.servicesoverview.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Random;

public class BoundService extends Service {
    // Binder given to clients.
    private final IBinder iBinder = new LocalBinder();
    private final Random mGenerator = new Random();
    //Class used for the client Binder
    public class LocalBinder extends Binder {
        public BoundService getService() {
            // Return this instance of LocalService so clients can call public methods.
            return BoundService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Starting", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    //Method for clients
    public int getRandom() {
        return mGenerator.nextInt(200);
    }
}