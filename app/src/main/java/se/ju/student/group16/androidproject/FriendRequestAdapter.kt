package se.ju.student.group16.androidproject

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FriendRequestAdapter(private val context: Activity, private val user: MutableList<User>) : ArrayAdapter<User>(context, R.layout.fr_row, user) {

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
        val rowView = inflater.inflate(R.layout.fr_row, null, true)

        val usersName = rowView.findViewById<TextView>(R.id.fr_name)

        usersName.text = user[position].displayname

        val acceptFriend = rowView.findViewById<Button>(R.id.accept_friend)
        val denyFriend = rowView.findViewById<Button>(R.id.deny_friend)

        acceptFriend.setOnClickListener {
            database.child(users).child(currentUser?.uid.toString()).child(friends).child(user[position].uid).setValue("true").addOnSuccessListener {
                Log.d("firebase", "Accepted " + user[position].uid)
                database.child(users).child(user[position].uid).child(friends).child(currentUser?.uid.toString()).setValue("true")
                database.child(users).child(user[position].uid).child(friendsPending).child(currentUser?.uid.toString()).removeValue()
                database.child(users).child(currentUser?.uid.toString()).child(friendsPending).child(user[position].uid).removeValue()
            }.addOnFailureListener{
                Log.e("firebase", "Error accepting friend", it)
            }
        }
        denyFriend.setOnClickListener {
            database.child(users).child(user[position].uid).child(friendsPending).child(currentUser?.uid.toString()).removeValue()
            database.child(users).child(currentUser?.uid.toString()).child(friendsPending).child(user[position].uid).removeValue()
        }
        return rowView
        //super.getView(position, convertView, parent)
    }
}