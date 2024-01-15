package com.example.elda_version_v7


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.elda_version_v7.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var textViewName: TextView
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textViewName = findViewById(R.id.textView_name)

        // Retrieve the user's name from Firebase
        val user = FirebaseAuth.getInstance().currentUser

        // Check if user is signed in
        if (user != null) {
            // Retrieve the user's name from Firebase Authentication
            val userName = user.displayName

            // Check if userName is not null (to avoid potential issues)
            if (!userName.isNullOrBlank()) {
                // Set the text of textView_name
                textViewName.text = userName
            }
        }

        binding.voice.setOnClickListener {
            val voiceIntent = Intent(this, VoiceActivity::class.java)
            startActivity(voiceIntent)
        }

        binding.water.setOnClickListener {
            val timerIntent = Intent(this, WaterReminder::class.java)
            startActivity(timerIntent)
        }
    }
}
