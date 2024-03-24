package com.example.myapplication1


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class mentorhome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentorhome)

        // Retrieve extras from the intent
        val name = intent.getStringExtra("name")
        val designation = intent.getStringExtra("designation")
        val description = intent.getStringExtra("description")
        val price = intent.getStringExtra("sessionprice")
        val status = intent.getStringExtra("status")
        val picuri = intent.getStringExtra("picuri")
        val circleImageView: CircleImageView = findViewById(R.id.profile)


        if (picuri != null) {

            Glide.with(this@mentorhome /* context */)
                .load(picuri)
                .placeholder(R.drawable.profileicon) // Placeholder image while loading
                .error(R.drawable.profileicon) // Error image if loading fails
                .into(circleImageView)

        } else {
            // Handle if user data is null
            Toast.makeText(applicationContext, "image not found", Toast.LENGTH_SHORT).show()
        }

        // Find TextViews in your layout
        val textViewName = findViewById<TextView>(R.id.textView38)
        val textViewDesignation = findViewById<TextView>(R.id.textView39)
        val textViewDescription = findViewById<TextView>(R.id.textView41)
        //val textViewPrice = findViewById<TextView>(R.id.textViewPrice)
        //val textViewStatus = findViewById<TextView>(R.id.textViewStatus)

        // Set values to TextViews
        textViewName.text = name
        textViewDesignation.text = designation
        textViewDescription.text = description
        //textViewPrice.text = price
        //textViewStatus.text = status






        val button5: Button = findViewById(R.id.button7)

        button5.setOnClickListener {
            val intent = Intent(this, MainActivity14::class.java).apply {
                putExtra("name", name)
                putExtra("designation", designation)
                putExtra("description", description)
                putExtra("sessionprice", price)
                putExtra("status", status)
                putExtra("picuri", picuri)


            }
            startActivity(intent)

            // Start the SignupActivity
            startActivity(intent)
        }






    }
}