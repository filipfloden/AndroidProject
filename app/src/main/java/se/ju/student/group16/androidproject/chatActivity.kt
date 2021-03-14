package se.ju.student.group16.androidproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.w3c.dom.Comment

class chatActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    lateinit var chattingWithUser: User
    private val messagesPath = "messages"
    private val usersPath = "users"
    private val listOfMessages = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val chattingWithUID = intent.getStringExtra("chattingWith")
        val chattingWithName = findViewById<TextView>(R.id.chatting_with)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        val currentUser = auth.currentUser
        val chatRecyclerView = findViewById<RecyclerView>(R.id.recycler_chat)
        val chatField = findViewById<EditText>(R.id.chat_message_input)
        val sendMessage = findViewById<Button>(R.id.button_chat_send)
        val chatAdapter = ChatAdapter(this, listOfMessages)
        chatRecyclerView.scrollToPosition(chatAdapter.itemCount-1)
        chatRecyclerView.adapter = chatAdapter
        chatRecyclerView.layoutManager = LinearLayoutManager(this)

        val ref1 = database.child(messagesPath).child(currentUser?.uid.toString() + "_" + chattingWithUID)
        val ref2 = database.child(messagesPath).child(chattingWithUID + "_" + currentUser?.uid.toString())

        database.child(usersPath).child(chattingWithUID.toString()).get().addOnSuccessListener {
            chattingWithUser = User(chattingWithUID.toString(), it.child("displayname").value.toString(), it.child("email").value.toString())
            chattingWithName.text = chattingWithUser.toString()
        }

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                //Log.d("firebase", "onChildAdded:" + dataSnapshot.key!!)

                // A new comment has been added, add it to the displayed list
                val user = dataSnapshot.child("user").value
                val message = dataSnapshot.child("message").value
                listOfMessages.add(Message(user.toString(), message.toString()))
                chatRecyclerView.scrollToPosition(chatAdapter.itemCount -1)
                chatAdapter.notifyDataSetChanged()

                // Message notification
                /*
                var builder = NotificationCompat.Builder(baseContext, "MESSAGE_CHANNEL")
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_light)
                        .setContentTitle("Message")
                        .setContentText(message.toString())
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                with(NotificationManagerCompat.from(baseContext)) {
                    // notificationId is a unique int for each notification that you must define
                    notify(2, builder.build())
                    Log.d("notfication sent", "true")
                }

                 */
                // ...
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("firebase", "onChildChanged: ${dataSnapshot.key}")

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                val newComment = dataSnapshot.value
                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d("firebase", "onChildRemoved:" + dataSnapshot.key!!)

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("firebase", "onChildMoved:" + dataSnapshot.key!!)

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                val movedComment = dataSnapshot.value
                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("firebase", "postComments:onCancelled", databaseError.toException())
                Toast.makeText(baseContext, "Failed to load comments.",
                    Toast.LENGTH_SHORT).show()
            }
        }
        ref1.addChildEventListener(childEventListener)

        sendMessage.setOnClickListener {
            val message = chatField.text.toString()
            if (message != "") {
                val map = mapOf("user" to currentUser?.uid, "message" to message)
                ref1.push().setValue(map)
                ref2.push().setValue(map)
                chatField.text = null
            }
        }
    }
}