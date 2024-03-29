package se.ju.student.group16.androidproject.friend

import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import se.ju.student.group16.androidproject.R
import se.ju.student.group16.androidproject.User
import se.ju.student.group16.androidproject.event.EventActivity

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

                val user = dataSnapshot.child("user").value
                val message = dataSnapshot.child("message").value
                val timestamp = dataSnapshot.child("timestamp").value

                listOfMessages.add(Message(user.toString(), message.toString(), timestamp as Long))
                chatRecyclerView.scrollToPosition(chatAdapter.itemCount -1)
                chatAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("firebase", "onChildChanged: ${dataSnapshot.key}")

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.

                // ...
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d("firebase", "onChildRemoved:" + dataSnapshot.key!!)

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                listOfMessages.remove(listOfMessages.find { it.message == getString(R.string.is_typing) })
                chatAdapter.notifyDataSetChanged()
                // ...
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("firebase", "onChildMoved:" + dataSnapshot.key!!)

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.

                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("firebase", "postComments:onCancelled", databaseError.toException())
                Toast.makeText(baseContext, "Failed to load comments.",
                    Toast.LENGTH_SHORT).show()
            }
        }
        ref1.addChildEventListener(childEventListener)
        val messageID = ref2.push().key
        chatField.doOnTextChanged { _, _, _, _ ->
            chatRecyclerView.scrollToPosition(chatAdapter.itemCount -1)
            val map = mapOf("user" to currentUser?.uid, "message" to getString(R.string.is_typing), "timestamp" to ServerValue.TIMESTAMP)
            ref2.child(messageID!!).setValue(map)
            if (chatField.text.toString() == ""){
                ref2.child(messageID).removeValue()
                chatAdapter.notifyDataSetChanged()
            }
        }
        chatField.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEND){
                val message = chatField.text.toString()
                if (message != "") {
                    if (message.length < 300) {
                        val map = mapOf(
                            "user" to currentUser?.uid,
                            "message" to message,
                            "timestamp" to ServerValue.TIMESTAMP
                        )
                        ref1.push().setValue(map)
                        ref2.push().setValue(map)
                        chatField.text = null
                        ref2.child(messageID!!).removeValue()
                    }else{
                        Toast.makeText(this, getString(R.string.message_too_long),
                            Toast.LENGTH_SHORT).show()
                    }
                }
                true
            } else {
                false
            }
        }
    }
}