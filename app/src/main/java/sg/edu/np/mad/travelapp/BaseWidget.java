package sg.edu.np.mad.travelapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class BaseWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            //Go to Main Activity when Widget is clicked
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            //Start pending intent when logo is clicked on
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.base_widget);
            views.setOnClickPendingIntent(R.id.widgetLogoImageView, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views); //Update all instances of the same widget
        }
    }
}