package se.ju.student.group16.androidproject

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView

class CreateEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
        val dateDialog = Dialog(this)
        dateDialog.setContentView(R.layout.datepopup)
        val inviteFrinedsDialog = Dialog(this)
        inviteFrinedsDialog.setContentView(R.layout.invite_friends_popup)
        val createEventButton = findViewById<Button>(R.id.eventCreateBtn)
        val dateDoneButton = dateDialog.findViewById<Button>(R.id.date_done)
        val pickADateButton = findViewById<Button>(R.id.pickDateBtn)
        val inviteFrinedsButton = findViewById<Button>(R.id.invite_Friends_Btn)
        val inviteFriendsDoneButton = inviteFrinedsDialog.findViewById<Button>(R.id.invite_Friends_done)
        var eventDate = null

        pickADateButton.setOnClickListener{
            dateDialog.show()
        }
        dateDoneButton.setOnClickListener{
            dateDialog.dismiss()
        }
        inviteFrinedsButton.setOnClickListener{
            inviteFrinedsDialog.show()
        }
        inviteFriendsDoneButton.setOnClickListener{
            inviteFrinedsDialog.dismiss()
        }
        createEventButton.setOnClickListener{
            createEvent()
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