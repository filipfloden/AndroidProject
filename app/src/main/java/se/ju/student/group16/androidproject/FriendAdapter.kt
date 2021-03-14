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

class FriendAdapter(private val context: Activity, private val friends: MutableList<User>) : ArrayAdapter<User>(context, R.layout.friend_row, friends) {

    private var friendsCopy = friends
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val users = "users"
    private val friendsPending = "friends-pending"

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.friend_row, null, true)
        val friendsName = rowView.findViewById<TextView>(R.id.friend_name)
        val openChat = rowView.findViewById<Button>(R.id.open_chat)
        friends.sortBy { it.displayname }
        friendsName.text = friends[position].displayname

        openChat.setOnClickListener {
            context.startActivity(
                    Intent(context, chatActivity::class.java).putExtra("chattingWith", friends[position].uid)
            )
        }

        return rowView
        //super.getView(position, convertView, parent)
    }

    fun filter(text: String?) {
        var tempFriends = friendsCopy.toMutableList()
        val text = text!!.toLowerCase()
        friends.clear()
        if (text.isEmpty()) {
            friends.addAll(tempFriends)
        } else {
            for (i in tempFriends) {
                if (i.displayname.toLowerCase().contains(text)) {
                    friends.add(i)
                }
            }
        }
        friendsCopy = tempFriends
        notifyDataSetChanged()
    }
}