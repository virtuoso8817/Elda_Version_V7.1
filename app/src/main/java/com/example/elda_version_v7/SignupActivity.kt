package com.example.elda_version_v7

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.elda_version_v7.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.signupArrow.setOnClickListener {
            val email = binding.signupEmail.text.toString()
            val password = binding.signupPassword.text.toString()
            val confirmPassword = binding.signupConfirm.text.toString()
            val name = binding.signupName.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && name.isNotEmpty()) {
                if (password == confirmPassword) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Store additional user information, such as name
                                val user = firebaseAuth.currentUser
                                val profileUpdates = userProfileChangeRequest {
                                    displayName = name
                                }
                                user?.updateProfile(profileUpdates)

                                // Save the user's name in SharedPreferences
                                val sharedPref =
                                    getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                                with(sharedPref.edit()) {
                                    putString("USER_NAME", name)
                                    apply()
                                }

                                // Pass user's name to MainActivity
                                val intent = Intent(this, LoginActivity::class.java)
                                intent.putExtra("USER_NAME", name)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this,
                                    task.exception?.message ?: "Sign up failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginRedirectText.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }
}
