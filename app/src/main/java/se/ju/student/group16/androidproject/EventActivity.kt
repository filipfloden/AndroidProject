package se.ju.student.group16.androidproject

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
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
