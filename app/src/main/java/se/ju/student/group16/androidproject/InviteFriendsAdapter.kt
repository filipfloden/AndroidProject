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
    
    private val inviteFriendsList = mutableListOf<User>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.invite_friends_row, null, true)

        val usersName = rowView.findViewById<TextView>(R.id.friend_name)

        usersName.text = friendsList[position].displayname

        val friendCheckBox = rowView.findViewById<CheckBox>(R.id.invite_friends_checkbox)

        friendCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                inviteFriendsList.add(User(friendsList[position].uid, friendsList[position].displayname, friendsList[position].email))
            }
            else{
                inviteFriendsList.remove(User(friendsList[position].uid, friendsList[position].displayname, friendsList[position].email))
            }
        }

        return rowView
        //super.getView(position, convertView, parent)
    }

    fun getCheckedFriends(): MutableList<User>{
        return inviteFriendsList
    }
}