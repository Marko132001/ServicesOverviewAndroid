package com.example.servicesoverview;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OutOfQuotaPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.servicesoverview.broadcasts.AirPlaneModeReceiver;
import com.example.servicesoverview.broadcasts.TestReceiver;
import com.example.servicesoverview.workmanager.BackgroundWorker;
import com.example.servicesoverview.workmanager.ForegroundWorker;

import java.util.concurrent.TimeUnit;

public class BackgroundTasksActivity extends AppCompatActivity {

    private static final int RC_NOTIFICATION = 99;
    private static final String BROADCAST_ACTION = "TEST_ACTION";
    WorkManager workManager = WorkManager.getInstance(this);
    AirPlaneModeReceiver airPlaneModeReceiver = new AirPlaneModeReceiver();
    TestReceiver testReceiver = new TestReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);

        //Request user permission for displaying long-running worker notifications
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    RC_NOTIFICATION);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(airPlaneModeReceiver);
        unregisterReceiver(testReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(
                airPlaneModeReceiver,
                new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(
                    testReceiver,
                    new IntentFilter(BROADCAST_ACTION),
                    RECEIVER_EXPORTED
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == RC_NOTIFICATION) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "ALLOWED", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void goToServicesActivity(View view) {
        startActivity(
                new Intent(BackgroundTasksActivity.this, ServicesActivity.class)
        );
    }

    /** Work Manager */
    public void executeOneTimeExpeditedWork(View view) {
        WorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(ForegroundWorker.class)
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setInputData(
                        new Data.Builder()
                                .putString("TEST_STRING", "Test string")
                                .build()
                )
                .build();

        workManager.enqueue(uploadWorkRequest);
    }

    public void executePeriodicWorkRequest(View view) {
        PeriodicWorkRequest request =
                new PeriodicWorkRequest.Builder(
                        BackgroundWorker.class,
                        15,
                        TimeUnit.MINUTES
                ).build();

        workManager.enqueueUniquePeriodicWork("PERIODIC_WORK",
                ExistingPeriodicWorkPolicy.KEEP, request
        );
    }

    public void executeWorkWithConstraintsAndDelay(View view) {
        //Required Wi-Fi connection
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.METERED)
                .build();

        WorkRequest request = new OneTimeWorkRequest.Builder(BackgroundWorker.class)
                .setInitialDelay(15, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .build();

        workManager.enqueue(request);
    }

    public void executeUniqueWork(View view) {
        OneTimeWorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(BackgroundWorker.class)
                .setInitialDelay(15, TimeUnit.SECONDS)
                .build();

        workManager.enqueueUniqueWork("UNIQUE_WORK",
                        ExistingWorkPolicy.REPLACE,
                        uploadWorkRequest);

    }

    public void cancelWorkExecution(View view) {
        workManager.cancelAllWork();
    }

    /** Broadcasts */
    public void sendBroadcast(View view) {
        sendBroadcast(new Intent(BROADCAST_ACTION));
    }


}