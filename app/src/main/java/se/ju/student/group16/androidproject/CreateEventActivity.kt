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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

private const val REQUEST_CODE = 1337
private var latitude = 0.0
private var longitude = 0.0

class CreateEventActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val users = "users"
    private val friends = "friends"
    private val displayname = "displayname"
    private val email = "email"
    private var inviteFriendsList = mutableListOf<User>()

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
        val inviteFriendsListView = inviteFriendsDialog.findViewById<ListView>(R.id.inviteFriendsListView)
        val friendsList = mutableListOf<User>()
        val inviteFriendsAdapter = InviteFriendsAdapter(this, friendsList)
        inviteFriendsListView.adapter = inviteFriendsAdapter
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        val currentUser = auth.currentUser

        pickADateButton.setOnClickListener{
            dateDialog.show()
        }
        dateDoneButton.setOnClickListener{
            dateDialog.dismiss()
        }

        database.child(users).child(currentUser?.uid.toString()).child(friends).get().addOnSuccessListener {
            friendsList.clear()
            for (friend in it.children){
                database.child(users).child(friend.key.toString()).get().addOnSuccessListener { info ->
                    val friendDisplayName = info.child(displayname).value
                    val friendEmail = info.child(email).value
                    friendsList.add(User(info.key.toString(), friendDisplayName.toString(), friendEmail.toString()))
                    inviteFriendsAdapter.notifyDataSetChanged()
                }
            }
        }
        inviteFriendsButton.setOnClickListener{
            inviteFriendsDialog.show()
        }
        inviteFriendsDoneButton.setOnClickListener{
            inviteFriendsList = inviteFriendsAdapter.getCheckedFriends()
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
            pickADateButton.setText(eventDate)
        }

        googleMapsButton.setOnClickListener{
            val intent = Intent(this, GoogleMapsActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            latitude = data!!.getDoubleExtra("latitude",0.0)
            longitude = data!!.getDoubleExtra("longitude",0.0)

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun createEvent(eventDate: String) {
        val currentUser = auth.currentUser!!.uid
        val eventTitle = findViewById<EditText>(R.id.event_title).text.toString()
        val eventTheme = findViewById<EditText>(R.id.event_theme).text.toString()
        val eventDescription = findViewById<EditText>(R.id.event_description).text.toString()
        if(eventTheme.isNotEmpty() && eventDescription.isNotEmpty() && eventTitle.isNotEmpty()){
            val eventInfo = mapOf("host" to currentUser,"title" to eventTitle, "theme" to eventTheme,
                    "description" to eventDescription, "date" to eventDate, "latitude" to latitude, "longitude" to longitude)
            Log.d("funkar","skicka till firebase")
            val eventID = database.child("event").push().key
            database.child("event").child(eventID.toString()).setValue(eventInfo)
            database.child(users).child(currentUser).child("my-events").child(eventID.toString()).setValue(true)
            Toast.makeText(this,getString(R.string.event_was_created), Toast.LENGTH_LONG).show()
        }else{
            Log.d("funkar inte", "skicka error")
        }
    }
}