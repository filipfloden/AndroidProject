package se.ju.student.group16.androidproject.event

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import se.ju.student.group16.androidproject.MyEventAdapter
import se.ju.student.group16.androidproject.R
import se.ju.student.group16.androidproject.firebaseRepository

class MyEventsActivity : AppCompatActivity() {

    private lateinit var eventAdapter: MyEventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_events)
        val myEventsListView = findViewById<ListView>(R.id.myEventsListView)
        val noMyEvents = findViewById<TextView>(R.id.no_events_text)
        eventAdapter = MyEventAdapter(
                this,
                eventRepository.getAllMyEvents()
        )
        myEventsListView.adapter = eventAdapter

        if (eventAdapter.isEmpty)
            noMyEvents.text = getString(R.string.no_my_events_text)
        else
            noMyEvents.text = getString(R.string.my_events)

        eventAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        eventAdapter.notifyDataSetChanged()
    }
}