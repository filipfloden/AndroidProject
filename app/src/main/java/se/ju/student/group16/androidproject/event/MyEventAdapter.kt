package se.ju.student.group16.androidproject

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import se.ju.student.group16.androidproject.event.Events
import se.ju.student.group16.androidproject.event.UpdateMyEventActivity
import java.util.*

class MyEventAdapter(private val context: Activity, private val myEvents: MutableList<Events>) : ArrayAdapter<Events>(context, R.layout.my_event_row, myEvents) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.my_event_row, null, true)
        val myEventTitle = rowView.findViewById<TextView>(R.id.my_event_title)
        myEventTitle.text = myEvents[position].eventTitle

        rowView.setOnClickListener {
            context.startActivity(Intent(context, UpdateMyEventActivity::class.java).putExtra("clickedId", myEvents[position].eventID))
        }


        return rowView
        //super.getView(position, convertView, parent)
    }
}