package se.ju.student.group16.androidproject

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService


class WidgetService: RemoteViewsService.RemoteViewsFactory {


    private val widgetItems = ArrayList<String>()
    private val mCount = 10
    private lateinit var mContext: Context

    /*
    fun WidgetService(context: Context, intent: Intent) {
        mContext = context
        mAppWidgetId = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
    }
    */

    override fun onCreate() {
        for (i in 1..mCount){
            Log.d("Widget", i.toString())
        }
    }

    override fun onDataSetChanged() {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }

    override fun getCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getViewAt(position: Int): RemoteViews {
        TODO("Not yet implemented")
    }

    override fun getLoadingView(): RemoteViews {
        TODO("Not yet implemented")
    }

    override fun getViewTypeCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getItemId(position: Int): Long {
        TODO("Not yet implemented")
    }

    override fun hasStableIds(): Boolean {
        TODO("Not yet implemented")
    }

}