package com.example.myapplication1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity11 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main11)

        val name = intent.getStringExtra("name")
        val designation = intent.getStringExtra("designation")
        val description = intent.getStringExtra("description")
        val price = intent.getStringExtra("sessionprice")
        val status = intent.getStringExtra("status")
        val picuri = intent.getStringExtra("picuri")
        val circleImageView: CircleImageView = findViewById(R.id.profile1)

        // Find TextViews in your layout
        val textViewName = findViewById<TextView>(R.id.textView38)
        //val textViewDesignation = findViewById<TextView>(R.id.textView39)
        //val textViewDescription = findViewById<TextView>(R.id.textView41)
        //val textViewPrice = findViewById<TextView>(R.id.textViewPrice)
        //val textViewStatus = findViewById<TextView>(R.id.textViewStatus)

        // Set values to TextViews
        textViewName.text = "Hi I'm  $name"
        //textViewDesignation.text = designation
        //textViewDescription.text = description

        if (!picuri.isNullOrEmpty()) {
            Glide.with(this)
                .load(picuri)
                .placeholder(R.drawable.profileicon) // Placeholder image while loading
                .error(R.drawable.profileicon) // Error image if loading fails
                .into(circleImageView)
        } else {
            // Handle if user data is null
            Toast.makeText(applicationContext, "image not found", Toast.LENGTH_SHORT).show()
        }

        // RatingBar and EditText initialization
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val feedbackTextView = findViewById<EditText>(R.id.feedbackTextView)

        // Assuming you want to store the values when a button is clicked, you would set up a button click listener
        findViewById<Button>(R.id.button7).setOnClickListener { storeValues(ratingBar, feedbackTextView ,name) }
    }

    private fun storeValues(ratingBar: RatingBar, feedbackTextView: EditText, name: String?) {
        // Get the values from RatingBar and EditText
        val currentUserEmail: String = FirebaseAuth.getInstance().currentUser?.email ?: ""
        val rating = ratingBar.rating
        val feedback = feedbackTextView.text.toString()
        val mentorname:String = name.toString()


        val review = reviews(mentorname, currentUserEmail, feedback, rating.toString())

        // Get a reference to the Firebase database
        val database = FirebaseDatabase.getInstance()
        val reviewsRef = database.getReference("reviews")

        // Push the review to the database
        val reviewId = reviewsRef.push().key // Generate a unique key for the review
        reviewId?.let {
            reviewsRef.child(it).setValue(review)
                .addOnSuccessListener {
                    // Data successfully saved
                    // You can add any further action here, like showing a toast
                   Toast.makeText(this, "Review added successfully", Toast.LENGTH_SHORT).show()

                    val firebaseHelper = FirebaseHelper()
                    val notification = notifications(useremail = currentUserEmail, notificationdata = " Your have reviewed the mentor    " + name.toString() )
                    firebaseHelper.addNotification(notification)

                    val myFirebaseMessagingService = MyFirebaseMessagingService()
                    myFirebaseMessagingService.generateNotification(this,"mentor_me", " Your have reviewed the mentor    " + name.toString() )


                }
                .addOnFailureListener {
                    // Failed to save data
                    // You can add any error handling here
                }
        }
    }
}
