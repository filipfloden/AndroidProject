package se.ju.student.group16.androidproject

import android.app.Activity
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth


class ChatAdapter(private val context: Activity, private val messages: MutableList<Message>) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.message_text)
        val frameLayout: FrameLayout = view.findViewById(R.id.message_layout)
        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.message_row, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        //Log.d("Compare", currentUser?.uid.toString() + " | " + messages[position].user)
        if (currentUser?.uid.toString() == messages[position].user){
            Log.d("this user", messages[position].toString())
            viewHolder.textView.backgroundTintList = ContextCompat.getColorStateList(context, R.color.ChatBlue)
            /*
            val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(400, 10)
            params.gravity = Gravity.RIGHT

            viewHolder.frameLayout.layoutParams = params

             */
        }
        else{
            viewHolder.textView.backgroundTintList = ContextCompat.getColorStateList(context, R.color.WhiteGrey)
            viewHolder.textView.gravity = Gravity.LEFT
        }
        viewHolder.textView.text = messages[position].toString()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = messages.size

}
