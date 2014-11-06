package com.dlucci.widdlewidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;
import android.hardware.Camera.Parameters;
import android.hardware.Camera;
import android.provider.Settings;
import java.lang.System;
/**
 * Created by dlucci on 10/27/14.
 */
public class WiddleWidget extends AppWidgetProvider{

    private static final String TAG = "WiddleWidget";

    private static final String WIFI_ACTION = "com.dlucci.widdlewidget.wifi";
    private static final String AIRPLANE_ACTION = "com.dlucci.widdlewidget.airplane";
    private static final String FLASHLIGHT_ACTION = "com.dlucci.widdlewidget.flashlight";

    private Button wifi, airplane, flashlight;

    private static boolean flashOn = false;

    private static Camera cam;

    @Override public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate");
        for(int i = 0; i < appWidgetIds.length; i++) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widdlewidget_layout);

            Intent btnAction = new Intent(context, this.getClass());
            btnAction.setAction(WIFI_ACTION);

            PendingIntent pi = PendingIntent.getBroadcast(context, 0, btnAction, 0);

            Log.d(TAG, "finishing up onUpdate");
            views.setOnClickPendingIntent(R.id.wifi, pi);
            appWidgetManager.updateAppWidget(appWidgetIds[i], views);

            btnAction.setAction(FLASHLIGHT_ACTION);
            pi = PendingIntent.getBroadcast(context, 0, btnAction, 0);
            views.setOnClickPendingIntent(R.id.light, pi);
            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
            btnAction.setAction(AIRPLANE_ACTION);
            pi = PendingIntent.getBroadcast(context, 0, btnAction, 0);
            views.setOnClickPendingIntent(R.id.airplane, pi);
            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
         }
    }

    @Override public void onReceive(Context context, Intent i){
        Log.d(TAG, "onReceive");
        String action  = i.getAction();
        Log.d(TAG, "Action is " + action);
        if(action.equals(WIFI_ACTION)){
            WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            if(manager.isWifiEnabled())
                manager.setWifiEnabled(false);
            else
                manager.setWifiEnabled(true);
        } else if(action.equals(FLASHLIGHT_ACTION)){

            if(flashOn){
                cam.stopPreview();
                cam.unlock();
                cam.release();
                flashOn = false;
            } else {
                cam = Camera.open();
                Parameters p = cam.getParameters();
                p.setFlashMode(Parameters.FLASH_MODE_TORCH);
                cam.setParameters(p);
                cam.startPreview();
                cam.lock();
                flashOn = true;
            }
        } else if(action.equals(AIRPLANE_ACTION)){
            boolean isEnabled = Settings.System.getInt(
                    context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) == 1;

            Settings.System.putInt(
                    context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, isEnabled ? 0 : 1);

        }

        super.onReceive(context, i);
    }
}
