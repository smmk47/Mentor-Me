package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Using postDelayed to send a message with a delayed time.
        Handler().postDelayed({
            // Intent to start the LoginPage activity
            val loginIntent = Intent(this, chooserole::class.java)
            startActivity(loginIntent)
            finish() // Destroy this activity so the user can't return to it
        }, 2500) // 2500 milliseconds is 2.5 seconds
    }
}

// Make sure you have a LoginPage activity defined in your application.
