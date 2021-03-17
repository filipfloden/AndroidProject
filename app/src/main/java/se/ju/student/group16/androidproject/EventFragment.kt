package se.ju.student.group16.androidproject

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import se.ju.student.group16.androidproject.databinding.FragmentEventBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val REQUEST_CODE = 14


/**
 * A simple [Fragment] subclass.
 * Use the [EventFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EventFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var auth: FirebaseAuth
    lateinit var eventAdapter: EventAdapter

    lateinit var binding: FragmentEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        val database = firebaseRepository.getDatabaseReference()
        val currentUser = firebaseRepository.getCurrentUser()
        val usersPath = "users"
        auth = FirebaseAuth.getInstance()
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("firebase", "onChildAdded:" + dataSnapshot.key!!)

                // A new comment has been added, add it to the displayed list

                val uid = dataSnapshot.key.toString()
                /*
                database.child(usersPath).child(uid).get().addOnSuccessListener {
                    displayName = it.child(displayNamePath).value.toString()
                    email = it.child(emailPath).value.toString()
                }
                if(friendsRepository.getFriendById(uid) == null)
                    friendsRepository.addUser(uid, displayName, email)

                 */
                eventAdapter.notifyDataSetChanged()
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
                //eventRepository.deleteMyEventById(commentKey.toString())
                eventAdapter.notifyDataSetChanged()
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
                Toast.makeText(context, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show()
            }
        }
        database.child(usersPath).child(currentUser?.uid.toString()).child("upcoming-events").addChildEventListener(childEventListener)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) = FragmentEventBinding.inflate(inflater, container, false).run {
        binding = this
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Test", "TESTOING")
        
        val eventRecyclerView = binding.recyclerEvents
        eventAdapter = EventAdapter(this.activity!!, eventRepository.getAllUpcomingEvents())
        eventRecyclerView.adapter = eventAdapter
        eventRecyclerView.layoutManager = LinearLayoutManager(this.activity!!, LinearLayoutManager.HORIZONTAL, false)
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(eventRecyclerView)
        binding.createEventBtn.setOnClickListener{
            val intent = Intent(context, CreateEventActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }

        binding.myEventButton.setOnClickListener {
            val intent = Intent(context, MyEventsActivity::class.java)
            startActivity(intent)
        }
        eventAdapter.notifyDataSetChanged()
        // TODO, listen for clicks on the Add button, add a number to the list and then
        // tell the adapter that the list has changed (e.g. notifyDataSetChanged).

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EventFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                EventFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}