package com.example.myapplication1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class forgotpassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpassword)




        val resetB: Button = findViewById(R.id.button12)

        // Set OnClickListener to handle the button click event
        resetB.setOnClickListener {
            // Create an Intent to navigate to the SignupActivity
            val intent = Intent(this, resetpassword::class.java)

            // Start the SignupActivity
            startActivity(intent)
        }


        val loginPasswordTextView: TextView = findViewById(R.id.textView68)
        loginPasswordTextView.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }





    }



}