package se.ju.student.group16.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.SearchView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddFriendActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val allUsers = mutableListOf<User>()
    private val friendRequests = mutableListOf<User>()
    private val users = "users"
    private val received = "received"
    private val friendsPending = "friends-pending"
    private val displayname = "displayname"
    private val email = "email"
    private val empty = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val usersListView = findViewById<ListView>(R.id.usersListView)
        val friendRequestListView = findViewById<ListView>(R.id.friendRequestListView)
        val searchBar = findViewById<SearchView>(R.id.search_user)
        searchBar.queryHint = "Search for user"
        val friendRequestAdapter = FriendRequestAdapter(this, friendRequests)
        val usersAdapter = UsersAdapter(this, allUsers)
        val currentUser = auth.currentUser

        database.child(users).child(currentUser?.uid.toString()).child(friendsPending).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            for (fr in it.children){
                if (fr.value.toString() == received){
                    friendRequests.add(User(fr.key.toString(), empty, empty))
                }
            }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
        database.child("users").get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            for (user in it.children){
                if (user.key.toString() != currentUser?.uid.toString())
                    allUsers.add(User(user.key.toString(), user.child(displayname).value.toString(), user.child("email").value.toString()))
                    if (friendRequests.contains(User(user.key.toString(), empty, empty))){
                        friendRequests.remove(User(user.key.toString(), empty, empty))
                        friendRequests.add(User(user.key.toString(), user.child(displayname).value.toString(), user.child(email).value.toString()))
                    }
            }
            friendRequestListView.adapter = friendRequestAdapter
            usersListView.adapter = usersAdapter
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

        usersListView.setOnItemClickListener { parent, view, position, id ->
            val userClicked = usersListView.getItemAtPosition(position) as User
            Log.d("userclicked", userClicked.uid)

            database.child(users).child(currentUser?.uid.toString()).child(friendsPending).child(userClicked.uid).setValue("sent")
            database.child(users).child(userClicked.uid).child(friendsPending).child(currentUser?.uid.toString()).setValue("received")
        }

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                usersAdapter.filter(newText)
                usersAdapter.notifyDataSetChanged()
                return false
            }
        })
    }
}