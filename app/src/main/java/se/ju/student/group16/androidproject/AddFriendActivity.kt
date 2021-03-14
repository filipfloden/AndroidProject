package se.ju.student.group16.androidproject

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*


class AddFriendActivity : AppCompatActivity() {

    private var database = firebaseRepository.getDatabaseReference()
    private val currentUser = firebaseRepository.getCurrentUser()
    private val allUsers = mutableListOf<User>()
    private val friendRequests = mutableListOf<User>()
    private val users = "users"
    private val received = "received"
    private val friendsPending = "friends-pending"
    private val displayname = "displayname"
    private val email = "email"

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)

        val usersListView = findViewById<ListView>(R.id.usersListView)
        val friendRequestListView = findViewById<ListView>(R.id.friendRequestListView)
        val searchBar = findViewById<SearchView>(R.id.search_user)
        val friendRequestAdapter = FriendRequestAdapter(this, friendRequests)
        val usersAdapter = UsersAdapter(this, allUsers)

        friendRequestListView.adapter = friendRequestAdapter
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.children
                friendRequests.clear()
                friendRequestAdapter.notifyDataSetChanged()
                for (fr in post){
                    if (fr.value.toString() == received){
                        database.child(users).child(fr.key.toString()).get().addOnSuccessListener {
                            friendRequests.add(
                                User(
                                    it.key.toString(),
                                    it.child(displayname).value.toString(),
                                    it.child(
                                        email
                                    ).value.toString()
                                )
                            )
                            friendRequestAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("error", "loadPost:onCancelled", databaseError.toException())
            }
        }
        database.child(users).child(currentUser?.uid.toString()).child(friendsPending).addValueEventListener(
            postListener
        )
        var currentUsersFriends = ""
        database.child(users).child(currentUser?.uid.toString()).child("friends").get().addOnSuccessListener {
            currentUsersFriends = it.value.toString()
        }
        var currentUsersPendingFriends = ""
        database.child(users).child(currentUser?.uid.toString()).child(friendsPending).get().addOnSuccessListener {
            currentUsersPendingFriends = it.value.toString()
        }
        database.child(users).get().addOnSuccessListener {

            for (user in it.children){
                if (user.key.toString() != currentUser?.uid.toString())
                    if (!currentUsersFriends.contains(user.key.toString()) && !currentUsersPendingFriends.contains(user.key.toString()))
                        allUsers.add(
                            User(
                                user.key.toString(),
                                user.child(displayname).value.toString(),
                                user.child(
                                    "email"
                                ).value.toString()
                            )
                        )
            }
            usersListView.adapter = usersAdapter
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

        usersListView.setOnItemClickListener { parent, view, position, id ->
            val userClicked = usersListView.getItemAtPosition(position) as User
            database.child(users).child(currentUser?.uid.toString()).child(friendsPending).child(userClicked.uid).setValue("sent")
            database.child(users).child(userClicked.uid).child(friendsPending).child(currentUser?.uid.toString()).setValue("received")
        }

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        val test = friendsRepository.getAllFriends()
        Log.d("Friends List", test.toString())
    }
}