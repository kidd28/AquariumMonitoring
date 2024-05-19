package com.capstone.aquariummonitoring;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Implementation of App Widget functionality.
 */
public class MonitoringWidget extends AppWidgetProvider {



    public static final String WIDGET_IDS_KEY ="mywidgetproviderwidgetids";
    public static final String WIDGET_DATA_KEY ="mywidgetproviderwidgetdata";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int[] appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.monitoring_widget);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent, PendingIntent.FLAG_IMMUTABLE
        );

        views.setOnClickPendingIntent(R.id.CurrentTurbidity, pendingIntent);
        views.setOnClickPendingIntent(R.id.Level, pendingIntent);
        views.setOnClickPendingIntent(R.id.Status, pendingIntent);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Info");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String turbidity =  "" + snapshot.child("Turbidity").getValue();

                views.setTextViewText(R.id.CurrentTurbidity, turbidity);


                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.monitoring_widget);
                remoteViews.setTextViewText(R.id.CurrentTurbidity, turbidity);
                Intent updateIntent = new Intent();
                updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                updateIntent.putExtra(MonitoringWidget.WIDGET_IDS_KEY, appWidgetId);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context, 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                remoteViews.setOnClickPendingIntent(R.id.CurrentTurbidity, pendingIntent);
                remoteViews.setOnClickPendingIntent(R.id.Level, pendingIntent);
                remoteViews.setOnClickPendingIntent(R.id.Status, pendingIntent);

                appWidgetManager.updateAppWidget(appWidgetId, views);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Turbidity");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String status = "" + snapshot.child("Status").getValue();

                views.setTextViewText(R.id.Status, status);


                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.monitoring_widget);
                remoteViews.setTextViewText(R.id.Status, status);
                Intent updateIntent = new Intent();
                updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                updateIntent.putExtra(MonitoringWidget.WIDGET_IDS_KEY, appWidgetId);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context, 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                remoteViews.setOnClickPendingIntent(R.id.CurrentTurbidity, pendingIntent);
                remoteViews.setOnClickPendingIntent(R.id.Level, pendingIntent);
                remoteViews.setOnClickPendingIntent(R.id.Status, pendingIntent);

                appWidgetManager.updateAppWidget(appWidgetId, views);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

            updateAppWidget(context, appWidgetManager, appWidgetIds);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(WIDGET_IDS_KEY)) {
            int[] ids = intent.getExtras().getIntArray(WIDGET_IDS_KEY);
            if (intent.hasExtra(WIDGET_DATA_KEY)) {
                Object data = intent.getExtras().getParcelable(WIDGET_DATA_KEY);
                this.updateAppWidget(context, AppWidgetManager.getInstance(context), ids);
            } else {
                this.onUpdate(context, AppWidgetManager.getInstance(context), ids);
            }
        } else super.onReceive(context, intent);
    }


}