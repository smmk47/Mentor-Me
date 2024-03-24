package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class chooserole<Button> : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chooserole)


//        Handler().postDelayed({
//            // Intent to start the LoginPage activity
//            val loginIntent = Intent(this, homepage::class.java)
//            startActivity(loginIntent)
//            finish() // Destroy this activity so the user can't return to it
//        }, 2500) // 2500 milliseconds is 2.5 seconds
//



        val buttons: android.widget.Button = findViewById(R.id.student)

        buttons.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        val buttonm: android.widget.Button = findViewById(R.id.mentor)

        buttonm.setOnClickListener {
            val intent = Intent(this, mentorlogin::class.java)
            startActivity(intent)
        }
        
    }
}