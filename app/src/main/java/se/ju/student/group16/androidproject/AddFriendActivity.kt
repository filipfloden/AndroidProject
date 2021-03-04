package se.ju.student.group16.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddFriendActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val users = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val usersListView = findViewById<ListView>(R.id.usersListView)

        val currentUser = auth.currentUser
        database.child("users").get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            for (user in it.children){
                Log.d("a", user.child("displayname").value.toString())
                users.add(User(user.key.toString(), user.child("displayname").value.toString(), user.child("email").value.toString()))
            }
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