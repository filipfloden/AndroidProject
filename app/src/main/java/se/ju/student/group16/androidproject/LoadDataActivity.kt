package se.ju.student.group16.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter

class LoadDataActivity : AppCompatActivity() {

    private val database = firebaseRepository.getDatabaseReference()
    private val currentUser = firebaseRepository.getCurrentUser()

    private val usersPath = "users"
    private val friendsPath = "friends"
    private val myEventsPath = "my-events"
    private val upcomingEventsPath = "upcoming-events"
    private val eventPath = "event"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_data)
        getFriends()
    }
    private fun getFriends(){
        Log.d("Started", "Getting friends")
        friendsRepository.clearFriends()
        database.child(usersPath).child(currentUser?.uid.toString()).child(friendsPath).get().addOnSuccessListener {
            for (friend in it.children){
                database.child(usersPath).child(friend.key.toString()).get().addOnSuccessListener { info ->
                    val friendUID = info.key.toString()
                    val friendDisplayName = info.child("displayname").value.toString()
                    val friendEmail = info.child("email").value.toString()
                    friendsRepository.addUser(friendUID, friendDisplayName, friendEmail)
                }
            }
            Log.d("Finished", "Getting friends")
            getMyEvents()
        }
    }
    private fun getMyEvents(){
        Log.d("Started", "Getting events")
        eventRepository.clearEvents()
        database.child(usersPath).child(currentUser?.uid.toString()).child(myEventsPath).get().addOnSuccessListener {
            for (event in it.children){
                database.child(eventPath).child(event.key.toString()).get().addOnSuccessListener { info ->
                    eventRepository.addMyEvent(
                            event.key.toString(),
                            info.child("host").value.toString(),
                            info.child("title").value.toString(),
                            info.child("description").value.toString(),
                            info.child("theme").value.toString(),
                            info.child("date").value.toString(),
                            info.child("latitude").value as Double,
                            info.child("longitude").value as Double,
                            info.child("guest-list").value as Map<String, String>
                    )
                }.addOnFailureListener{
                    Log.e("firebase", "Error getting data", it)
                }
            }
            Log.d("Finished", "Getting my events")
            getUpcomingEvents()
        }
    }
    private fun getUpcomingEvents(){
        Log.d("Started", "Getting upcoming events")
        eventRepository.clearEvents()
        database.child(usersPath).child(currentUser?.uid.toString()).child(upcomingEventsPath).get().addOnSuccessListener {
            for (event in it.children){
                database.child(eventPath).child(event.key.toString()).get().addOnSuccessListener { info ->
                    eventRepository.addUpcomingEvents(
                            event.key.toString(),
                            info.child("host").value.toString(),
                            info.child("title").value.toString(),
                            info.child("description").value.toString(),
                            info.child("theme").value.toString(),
                            info.child("date").value.toString(),
                            info.child("latitude").value as Double,
                            info.child("longitude").value as Double,
                            info.child("guest-list").value as Map<String, String>
                    )
                }.addOnFailureListener{
                    Log.e("firebase", "Error getting data", it)
                }
            }
            startActivity(Intent(this, EventActivity::class.java))
            finish()
            Log.d("Finished", "Getting upcoming events")
        }
    }
}
