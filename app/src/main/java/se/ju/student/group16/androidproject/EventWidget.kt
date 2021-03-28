package se.ju.student.group16.androidproject

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
/**
 * Implementation of App Widget functionality.
 */
class EventWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    //val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val database = firebaseRepository.getDatabaseReference()
    val currentUser = firebaseRepository.getCurrentUser()
    lateinit var lastEvent: String
    val views = RemoteViews(context.packageName, R.layout.event_widget)
    //views.setRemoteAdapter(R.id.widget_stackview, Intent(context, WidgetService::class.java))

    database.child("users").child(currentUser?.uid.toString()).child("upcoming-events").get().addOnSuccessListener {
        if (it.hasChildren()) {
            lastEvent = it.children.last().key.toString()
        }else{
            return@addOnSuccessListener
        }
        database.child("event").child(lastEvent).get().addOnSuccessListener { info ->

            views.setTextViewText(R.id.widgetEventTitle,  info.child("title").value.toString())
            views.setTextViewText(R.id.widgetEventTheme,  info.child("theme").value.toString())
            views.setTextViewText(R.id.widgetEventDesc,  info.child("description").value.toString())
            views.setOnClickPendingIntent(R.id.next_event, PendingIntent
                .getActivity(context, 0, Intent(context, LoadDataActivity::class.java)
                    .putExtra("next-activity", "ThisEventActivity")
                    .putExtra("thisEvent", lastEvent), 0))
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}