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
import java.lang.reflect.Type

class MyEventsActivity : AppCompatActivity() {

    private val database = firebaseRepository.getDatabaseReference()
    private val eventPath = "event"
    private val users = "users"
    private val id = "id"
    private val host = "host"
    private val myEvents = "my-events"
    private var myEventsList = mutableListOf<Events>()
    private var myEventsID = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_events)
        val myEventsListView = findViewById<ListView>(R.id.myEventsListView)
        val currentUser = firebaseRepository.getCurrentUser()
        database.child(users).child(currentUser?.uid.toString()).child(myEvents).get().addOnSuccessListener {
            it.children.forEach {
                myEventsID.add(
                        it.key.toString()
                )
            }
            for (id in myEventsID){
                database.child(eventPath).child(id).get().addOnSuccessListener {
                    myEventsList
                        .add(
                            Events(
                                it.key.toString(),
                                it.child("title").value.toString(),
                                it.child("description").value.toString(),
                                it.child("theme").value.toString(),
                                it.child("date").value.toString(),
                                it.child("latitude").value as Double,
                                it.child("longitude").value as Double,
                                it.child("host").value.toString(),
                                it.child("guest-list").value as Map<User, String>
                            )
                        )
                    myEventsListView.adapter = ArrayAdapter<Events>(
                            this,
                            android.R.layout.simple_list_item_1,
                            android.R.id.text1,
                            myEventsList
                    )

                    myEventsListView.setOnItemClickListener { parent, view, position, eventID ->
                        val eventListItem = myEventsListView.adapter.getItem(position) as Events
                        startActivity(Intent(this, UpdateMyEventActivity::class.java).putExtra("clickedId", eventListItem.eventID))
                    }
                }.addOnFailureListener{
                    Log.e("firebase", "Error getting data", it)
                }
            }

        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

    }
}