package com.example.servicesoverview.workmanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.ForegroundInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.servicesoverview.R;

import java.util.concurrent.TimeUnit;

public class ForegroundWorker extends Worker {

    public ForegroundWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params
    ) {
        super(context, params);
    }

    @NonNull
    @Override
    public ForegroundInfo getForegroundInfo() {
        return getForegroundInfo(getApplicationContext());
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            setForegroundAsync(getForegroundInfo(getApplicationContext()));
            TimeUnit.SECONDS.sleep(10);

            String testString = getInputData().getString("TEST_STRING");
            if(testString == null) {
                return Result.failure();
            }

            Log.d("FOREGROUND_WORKER", "Returned text: " + testString + "...");

        } catch (InterruptedException e) {
            return Result.failure();
        }

        return Result.success();
    }

    private ForegroundInfo getForegroundInfo(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new ForegroundInfo(
                    1,
                    createNotification(context),
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            );
        }

        return new ForegroundInfo(
                1,
                createNotification(context)
        );
    }

    private Notification createNotification(Context context) {
        final String CHANNELID = "Worker Foreground Service ID";
        NotificationChannel channel = new NotificationChannel(
                CHANNELID,
                CHANNELID,
                NotificationManager.IMPORTANCE_LOW
        );

        context.getSystemService(NotificationManager.class)
                .createNotificationChannel(channel);

        Notification notification = new NotificationCompat.Builder(context, CHANNELID)
                .setContentText("Worker is running")
                .setContentTitle("Worker initiated")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOngoing(true)
                .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
                .build();

        NotificationManagerCompat.from(context);

        return notification;
    }
}
