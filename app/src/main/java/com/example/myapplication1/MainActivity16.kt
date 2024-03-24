package com.example.myapplication1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity16 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main16)
        val communityId = intent.getStringExtra("communityId")
        val mentorname = intent.getStringExtra("mentorname")
        val picuri = intent.getStringExtra("picuri")
        val email = intent.getStringExtra("email")

        val circleImageView: CircleImageView = findViewById(R.id.profilepic)
        val textViewName = findViewById<TextView>(R.id.textView38)
        textViewName.text = mentorname

        Glide.with(this@MainActivity16 /* context */)
            .load(picuri)
            .placeholder(R.drawable.profileicon) // Placeholder image while loading
            .error(R.drawable.profileicon) // Error image if loading fails
            .into(circleImageView)


        val clickPhotoButton = findViewById<Button>(R.id.clickphoto)

        // Set OnClickListener for the button
        clickPhotoButton.setOnClickListener {
            Toast.makeText(applicationContext, "Button Clicked", Toast.LENGTH_SHORT).show()

            // Create an Intent to navigate to MainActivity17
            val intent = Intent(this, MainActivity17::class.java)
            startActivity(intent)
        }


        val button17: Button = findViewById(R.id.button17)

        button17.setOnClickListener {
            val intent = Intent(this, homepage::class.java)
            startActivity(intent)
        }


        val button18: Button = findViewById(R.id.button18)

        button18.setOnClickListener {
            val intent = Intent(this, searchhome::class.java)
            startActivity(intent)
        }


        val button22: Button = findViewById(R.id.button22)

        button22.setOnClickListener {
            val intent = Intent(this, MainActivity12::class.java)
            startActivity(intent)
        }
        val button20: Button = findViewById(R.id.button20)
        button20.setOnClickListener {
            val intent = Intent(this, MainActivity14::class.java)
            startActivity(intent)
        }


        val button211: Button = findViewById(R.id.button211)
        button211.setOnClickListener {
            val intent = Intent(this, MainActivity21::class.java)
            startActivity(intent)
        }

    }
}