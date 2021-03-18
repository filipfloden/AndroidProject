package se.ju.student.group16.androidproject.event

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import se.ju.student.group16.androidproject.*
import se.ju.student.group16.androidproject.friend.InviteFriendsAdapter
import se.ju.student.group16.androidproject.friend.friendsRepository

private const val REQUEST_CODE = 1440

class UpdateMyEventActivity : AppCompatActivity() {

    private val database = firebaseRepository.getDatabaseReference()
    private val eventPath = "event"
    private var myEvent = mutableListOf<Events>()
    private var inviteFriendsList = mutableMapOf<String, String>()
    private var latitude = 0.0
    private var longitude = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_my_event)
        val dateDialog = Dialog(this)
        dateDialog.setContentView(R.layout.datepopup)
        val inviteFriendsDialog = Dialog(this)
        inviteFriendsDialog.setContentView(R.layout.invite_friends_popup)
        val googleMapDialog = Dialog(this)
        googleMapDialog.setContentView(R.layout.activity_google_maps)
        val eventTitleTextView = findViewById<TextView>(R.id.event_title)
        val eventThemeTextView = findViewById<TextView>(R.id.event_theme)
        val eventDescriptionTextView = findViewById<TextView>(R.id.event_description)
        val updateEventButton = findViewById<Button>(R.id.updateEventBtn)
        val deleteEventButton = findViewById<Button>(R.id.deleteEventBtn)
        val dateDoneButton = dateDialog.findViewById<Button>(R.id.date_done)
        val pickADateButton = findViewById<Button>(R.id.pickDateBtn)
        val inviteFriendsButton = findViewById<Button>(R.id.invite_Friends_Btn)
        val inviteFriendsDoneButton = inviteFriendsDialog.findViewById<Button>(R.id.invite_Friends_done)
        val calendarView = dateDialog.findViewById<CalendarView>(R.id.calendarView)
        val googleMapsButton = findViewById<Button>(R.id.googleMapsBtn)
        val inviteFriendsListView = inviteFriendsDialog.findViewById<ListView>(R.id.inviteFriendsListView)
        val friendsList = friendsRepository.getAllFriends()
        val inviteFriendsAdapter = InviteFriendsAdapter(this, friendsList)
        inviteFriendsListView.adapter = inviteFriendsAdapter

        var eventDate: String

        val thisEvent = eventRepository.getMyEventById(intent.getStringExtra("clickedId").toString())
        eventTitleTextView.text = thisEvent!!.eventTitle
        eventThemeTextView.text = thisEvent.eventTheme
        eventDescriptionTextView.text = thisEvent.eventDescription
        eventDate = thisEvent.eventDate
        pickADateButton.text = eventDate
        latitude = thisEvent.eventLat
        longitude = thisEvent.eventLong
        for (guest in thisEvent.guestList){
            inviteFriendsList[guest.key] = guest.value
        }

        pickADateButton.setOnClickListener{
            dateDialog.show()
        }
        dateDoneButton.setOnClickListener{
            dateDialog.dismiss()
        }
        inviteFriendsButton.setOnClickListener{
            inviteFriendsDialog.show()
        }
        inviteFriendsDoneButton.setOnClickListener{
            val invitedFriend = inviteFriendsAdapter.getCheckedFriends()
            for (friend in invitedFriend){
                if(inviteFriendsList.containsKey(friend.toString())){
                    break
                }else{
                    inviteFriendsList[friend.uid] = "pending"
                }
            }
            Log.d("list", inviteFriendsList.toString())
            inviteFriendsDialog.dismiss()
        }
        updateEventButton.setOnClickListener{
            updateEvent(thisEvent, eventDate)
        }
        deleteEventButton.setOnClickListener {
            AlertDialog.Builder(this)
                    .setTitle(getString(R.string.delete_event))
                    .setMessage(getString(R.string.delete_confirmation))
                    .setPositiveButton(getString(R.string.yes)){
                        dialog, whichButton ->
                        eventRepository.deleteMyEventById(thisEvent.eventID)
                        Toast.makeText(this,getString(R.string.event_was_deleted), Toast.LENGTH_LONG).show()
                        finish()
                    }.setNegativeButton(getString(R.string.no)) { dialog, whichButtin ->
                        //Don't delete it
                    }.show()
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
            val shortYear = year-2000
            displayChosenDate.text = "$year-$selectedMonth-$selectedDay"
            eventDate = "$year-$selectedMonth-$selectedDay"
            pickADateButton.text = "$shortYear-$selectedMonth-$selectedDay"
        }

        googleMapsButton.setOnClickListener{
            val intent = Intent(this, GoogleMapsActivity::class.java)
            if (longitude == 0.0 &&latitude == 0.0){
                startActivityForResult(intent, REQUEST_CODE)
            }else{
                intent.putExtra("long", longitude)
                intent.putExtra("lat", latitude)
                startActivityForResult(intent, REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            latitude = data!!.getDoubleExtra("latitude",0.0)
            longitude = data.getDoubleExtra("longitude",0.0)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun updateEvent(thisEvent: Events, eventDate: String) {

        val currentUser = firebaseRepository.getCurrentUser()
        val eventTitle = findViewById<EditText>(R.id.event_title).text.toString()
        val eventTheme = findViewById<EditText>(R.id.event_theme).text.toString()
        val eventDescription = findViewById<EditText>(R.id.event_description).text.toString()
        if (eventTheme.isNotEmpty() && eventDescription.isNotEmpty() && eventTitle.isNotEmpty()
                && eventDate.isNotEmpty() && !longitude.isNaN() && !latitude.isNaN() && inviteFriendsList.isNotEmpty()) {
            val eventInfo = mapOf("host" to currentUser?.uid, "title" to eventTitle, "theme" to eventTheme,
                    "description" to eventDescription, "date" to eventDate, "latitude" to latitude,
                    "longitude" to longitude, "guest-list" to inviteFriendsList)
            Log.d("en till", eventInfo.toString())
            eventRepository.updateMyEventById(thisEvent.eventID, eventTitle, eventDescription, eventTheme,
                    eventDate, longitude, latitude, inviteFriendsList)
            database.child(eventPath).child(thisEvent.eventID).setValue(eventInfo)
            Toast.makeText(this, getString(R.string.event_was_updated), Toast.LENGTH_LONG).show()
            inviteFriendsList[currentUser?.uid.toString()] = "pending"
            for (invite in inviteFriendsList){
                database.child("users").child(invite.key).child("upcoming-events").child(thisEvent.eventID).setValue(false)
                database.child("users").child(invite.key).child("upcoming-events").child(thisEvent.eventID).setValue(true)
            }
            startActivity(Intent(this, EventActivity::class.java))
            finish()
        } else {
            Toast.makeText(this,getString(R.string.fill_all_fields), Toast.LENGTH_LONG).show()
        }
    }
}

