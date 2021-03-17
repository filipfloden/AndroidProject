package se.ju.student.group16.androidproject

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import java.util.*

private const val REQUEST_CODE = 1440

class UpdateMyEventActivity : AppCompatActivity() {

    private val database = firebaseRepository.getDatabaseReference()
    private val eventPath = "event"
    private var myEvent = mutableListOf<Events>()
    private var inviteFriendsList = mutableListOf<User>()
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

        var eventDate = ""

        val thisEvent = intent.getStringExtra("clickedId")
        database.child(eventPath).child(thisEvent!!).get().addOnSuccessListener{
            myEvent
                .add(
                    Events(
                        it.key.toString(),
                        it.child("title").value.toString(),
                        it.child("description").value.toString(),
                        it.child("theme").value.toString(),
                        it.child("date").value.toString(),
                        it.child("latitude").value as Double,
                        it.child("longitude").value as Double,
                        it.child("host").value.toString(),
                        it.child("guest-list").value as Map<User, String>
                    )
                )
            eventTitleTextView.setText(myEvent[0].eventTitle)
            eventThemeTextView.setText(myEvent[0].eventTheme)
            eventDescriptionTextView.setText(myEvent[0].eventDescription)
            eventDate = myEvent[0].eventDate
            pickADateButton.setText(eventDate)
            latitude = myEvent[0].eventLat
            longitude = myEvent[0].eventLong
            for (guest in myEvent[0].guestList){
                inviteFriendsList.add(guest.key)
            }
            Log.d("awshit", myEvent[0].guestList.toString())
            Log.d("hellyeah", inviteFriendsList.toString())

        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
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
            inviteFriendsList = inviteFriendsAdapter.getCheckedFriends()
            inviteFriendsDialog.dismiss()
        }
        updateEventButton.setOnClickListener{
            updateEvent(eventDate)
        }
        deleteEventButton.setOnClickListener {
            AlertDialog.Builder(this)
                    .setTitle(getString(R.string.delete_event))
                    .setMessage(getString(R.string.delete_confirmation))
                    .setPositiveButton(getString(R.string.yes)){
                        dialog, whichButton ->
                        deleteEvent()
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
            longitude = data!!.getDoubleExtra("longitude",0.0)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun updateEvent(eventDate: String){
        val currentUser = firebaseRepository.getCurrentUser()
        val eventTitle = findViewById<EditText>(R.id.event_title).text.toString()
        val eventTheme = findViewById<EditText>(R.id.event_theme).text.toString()
        val eventDescription = findViewById<EditText>(R.id.event_description).text.toString()

        if(eventTheme.isNotEmpty() && eventDescription.isNotEmpty() && eventTitle.isNotEmpty()){
            val eventInfo = mapOf("host" to currentUser?.uid,"title" to eventTitle, "theme" to eventTheme,
                    "description" to eventDescription, "date" to eventDate, "latitude" to latitude, "longitude" to longitude)
            Log.d("funkar","skicka till firebase")
            Log.d("event id",myEvent[0].eventID)
            Log.d("all info",eventInfo.toString())

            database.child(eventPath).child(myEvent[0].eventID).setValue(eventInfo)
            Log.d("all friends",inviteFriendsList.toString())

            for (invitedFriend in inviteFriendsList) {
                Log.d("this friend",invitedFriend.toString())

                database.child(eventPath).child(myEvent[0].eventID).child("guest-list").child(invitedFriend.toString()).setValue("pending")
            }
            Toast.makeText(this,getString(R.string.event_was_updated), Toast.LENGTH_LONG).show()
            finish()
        }else {
            Log.d("funkar inte", "skicka error")
        }
    }

    private fun deleteEvent(){
        database.child(eventPath).child(myEvent[0].eventID).removeValue()
    }
}

