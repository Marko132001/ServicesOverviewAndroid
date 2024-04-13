package com.example.servicesoverview.workmanager;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class BackgroundWorker extends Worker {

    public BackgroundWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params
    ) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.d("BACKGROUND_WORKER", "Doing work...");

        return Result.success();
    }
}
