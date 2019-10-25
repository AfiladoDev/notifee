package io.invertase.notifee.core;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

public class NotifeeWorker extends Worker {
  private static String INPUT_KEY_TYPE = "type";
  private static String WORK_TYPE_LICENSE_CHECK = "license_check";
  private static String WORK_TYPE_SCHEDULED_NOTIFICATION = "scheduled_notification";

  public NotifeeWorker(
    @NonNull Context context,
    @NonNull WorkerParameters params
  ) {
    super(context, params);
  }

  static void scheduleLicenseCheckWork(
    ExistingPeriodicWorkPolicy existingPeriodicWorkPolicy
  ) {
    Constraints constraints = new Constraints.Builder()
      .setRequiredNetworkType(NetworkType.CONNECTED)
      .build();

    Data workData = new Data.Builder().putString(INPUT_KEY_TYPE, WORK_TYPE_LICENSE_CHECK).build();

    PeriodicWorkRequest licenseWorkRequest = new PeriodicWorkRequest.Builder(
      NotifeeWorker.class,
      2,
      TimeUnit.DAYS
    ).setInputData(workData).setConstraints(constraints).build();

    WorkManager
      .getInstance(NotifeeContextHolder.getApplicationContext())
      .enqueueUniquePeriodicWork(
        WORK_TYPE_LICENSE_CHECK,
        existingPeriodicWorkPolicy,
        licenseWorkRequest
      );
  }

  static void scheduleNotificationWork() {
    // TODO
  }

  @NonNull
  @Override
  public Result doWork() {
    String workType = getInputData().getString(INPUT_KEY_TYPE);
    // TODO do actual work e.g. check license or show notification
    Log.d("NotifeeWorker", workType);
    Data outputData = new Data.Builder().build();
    return Result.success(outputData);
  }
}
