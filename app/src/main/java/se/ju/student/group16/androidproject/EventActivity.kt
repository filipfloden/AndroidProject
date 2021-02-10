package se.ju.student.group16.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth

class EventActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_event)
        auth = FirebaseAuth.getInstance()

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

        val logoutBtn = findViewById<Button>(R.id.logoutBtn)
        logoutBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
    private fun makeCurrentFragment(fragment: Fragment) = supportFragmentManager.beginTransaction().apply{
        replace(R.id.frame_layout, fragment)
        commit()
    }
}