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

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val users = "users"
    private val friendsPending = "friends-pending"

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.my_event_row, null, true)
        val myEventTitle = rowView.findViewById<TextView>(R.id.my_event_title)
        myEventTitle.text = myEvents[position].eventTitle

        myEventTitle.setOnClickListener {
            //Log.d("Event", eventAdapter.getItem(position).toString())
            context.startActivity(Intent(context, UpdateMyEventActivity::class.java).putExtra("clickedId", myEvents[position].eventID))
        }


        return rowView
        //super.getView(position, convertView, parent)
    }
    /*
    fun filter(text: String?) {
        var tempUsers = userCopy.toMutableList()
        val text = text!!.toLowerCase()
        user.clear()
        if (text.isEmpty()) {
            user.addAll(tempUsers)
        } else {
            for (i in tempUsers) {
                if (i.displayname.toLowerCase().contains(text)) {
                    user.add(i)
                }
            }
        }
        userCopy = tempUsers
        notifyDataSetChanged()
    }
     */
}