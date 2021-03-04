package se.ju.student.group16.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddFriendActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val users = mutableListOf<User>()
    private val friendRequests = mutableListOf<User>()
    private val received = "received"
    private val displayname = "displayname"
    private val email = "email"
    private val empty = ""
    private var friends = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val usersListView = findViewById<ListView>(R.id.usersListView)
        val friendRequestListView = findViewById<ListView>(R.id.friendRequestListView)
        val currentUser = auth.currentUser

        database.child("users").child(currentUser?.uid.toString()).child("friends-pending").get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            for (fr in it.children){
                if (fr.value.toString() == received){
                    friendRequests.add(User(fr.key.toString(), empty, empty))
                }
            }
        }.addOnFailureListener{
            Log.e("firebase", "Error gettin data", it)
        }

        database.child("users").get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            for (user in it.children){
                users.add(User(user.key.toString(), user.child(displayname).value.toString(), user.child("email").value.toString()))
                if (friendRequests.contains(User(user.key.toString(), empty, empty))){
                    friendRequests.remove(User(user.key.toString(), empty, empty))
                    friendRequests.add(User(user.key.toString(), user.child(displayname).value.toString(), user.child(email).value.toString()))
                    Log.i("testing string", user.key.toString() + " " + user.child(displayname).toString() + "" + user.child(email).toString())
                }
            }
            friendRequestListView.adapter = FriendRequestAdapter(
                    this,
                    friendRequests
            )
            usersListView.adapter = ArrayAdapter<User>(
                    this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    users
            )
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

        usersListView.setOnItemClickListener { parent, view, position, id ->
            val userClicked = usersListView.getItemAtPosition(position) as User
            Log.d("userclicked", userClicked.uid)

            database.child("users").child(currentUser?.uid.toString()).child("friends-pending").child(userClicked.uid).setValue("sent")
            database.child("users").child(userClicked.uid).child("friends-pending").child(currentUser?.uid.toString()).setValue("received")
        }
    }
}