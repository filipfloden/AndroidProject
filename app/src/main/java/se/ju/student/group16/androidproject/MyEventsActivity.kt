package se.ju.student.group16.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.reflect.Type

class MyEventsActivity : AppCompatActivity() {

    private val myEventsList = eventRepository.getAllMyEvents()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_events)
        val myEventsListView = findViewById<ListView>(R.id.myEventsListView)
        val currentUser = firebaseRepository.getCurrentUser()
        val eventAdapter = ArrayAdapter<Events>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                myEventsList
        )
        myEventsListView.adapter = eventAdapter

        myEventsListView.setOnItemClickListener { _, _, position, _ ->
            startActivity(Intent(this, UpdateMyEventActivity::class.java).putExtra("clickedId", myEventsList[position].eventID))
        }
    }
}