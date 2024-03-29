package se.ju.student.group16.androidproject

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import se.ju.student.group16.androidproject.databinding.FragmentSettingsBinding
import se.ju.student.group16.androidproject.friend.friendsRepository

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val auth = firebaseRepository.getAuth()

    lateinit var binding: FragmentSettingsBinding


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
    ) = FragmentSettingsBinding.inflate(inflater, container, false).run {
        binding = this
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logoutBtn.setOnClickListener {
            signOut()
        }

        binding.changePasswordBtn.setOnClickListener {
            val intent = Intent(context, changePassword::class.java)
            startActivity(intent)
        }

        // TODO, listen for clicks on the Add button, add a number to the list and then
        // tell the adapter that the list has changed (e.g. notifyDataSetChanged).
    }
    fun signOut(){
        auth.signOut()
        friendsRepository.clearFriends()
        val intent = Intent(context, LoginActivity::class.java)
        intent.putExtra("finish", true)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // To clean up all activities
        this.activity?.finish()
        startActivity(intent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}