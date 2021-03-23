package se.ju.student.group16.androidproject.event

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import se.ju.student.group16.androidproject.R
import se.ju.student.group16.androidproject.firebaseRepository
import se.ju.student.group16.androidproject.friend.chatActivity

class EventAdapter(private val context: Activity, private val events: MutableList<Events>) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val eventTitle: TextView = view.findViewById(R.id.eventTitleTextView)
        val eventTheme: TextView = view.findViewById(R.id.eventThemeTextView)
        val eventDescription: TextView = view.findViewById(R.id.eventDescTextView)
        val attendanceStatus: TextView = view.findViewById(R.id.attendance_status)
        val readMore: TextView = view.findViewById(R.id.eventInfoBtn)

        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.upcoming_event, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        viewHolder.eventTitle.text = events[position].toString()
        viewHolder.eventTheme.text = events[position].eventTheme
        viewHolder.eventDescription.text = events[position].eventDescription
        if (events[position].eventHost == firebaseRepository.getCurrentUser()?.uid)
            viewHolder.attendanceStatus.text = "Host"
        else
            viewHolder.attendanceStatus.text = events[position].guestList[firebaseRepository.getCurrentUser()?.uid]
        Log.d("Status", events[position].guestList[firebaseRepository.getCurrentUser()?.uid].toString())

        viewHolder.readMore.setOnClickListener {
            Log.d("read more", events[position].toString())
            context.startActivity(
                    Intent(context, ThisEventActivity::class.java).putExtra("thisEvent", events[position].eventID)
            )
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = events.size

}
