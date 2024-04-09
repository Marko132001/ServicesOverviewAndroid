package com.example.servicesoverview;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.servicesoverview.services.BackgroundService;
import com.example.servicesoverview.services.BoundService;
import com.example.servicesoverview.services.ForegroundService;

public class ServicesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**Background Services */
    public void startBackgroundService(View view) {
        Intent intent = new Intent(this, BackgroundService.class);
        startService(intent);
    }

    public void stopBackgroundService(View view) {
        Intent intent = new Intent(this, BackgroundService.class);
        stopService(intent);
    }

    /**Foreground Services */
    public void startForegroundService(View view) {
        Intent intent = new Intent(this, ForegroundService.class);
        startForegroundService(intent);
    }

    public void stopForegroundService(View view) {
        Intent intent = new Intent(this, ForegroundService.class);
        stopService(intent);
    }

    /**Bound Services */
    BoundService boundService;
    boolean isBound = false;

    public void startBoundService(View view) {
        Intent intent = new Intent(this, BoundService.class);
        startService(intent);
    }

    public void stopBoundService(View view) {
        Intent intent = new Intent(this, BoundService.class);
        stopService(intent);
    }

    public void bindToService(View view) {
        if(!isBound) {
            Intent intent = new Intent(this, BoundService.class);
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            Toast.makeText(this, "Bound to service", Toast.LENGTH_SHORT).show();
        }
    }

    public void unbindFromService(View view) {
        if(isBound) {
            unbindService(serviceConnection);
            isBound = false;
            Toast.makeText(this, "Unbound from service", Toast.LENGTH_SHORT).show();
        }
    }

    public void getRandomNumber(View view) {
        if(isBound) {
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(Integer.toString(boundService.getRandom()));
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        //Deliver the IBinder returned by the service's onBind() method
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BoundService.LocalBinder binder = (BoundService.LocalBinder) service;
            boundService = binder.getService();
            isBound = true;
        }

        //Service is killed
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

}