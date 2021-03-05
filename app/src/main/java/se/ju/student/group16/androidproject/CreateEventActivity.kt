package se.ju.student.group16.androidproject

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*

class CreateEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
        val dateDialog = Dialog(this)
        dateDialog.setContentView(R.layout.datepopup)
        val inviteFriendsDialog = Dialog(this)
        inviteFriendsDialog.setContentView(R.layout.invite_friends_popup)
        val createEventButton = findViewById<Button>(R.id.eventCreateBtn)
        val dateDoneButton = dateDialog.findViewById<Button>(R.id.date_done)
        val pickADateButton = findViewById<Button>(R.id.pickDateBtn)
        val inviteFriendsButton = findViewById<Button>(R.id.invite_Friends_Btn)
        val inviteFriendsDoneButton = inviteFriendsDialog.findViewById<Button>(R.id.invite_Friends_done)
        val calendarView = dateDialog.findViewById<CalendarView>(R.id.calendarView)
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
            var thisMonth = ""
            if(month<9){
                thisMonth = (month+1).toString()
                thisMonth = "0"+thisMonth
            }else{
                thisMonth = (month+1).toString()
            }
            displayChosenDate.text = "$year-$thisMonth-$dayOfMonth"
            eventDate = "$year-$thisMonth-$dayOfMonth"
        }

    }


    private fun createEvent(){
        val eventTitle = findViewById<EditText>(R.id.event_title)
        val eventTheme = findViewById<EditText>(R.id.event_theme)
        val eventDescription = findViewById<EditText>(R.id.event_description)
        if(eventTheme.text.isNotEmpty() && eventDescription.text.isNotEmpty() && eventTitle.text.isNotEmpty()){
            Log.d("funkar","skicka till firebase")
        }else{
            Log.d("funkar inte", "skicka error")
        }
    }
}