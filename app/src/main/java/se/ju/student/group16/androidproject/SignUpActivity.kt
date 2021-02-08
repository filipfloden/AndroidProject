package se.ju.student.group16.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val usernameInput = findViewById<EditText>(R.id.usernameInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val password2Input = findViewById<EditText>(R.id.password2Input)
        val createAccountBtn = findViewById<Button>(R.id.createAccountBtn)

        createAccountBtn.setOnClickListener {
            Log.d("test", "hmm")
            if (passwordInput.text.toString() == password2Input.text.toString()){
                auth = FirebaseAuth.getInstance()

                auth.createUserWithEmailAndPassword(usernameInput.text.toString(), passwordInput.text.toString()).addOnCompleteListener(this, OnCompleteListener{ task ->
                    Log.d("task", task.toString())
                    if(task.isSuccessful){
                        Log.d("test", "success")
                        Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, EventActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else {
                        Log.d("test", "failed")
                        Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }
}