package se.ju.student.group16.androidproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.content.Context
import android.widget.CalendarView
import android.widget.PopupWindow
import androidx.cardview.widget.CardView
import se.ju.student.group16.androidproject.databinding.ActivityCreateEventBinding
import se.ju.student.group16.androidproject.databinding.FragmentFriendsBinding

class CreateEventActivity : AppCompatActivity() {
    val pickADateBtn = findViewById<Button>(R.id.pickDateBtn)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        
        }


    }

}