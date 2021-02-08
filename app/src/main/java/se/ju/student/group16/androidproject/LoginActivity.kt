package se.ju.student.group16.androidproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if(currentUser != null){
            Log.d("currentUser", currentUser.displayName.toString())
            startActivity(Intent(this, EventActivity::class.java))
        }

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val signupBtn = findViewById<Button>(R.id.signupBtn)

        loginBtn.setOnClickListener {
            val emailInput = findViewById<EditText>(R.id.emailInput)
            val passwordInput = findViewById<EditText>(R.id.passwordInput)

            if (emailInput.text.toString().trim().isNotEmpty() && passwordInput.text.toString().trim().isNotEmpty())
                auth.signInWithEmailAndPassword(emailInput.text.toString(), passwordInput.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("login", "signInWithEmail:success")
                                var user = auth.currentUser
                                Log.d("user", user.toString())
                                //updateUI(user)
                                val intent = Intent(this, EventActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("login", "signInWithEmail:failure", task.exception)
                                Toast.makeText(baseContext, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show()
                                //updateUI(null)
                            }
                        }
        }

        signupBtn.setOnClickListener {
            startActivity(
                    Intent(this, SignUpActivity::class.java)
            )
        }
        /*
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(android.R.string.default_web_client_id))
                .requestEmail()
                .build()

         */
    }
}