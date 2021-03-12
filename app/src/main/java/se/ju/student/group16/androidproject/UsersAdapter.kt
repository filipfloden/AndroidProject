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
import java.util.*

class UsersAdapter(private val context: Activity, private val user: MutableList<User>) : ArrayAdapter<User>(context, R.layout.user_row, user) {

    private var userCopy = user
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val users = "users"
    private val friendsPending = "friends-pending"

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.user_row, null, true)
        val usersName = rowView.findViewById<TextView>(R.id.user_name)
        user.sortBy { it.displayname }
        usersName.text = user[position].displayname

        val addFriend = rowView.findViewById<Button>(R.id.add_friend)

        addFriend.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            database = FirebaseDatabase.getInstance().reference
            val currentUser = auth.currentUser

            database.child(users).child(currentUser?.uid.toString()).child(friendsPending).child(user[position].uid).setValue("sent")
            database.child(users).child(user[position].uid).child(friendsPending).child(currentUser?.uid.toString()).setValue("received")

            addFriend.text = "✓ Added"
        }
        return rowView
        //super.getView(position, convertView, parent)
    }

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
}