package se.ju.student.group16.androidproject

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment

private const val REQUEST_CODE = 1337
private var eventLocation = ""

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
            createEvent()
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
            val location:String = data?.getStringExtra("eventLocation").toString()
            eventLocation = location


        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun createEvent(){
        val eventTitle = findViewById<EditText>(R.id.event_title)
        val eventTheme = findViewById<EditText>(R.id.event_theme)
        val eventDescription = findViewById<EditText>(R.id.event_description)
        Log.d("koordinat i create", eventLocation)
        if(eventTheme.text.isNotEmpty() && eventDescription.text.isNotEmpty() && eventTitle.text.isNotEmpty()){
            Log.d("funkar","skicka till firebase")
        }else{
            Log.d("funkar inte", "skicka error")
        }
    }
}