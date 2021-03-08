package se.ju.student.group16.androidproject

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*

private const val REQUEST_CODE = 1337
private var eventLocation = emptyMap<String,Double?>()

class CreateEventActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
        val dateDialog = Dialog(this)
        dateDialog.setContentView(R.layout.datepopup)
        val inviteFriendsDialog = Dialog(this)
        inviteFriendsDialog.setContentView(R.layout.invite_friends_popup)
        val googleMapDialog = Dialog(this)
        googleMapDialog.setContentView(R.layout.activity_google_maps)
        val createEventButton = findViewById<Button>(R.id.eventCreateBtn)
        val dateDoneButton = dateDialog.findViewById<Button>(R.id.date_done)
        val pickADateButton = findViewById<Button>(R.id.pickDateBtn)
        val inviteFriendsButton = findViewById<Button>(R.id.invite_Friends_Btn)
        val inviteFriendsDoneButton = inviteFriendsDialog.findViewById<Button>(R.id.invite_Friends_done)
        val calendarView = dateDialog.findViewById<CalendarView>(R.id.calendarView)
        val googleMapsButton = findViewById<Button>(R.id.googleMapsBtn)
        var eventDate = ""

        pickADateButton.setOnClickListener{
            dateDialog.show()
        }
        dateDoneButton.setOnClickListener{
            Log.d(eventDate,"vibe")
            dateDialog.dismiss()
        }
        inviteFriendsButton.setOnClickListener{
            inviteFriendsDialog.show()
        }
        inviteFriendsDoneButton.setOnClickListener{
            inviteFriendsDialog.dismiss()
        }
        createEventButton.setOnClickListener{
            createEvent(eventDate)
        }
        calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            val displayChosenDate = dateDialog.findViewById<TextView>(R.id.chosenDateTextView)
            var selectedMonth = ""
            var selectedDay = ""
            if (dayOfMonth < 10){
                selectedDay = "0"+dayOfMonth.toString()
            }else{
                selectedDay = dayOfMonth.toString()
            }
            if(month<9){
                selectedMonth = (month+1).toString()
                selectedMonth = "0"+selectedMonth
            }else{
                selectedMonth = (month+1).toString()
            }
            displayChosenDate.text = "$year-$selectedMonth-$selectedDay"
            eventDate = "$year-$selectedMonth-$selectedDay"
        }

        googleMapsButton.setOnClickListener{
            //googleMapDialog.show()
            val intent = Intent(this, GoogleMapsActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)

        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val latitude = data?.getDoubleExtra("latitude",0.0)
            val longitude = data?.getDoubleExtra("longitude",0.0)
            Log.d("latitude", latitude.toString())
            Log.d("longitude", longitude.toString())
            eventLocation = mapOf("lat" to latitude, "lng" to longitude)
            
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun createEvent(eventDate: String) {
        val eventTitle = findViewById<EditText>(R.id.event_title)
        val eventTheme = findViewById<EditText>(R.id.event_theme)
        val eventDescription = findViewById<EditText>(R.id.event_description)
        Log.d("koordinat i create", eventLocation.toString())
        if(eventTheme.text.isNotEmpty() && eventDescription.text.isNotEmpty() && eventTitle.text.isNotEmpty()){
            val eventInfo = mapOf("title" to eventTitle, "theme" to eventTheme,
                "Description" to eventDescription, "date" to eventDate, "location" to eventLocation)
            Log.d("funkar","skicka till firebase")
        }else{
            Log.d("funkar inte", "skicka error")
        }
    }
}