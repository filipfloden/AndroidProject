package se.ju.student.group16.androidproject.event

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import se.ju.student.group16.androidproject.R
import se.ju.student.group16.androidproject.firebaseRepository

class MyEventsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_events)
        val myEventsListView = findViewById<ListView>(R.id.myEventsListView)
        val currentUser = firebaseRepository.getCurrentUser()
        val eventAdapter = ArrayAdapter<Events>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                eventRepository.getAllMyEvents()
        )
        myEventsListView.adapter = eventAdapter

        myEventsListView.setOnItemClickListener { _, _, position, _ ->
            Log.d("Event", eventAdapter.getItem(position).toString())
            startActivity(Intent(this, UpdateMyEventActivity::class.java).putExtra("clickedId", eventAdapter.getItem(position)?.eventID))
        }
    }
}