package com.example.servicesoverview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.BackoffPolicy;
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

import com.example.servicesoverview.workmanager.BackgroundWorker;
import com.example.servicesoverview.workmanager.ForegroundWorker;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class WorkerActivity extends AppCompatActivity {

    private static final int RC_NOTIFICATION = 99;
    WorkManager workManager = WorkManager.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    RC_NOTIFICATION);
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
                .setRequiredNetworkType(NetworkType.UNMETERED)
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


}