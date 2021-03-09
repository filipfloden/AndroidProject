package se.ju.student.group16.androidproject

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InviteFriendsAdapter(private val context: Activity, private val friendsList: MutableList<User>) : ArrayAdapter<User>(context, R.layout.invite_friends_row, friendsList) {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val users = "users"
    private val friends = "friends"
    private val received = "received"
    private val friendsPending = "friends-pending"
    private val displayname = "displayname"
    private val email = "email"

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        val currentUser = auth.currentUser

        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.invite_friends_row, null, true)

        val usersName = rowView.findViewById<TextView>(R.id.friend_name)

        usersName.text = friendsList[position].displayname

        val friendCheckBox = rowView.findViewById<CheckBox>(R.id.invite_friends_checkbox)

        friendCheckBox.setOnCheckedChangeListener { _, isChecked ->
            friendsList[position]

        }

        return rowView
        //super.getView(position, convertView, parent)
    }
}