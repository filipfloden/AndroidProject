package se.ju.student.group16.androidproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import se.ju.student.group16.androidproject.event.EventActivity


class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val usernameInput = findViewById<EditText>(R.id.usernameInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val password2Input = findViewById<EditText>(R.id.password2Input)
        val createAccountBtn = findViewById<Button>(R.id.createAccountBtn)
        val signUpErrors = findViewById<TextView>(R.id.signUpErrors)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        createAccountBtn.setOnClickListener {
            if (emailInput.text.toString().trim().isEmpty() || usernameInput.text.toString().trim().isEmpty() || passwordInput.text.toString().trim().isEmpty()){
                signUpErrors.setText(R.string.signup_empty_fields)
            }else if (passwordInput.text.toString().trim() != password2Input.text.toString().trim()){
                signUpErrors.setText(R.string.passwords_dont_match)
            }else {
                auth.createUserWithEmailAndPassword(emailInput.text.toString(), passwordInput.text.toString()).addOnCompleteListener(this, OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Signup", "success")
                        Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show()

                        val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(usernameInput.text.toString()).build()

                        auth.currentUser!!.updateProfile(profileUpdates)

                        val currentUser = auth.currentUser
                        val dbPath = database.child("users").child(auth.currentUser!!.uid)
                        database.child("users").child(currentUser!!.uid).child("displayname").setValue(currentUser.displayName)
                        dbPath.child("displayname").setValue(usernameInput.text.toString())
                        dbPath.child("email").setValue(currentUser.email)

                        val intent = Intent(this, LoadDataActivity::class.java)
                                .putExtra("next-activity", "EventActivity")
                        startActivity(intent)
                        finish()
                    } else {
                        signUpErrors.setText(task.exception!!.message)
                        Log.d("Signup", "failed" + task.exception)
                        Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }
}