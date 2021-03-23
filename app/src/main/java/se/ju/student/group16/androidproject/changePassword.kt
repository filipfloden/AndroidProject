package se.ju.student.group16.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class changePassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        val changePasswordBtn = findViewById<Button>(R.id.btn_change_password)

        changePasswordBtn.setOnClickListener{
            changePassword()
        }
    }

    private fun changePassword(){
        val currentPassword = findViewById<EditText>(R.id.et_current_password)
        val newPassword = findViewById<EditText>(R.id.et_new_password)
        val confirmPassword = findViewById<EditText>(R.id.et_confirm_password)

        val auth = firebaseRepository.getAuth()

        if(currentPassword.text.isNotEmpty() && newPassword.text.isNotEmpty() && confirmPassword.text.isNotEmpty()){

            if(newPassword.text.toString() == confirmPassword.text.toString()){

                val currentUser = firebaseRepository.getCurrentUser()
                if(currentUser != null && currentUser.email != null){
                    val credential = EmailAuthProvider.getCredential(currentUser.email!!, currentPassword.text.toString())

                    currentUser.reauthenticate(credential).addOnCompleteListener{
                        if(it.isSuccessful){
                            Toast.makeText(this,"Re-Authentication success.", Toast.LENGTH_SHORT).show()
                            currentUser.updatePassword(newPassword.text.toString()).addOnCompleteListener{  task ->
                                if(task.isSuccessful){
                                    Toast.makeText(this,"User Password changed", Toast.LENGTH_SHORT).show()
                                    auth.signOut()
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
                                }else{
                                    Toast.makeText(this,"User Password change failed.", Toast.LENGTH_SHORT).show()

                                }
                            }
                        }else{
                            Toast.makeText(this,"Re-Authentication failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    startActivity(Intent(this, LoginActivity::class.java))
                }

            }else{
                Toast.makeText(this,"Passwords doesn't match.", Toast.LENGTH_SHORT).show()
            }

        }else{
            Toast.makeText(this,"Please fill all the fields before submitting.", Toast.LENGTH_SHORT).show()
        }
    }
}