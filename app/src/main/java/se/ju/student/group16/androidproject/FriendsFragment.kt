package se.ju.student.group16.androidproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import se.ju.student.group16.androidproject.databinding.FragmentEventBinding
import se.ju.student.group16.androidproject.databinding.FragmentFriendsBinding

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
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val users = "users"
    private val friends = "friends"
    private val displayname = "displayname"
    private val email = "email"
    private val friendsList = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


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
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        val friendsListView = binding.friendsListView
        val currentUser = auth.currentUser
        val friendsAdapter = FriendAdapter(this.activity!!, friendsList)
        friendsListView.adapter = friendsAdapter

        database.child(users).child(currentUser?.uid.toString()).child(friends).get().addOnSuccessListener {
            friendsList.clear()
            for (friend in it.children){
                database.child(users).child(friend.key.toString()).get().addOnSuccessListener { info ->
                    val friendDisplayName = info.child(displayname).value
                    val friendEmail = info.child(email).value
                    friendsList.add(User(info.key.toString(), friendDisplayName.toString(), friendEmail.toString()))
                    friendsAdapter.notifyDataSetChanged()
                }
            }
        }
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