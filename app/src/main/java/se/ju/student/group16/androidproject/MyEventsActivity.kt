package se.ju.student.group16.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MyEventsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val event = "event"
    private val id = "id"
    private val host = "host"
    private val email = "email"
    private var myEventsList = mutableListOf<Events>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_events)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        val myEventsListView = findViewById<ListView>(R.id.myEventsListView)
        val currentUser = auth.currentUser
        database.child(event).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }


 /*       myEventsListView.adapter = myEventsList<Events>(
            this,
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            toDoRepository.getAllTodos()
        )

        myEventsListView.setOnItemClickListener { parent, view, position, id ->
            val toDoItem = myEventsListView.adapter.getItem(position) as Events
            startActivity(Intent(this, ToDoItemActivity::class.java).putExtra("clickedId", toDoItem.id))
        }*/



    }
}