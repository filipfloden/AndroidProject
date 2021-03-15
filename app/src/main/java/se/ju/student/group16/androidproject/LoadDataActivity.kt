package se.ju.student.group16.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LoadDataActivity : AppCompatActivity() {

    private val database = firebaseRepository.getDatabaseReference()
    private val currentUser = firebaseRepository.getCurrentUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_data)

        friendsRepository.clearFriends()
        database.child("users").child(currentUser?.uid.toString()).child("friends").get().addOnSuccessListener {
            for (friend in it.children){
                database.child("users").child(friend.key.toString()).get().addOnSuccessListener { info ->
                    val friendUID = info.key.toString()
                    val friendDisplayName = info.child("displayname").value.toString()
                    val friendEmail = info.child("email").value.toString()
                    friendsRepository.addUser(friendUID, friendDisplayName, friendEmail)
                }
            }
            startActivity(Intent(this, EventActivity::class.java))
            finish()
        }
    }
}