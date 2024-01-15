package com.example.elda_version_v7

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class VoiceActivity : AppCompatActivity() {

    private val CALL_PERMISSION_REQUEST_CODE = 100

    // Initialize Firebase
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val sosNumberReference: DatabaseReference = database.getReference("sosNumber")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice)
        val phonebutton = findViewById<Button>(R.id.phonebutton)
        val phonetext = findViewById<TextView>(R.id.phonetextview)

        phonebutton.setOnClickListener {
            val phoneNumber = phonetext.text.toString()

            if (phoneNumber.isEmpty()) {
                Toast.makeText(this, "Please enter a phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save the phone number to Firebase
            saveSOSNumber(phoneNumber)
            Toast.makeText(this, "SOS set", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveSOSNumber(phoneNumber: String) {
        sosNumberReference.setValue(phoneNumber)
            .addOnSuccessListener {
                Toast.makeText(this, "SOS Number saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save SOS Number", Toast.LENGTH_SHORT).show()
            }
    }

    fun showSOSInfo(view: View) {
        val knowMoreTextView = findViewById<TextView>(R.id.knowMoreTextView)
        val sosInfoBox = findViewById<LinearLayout>(R.id.sosInfoBox)

        // Remove the "Know more about SOS" text with animation
        val collapseAnimation = CollapseAnimation(knowMoreTextView, 300)
        knowMoreTextView.startAnimation(collapseAnimation)

        // Show the SOS info box with animation
        sosInfoBox.visibility = View.VISIBLE
        knowMoreTextView.visibility = View.GONE
        sosInfoBox.alpha = 0f
        sosInfoBox.animate().alpha(1f).setDuration(500).start()
    }

    private fun checkCallPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CALL_PHONE),
                CALL_PERMISSION_REQUEST_CODE
            )

            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CALL_PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, call intent
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private class CollapseAnimation(
        private val view: View,
        private var duration: Long
    ) : Animation() {
        private val initialHeight: Int = view.height

        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation?
        ) {
            view.layoutParams.height =
                if (interpolatedTime == 1f) {
                    0
                } else {
                    (initialHeight * (1 - interpolatedTime)).toInt()
                }
            view.requestLayout()
        }

        override fun initialize(
            width: Int,
            height: Int,
            parentWidth: Int,
            parentHeight: Int
        ) {
            super.initialize(width, height, parentWidth, parentHeight)
            duration = this.duration
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }
}
