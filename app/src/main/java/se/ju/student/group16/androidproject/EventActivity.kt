package se.ju.student.group16.androidproject

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import se.ju.student.group16.androidproject.databinding.ActivityEventBinding

class EventActivity : AppCompatActivity() {

    private val currentUser = firebaseRepository.getCurrentUser()
    private val database = firebaseRepository.getDatabaseReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_event)

        val binding = ActivityEventBinding.inflate(layoutInflater)

        val bottom_nav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val eventFragment = EventFragment()
        val friendsFragment = FriendsFragment()
        val settingsFragment = SettingsFragment()
        makeCurrentFragment(eventFragment)

        bottom_nav.setOnNavigationItemSelectedListener{
            when (it.itemId){
                R.id.event_item -> makeCurrentFragment(eventFragment)
                R.id.friends_item -> makeCurrentFragment(friendsFragment)
                R.id.settings_item -> makeCurrentFragment(settingsFragment)
            }
            true
        }
        friendsRepository.clearFriends()
        database.child("users").child(currentUser?.uid.toString()).child("friends").get().addOnSuccessListener {
            for (friend in it.children){
                database.child("users").child(friend.key.toString()).get().addOnSuccessListener { info ->
                    val friendUID = info.key.toString()
                    val friendDisplayName = info.child("displayname").value.toString()
                    val friendEmail = info.child("email").value.toString()
                    friendsRepository.addUser(friendUID, friendDisplayName, friendEmail)
                }
            }
        }
        createNotificationChannel()
    }
    private fun makeCurrentFragment(fragment: Fragment) = supportFragmentManager.beginTransaction().apply{
        replace(R.id.frame_layout, fragment)
        commit()
    }
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.project_id)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("MESSAGE_CHANNEL", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
