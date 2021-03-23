package se.ju.student.group16.androidproject.friend

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.*
import se.ju.student.group16.androidproject.databinding.FragmentFriendsBinding
import se.ju.student.group16.androidproject.firebaseRepository

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FriendsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FriendsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentFriendsBinding


    private var database = firebaseRepository.getDatabaseReference()
    private val currentUser = firebaseRepository.getCurrentUser()
    private val usersPath = "users"
    private val friends = "friends"
    private val displayNamePath = "displayname"
    private val emailPath = "email"
    private var displayName = ""
    private var email = ""
    lateinit var friendsAdapter: FriendAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                //Log.d("firebase", "onChildAdded:" + dataSnapshot.key!!)

                // A new comment has been added, add it to the displayed list

                val uid = dataSnapshot.key.toString()

                database.child(usersPath).child(uid).get().addOnSuccessListener {
                    displayName = it.child(displayNamePath).value.toString()
                    email = it.child(emailPath).value.toString()
                    if(friendsRepository.getFriendById(uid) == null)
                        friendsRepository.addUser(uid, displayName, email)
                    friendsAdapter.notifyDataSetChanged()
                }
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
                val commentKey = dataSnapshot.key
                friendsRepository.deleteFriendById(commentKey.toString())
                friendsAdapter.notifyDataSetChanged()
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
                Toast.makeText(context, "Failed to load comments.",
                    Toast.LENGTH_SHORT).show()
            }
        }
        database.child(usersPath).child(currentUser?.uid.toString()).child(friends).addChildEventListener(childEventListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentFriendsBinding.inflate(inflater, container, false).run {
        binding = this
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val friendsListView = binding.friendsListView
        friendsAdapter = FriendAdapter(this.activity!!, friendsRepository.getAllFriends())
        friendsListView.adapter = friendsAdapter

        binding.addFriendBtn.setOnClickListener {
            startActivity(
                Intent(activity, AddFriendActivity::class.java)
            )
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FriendsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FriendsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}