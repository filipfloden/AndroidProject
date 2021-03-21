package se.ju.student.group16.androidproject

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import se.ju.student.group16.androidproject.event.eventRepository
import se.ju.student.group16.androidproject.friend.friendsRepository
import kotlin.random.Random

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
    database.child("users").child(currentUser?.uid.toString()).child("upcoming-events").get().addOnSuccessListener {
        lastEvent = it.children.last().key.toString()
        database.child("event").child(lastEvent).get().addOnSuccessListener { info ->

            views.setTextViewText(R.id.widgetEventTitle,  info.child("title").value.toString())
            views.setTextViewText(R.id.widgetEventTheme,  info.child("theme").value.toString())
            views.setTextViewText(R.id.widgetEventDesc,  info.child("description").value.toString())
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}