package se.ju.student.group16.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val signupBtn = findViewById<Button>(R.id.signupBtn)

        loginBtn.setOnClickListener {
            startActivity(
                    Intent(this, EventActivity::class.java)
            )
        }

        signupBtn.setOnClickListener {
            startActivity(
                    Intent(this, SignUpActivity::class.java)
            )
        }
    }
}