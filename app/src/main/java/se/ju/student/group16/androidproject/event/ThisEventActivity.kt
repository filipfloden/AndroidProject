package se.ju.student.group16.androidproject.event

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import se.ju.student.group16.androidproject.GoogleMapsActivity
import se.ju.student.group16.androidproject.R
import se.ju.student.group16.androidproject.firebaseRepository
import java.util.*


class ThisEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_this_event)
        val eventTitle = findViewById<TextView>(R.id.eventTitle)
        val eventTheme = findViewById<TextView>(R.id.eventTheme)
        val eventDescription = findViewById<TextView>(R.id.eventDescription)
        val eventDate = findViewById<TextView>(R.id.eventDate)
        val eventLocation = findViewById<Button>(R.id.locationButton)
        val inviteAcceptBtn = findViewById<Button>(R.id.inviteAcceptBtn)
        val inviteDeclineBtn = findViewById<Button>(R.id.inviteDeclineBtn)
        val thisEvent = eventRepository.getUpcomingEventById(intent.getStringExtra("thisEvent")!!)
        Log.d("yeye", thisEvent.toString())
        eventTitle.text = thisEvent?.eventTitle
        eventTheme.text = thisEvent?.eventTheme
        eventDescription.text = thisEvent?.eventDescription
        eventDate.text = thisEvent?.eventDate
        eventLocation.setOnClickListener {
            val intent = Intent(this, GoogleMapsActivity::class.java)
            intent.putExtra("long", thisEvent!!.eventLong)
            intent.putExtra("lat", thisEvent.eventLong)
            intent.putExtra("showEvent", true)
            startActivity(intent)
        }
        inviteAcceptBtn.setOnClickListener {
            val database = firebaseRepository.getDatabaseReference()
            val currentUser = firebaseRepository.getCurrentUser()
            database.child("event").child(thisEvent!!.eventID).child("guest-list").child(currentUser!!.uid).setValue("accepted")
            Toast.makeText(this,getString(R.string.event_was_accepted), Toast.LENGTH_LONG).show()
        }
        inviteDeclineBtn.setOnClickListener {
            val database = firebaseRepository.getDatabaseReference()
            val currentUser = firebaseRepository.getCurrentUser()
            database.child("event").child(thisEvent!!.eventID).child("guest-list").child(currentUser!!.uid).setValue("declined")
            Toast.makeText(this,getString(R.string.event_was_declined), Toast.LENGTH_LONG).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, EventActivity::class.java))
        finish()
    }
}