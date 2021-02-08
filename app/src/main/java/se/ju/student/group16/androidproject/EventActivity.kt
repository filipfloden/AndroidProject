package se.ju.student.group16.androidproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class EventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_event)

        val eventButton = findViewById<Button>(R.id.event_button)
        val friendsButton = findViewById<Button>(R.id.friend_button)
        val settingsButton = findViewById<Button>(R.id.settings_button)

        supportFragmentManager.beginTransaction().run{
            add(R.id.frame_layout, EventFragment())
            commit()
        }

        eventButton.setOnClickListener{
            supportFragmentManager.beginTransaction().run{
                replace(R.id.frame_layout, EventFragment())
                commit()
            }
        }

        friendsButton.setOnClickListener{
            supportFragmentManager.beginTransaction().run{
                replace(R.id.frame_layout, FriendsFragment())
                commit()
            }
        }

        settingsButton.setOnClickListener{
            supportFragmentManager.beginTransaction().run{
                replace(R.id.frame_layout, SettingsFragment())
                commit()
            }
        }

//        val logoutButton = findViewById<Button>(R.id.logoutBtn)
//        logoutButton.setOnClickListener{
//        }




    }
}