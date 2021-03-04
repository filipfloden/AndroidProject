package se.ju.student.group16.androidproject

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView

class FriendRequestAdapter(private val context: Activity, private val user: MutableList<User>) : ArrayAdapter<User>(context, R.layout.fr_row, user) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.fr_row, null, true)

        val usersName = rowView.findViewById<TextView>(R.id.fr_name)

        usersName.text = user[position].displayname

        val acceptFriend = rowView.findViewById<Button>(R.id.accept_friend)
        val denyFriend = rowView.findViewById<Button>(R.id.deny_friend)

        acceptFriend.setOnClickListener {
            Log.d("FR", "Accepted " + user[position].uid)
        }
        denyFriend.setOnClickListener {
            Log.d("FR", "Denied " + user[position].uid)
        }

        return rowView
        //super.getView(position, convertView, parent)
    }
}