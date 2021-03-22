package se.ju.student.group16.androidproject

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import se.ju.student.group16.androidproject.event.EventActivity
import se.ju.student.group16.androidproject.event.ThisEventActivity
import se.ju.student.group16.androidproject.event.eventRepository
import se.ju.student.group16.androidproject.friend.friendsRepository

class LoadDataActivity : AppCompatActivity() {

    private val database = firebaseRepository.getDatabaseReference()
    private val currentUser = firebaseRepository.getCurrentUser()

    private val usersPath = "users"
    private val friendsPath = "friends"
    private val myEventsPath = "my-events"
    private val upcomingEventsPath = "upcoming-events"
    private val eventPath = "event"
    private lateinit var eventActivity: String
    private lateinit var thisEventActivity: String
    private lateinit var thisEvent: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_data)
        eventActivity = intent.getStringExtra("event-activity").toString()
        thisEventActivity = intent.getStringExtra("this-event-activity").toString()
        thisEvent = intent.getStringExtra("thisEvent").toString()
        Log.d("Extra", "$eventActivity | $thisEvent | $thisEventActivity")
        friendsRepository.clearFriends()
        eventRepository.clearEvents()
        getFriends()
    }
    private fun getFriends(){
        Log.d("Started", "Getting friends")
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
            getUpcomingEvents()
        }
    }
    private fun getUpcomingEvents(){
        Log.d("Started", "Getting upcoming events")
        database.child(usersPath).child(currentUser?.uid.toString()).child(upcomingEventsPath).get().addOnSuccessListener {
            for (event in it.children){
                database.child(eventPath).child(event.key.toString()).get().addOnSuccessListener { info ->
                    eventRepository.addUpcomingEvent(
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
            Log.d("Finished", "Getting upcoming events")
            getMyEvents()
        }
    }
    private fun getMyEvents(){
        Log.d("Started", "Getting events")

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
            if (thisEvent == "null") {
                startActivity(Intent(this, EventActivity::class.java))
            }else {
                startActivity(Intent(this, ThisEventActivity::class.java).putExtra("thisEvent", thisEvent))
            }
            finish()
        }
    }
}
