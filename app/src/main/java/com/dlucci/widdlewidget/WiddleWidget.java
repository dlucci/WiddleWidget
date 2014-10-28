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

/**
 * Created by dlucci on 10/27/14.
 */
public class WiddleWidget extends AppWidgetProvider{

    private static final String TAG = "WiddleWidget";

    private static final String WIFI_ACTION = "com.dlucci.widdlewidget.wifi";
    private static final String AIRPLANE_ACTION = "com.dlucci.widdlewidget.airplane";
    private static final String FLASHLIGHT_ACTION = "com.dlucci.widdlewidget.flashlight";

    private Button wifi, airplane, flashlight;

    @Override public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate");

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widdlewidget_layout);

        Intent wifiIntent = new Intent(context, WiddleWidget.class);
        wifiIntent.setAction(WIFI_ACTION);

        PendingIntent pi;
        pi =  PendingIntent.getBroadcast(context, 0 , wifiIntent, 0);

        Log.d(TAG, "finishing up onUpdate");
        views.setOnClickPendingIntent(R.id.wifi, pi);

    }

    @Override public void onReceive(Context context, Intent i){
        Log.d(TAG, "onReceive");
        String action  = i.getAction();
        if(i.equals(WIFI_ACTION)){
            WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            if(manager.isWifiEnabled())
                manager.setWifiEnabled(false);
            else
                manager.setWifiEnabled(true);
        }
    }
}
