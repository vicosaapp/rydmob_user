package com.general.files;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.ViewGroup;

import com.sessentaservices.usuarios.R;
import com.sessentaservices.usuarios.RideDeliveryActivity;
import com.sessentaservices.usuarios.UberXHomeActivity;

import java.util.List;

/**
 * Created by Admin on 23-11-2016.
 */
public class GpsReceiver extends BroadcastReceiver {
    Context context;
    MyApp mApplication;


    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        mApplication = ((MyApp) context.getApplicationContext());
        checkGps(context);


    }

    public void checkGps(Context context) {
        /*GeneralFunctions generalFunc = MyApp.getInstance().getGeneralFun(context);
        boolean foregroud = false;
        try {
            foregroud = new ForegroundCheckTask().execute(context).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (foregroud == true && generalFunc.isLocationEnabled() == false && isApplicationBroughtToBackground() == false) {
            restartApp();
        }
        if (foregroud == true && generalFunc.isLocationEnabled() == true && isApplicationBroughtToBackground() == false) {
            restartApp();
        }*/

        checkGPSSettings();
    }

    private void checkGPSSettings() {
        Activity currentActivity = MyApp.getInstance().getCurrentAct();

        if (currentActivity != null) {
            if (currentActivity instanceof UberXHomeActivity || MyApp.getInstance().isCurrentActByConfigView()) {

                ViewGroup viewGroup;
                if (currentActivity instanceof UberXHomeActivity) {
                    viewGroup = currentActivity.findViewById(R.id.MainArea);

                } else if (currentActivity instanceof RideDeliveryActivity) {
                    viewGroup = currentActivity.findViewById(R.id.MainLayout);

                } else {
                    viewGroup = currentActivity.findViewById(android.R.id.content);
                }
                handleGPSView(currentActivity, viewGroup);
            }
        }
    }

    private void handleGPSView(Activity activity, ViewGroup viewGroup) {
        try {
            OpenNoLocationView.getInstance(activity, viewGroup).configView(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void restartApp() {
        /*Activity currentActivity = mApplication.getCurrentActivity();
        if (currentActivity != null && currentActivity.getLocalClassName().equalsIgnoreCase("MainActivity")) {
            if (!MyApp.getInstance().getGeneralFun(currentActivity).isLocationEnabled()) {

                if (((MainActivity) currentActivity).noloactionview != null) {
                    ((MainActivity) currentActivity).setNoGpsViewEnableOrDisabled(true);
                }
            } else {
                if (((MainActivity) currentActivity).noloactionview != null) {
                    ((MainActivity) currentActivity).setNoGpsViewEnableOrDisabled(false);
                }
            }

        }*/
    }

    private boolean isApplicationBroughtToBackground() {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
            if (!tasks.isEmpty()) {
                ComponentName topActivity = tasks.get(0).topActivity;
                if (!topActivity.getPackageName().equals(context.getPackageName())) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }


    class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Context... params) {
            final Context context = params[0].getApplicationContext();
            return isAppOnForeground(context);
        }

        private boolean isAppOnForeground(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            final String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }
    }

}
