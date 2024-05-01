package com.example.myapplication1

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
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

        val firebaseHelper = FirebaseHelper()
        val notification = notifications(useremail = "currentUserEmail", notificationdata = " Your have reviewed the mentor    " + name.toString() )
        firebaseHelper.addNotification(notification)

        val myFirebaseMessagingService = MyFirebaseMessagingService()
        myFirebaseMessagingService.generateNotification(this,"mentor_me", " Your have reviewed the mentor    " + name.toString() )


        // Set values to TextViews
        textViewName.text = "Hi I'm  $name"
        //textViewDesignation.text = designation
        //textViewDescription.text = description

        if (!picuri.isNullOrEmpty()) {
            Glide.with(this)
                .load(picuri)
                .placeholder(R.drawable.profileicon) // Placeholder image while loading
                .error(R.drawable.pro) // Error image if loading fails
                .into(circleImageView)
        } else {
            // Handle if user data is null
            Toast.makeText(applicationContext, "image not found", Toast.LENGTH_SHORT).show()
        }

        // RatingBar and EditText initialization
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val feedbackTextView = findViewById<EditText>(R.id.feedbackTextView)
        //val name = intent.getStringExtra("name")

        // Assuming you want to store the values when a button is clicked, you would set up a button click listener
        findViewById<Button>(R.id.button7).setOnClickListener {
            if (name != null) {
                storeValues(ratingBar, feedbackTextView ,name)
            }
        }
    }



    private fun storeValues(ratingBar: RatingBar, feedbackTextView: EditText, mentorName: String) {
        // Get the values from RatingBar and EditText
        val rating = ratingBar.rating.toString()
        val feedback = feedbackTextView.text.toString()
        val mentorname:String = mentorName.toString()

        val url = "http://192.168.0.102/smd/insertreview.php"


        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                Log.d("API Response", response)
            },
            Response.ErrorListener { error ->
                Log.e("API Error", "Error occurred: ${error.message}")
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()



                params["mentorname"] = mentorname
                params["useremail"] = "name"
                params["feedback"] = feedback
                params["rating"] = rating


                Log.d("name",feedback)
                Log.d("email",mentorname)
                Log.d("phone",feedback)
                Log.d("country",rating.toString())


//                val firebaseHelper = FirebaseHelper()
//                val notification = notifications(useremail = "userEmail.toString()", notificationdata = " Your have added a  mentor    " + name.toString() )
//                firebaseHelper.addNotification(notification)
//
//                val myFirebaseMessagingService = MyFirebaseMessagingService()
//                myFirebaseMessagingService.generateNotification(this,"mentor_me", " Your have added a  mentor    " + name.toString())
//

                return params
            }
        }
        Volley.newRequestQueue(this).add(stringRequest)


    }


}
