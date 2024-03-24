package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

class Main10Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main10)

        // Retrieve extras from the intent
        val name = intent.getStringExtra("name")
        val designation = intent.getStringExtra("designation")
        val description = intent.getStringExtra("description")
        val price = intent.getStringExtra("sessionprice")
        val status = intent.getStringExtra("status")
        val picuri = intent.getStringExtra("picuri")
        val circleImageView: CircleImageView = findViewById(R.id.profile)


        if (picuri != null) {

            Glide.with(this@Main10Activity /* context */)
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
        textViewName.text = "Hi I'm  "+name
        textViewDesignation.text = designation
        textViewDescription.text = description
        //textViewPrice.text = price
        //textViewStatus.text = status



        val button7: Button = findViewById(R.id.button7)

        button7.setOnClickListener {
            // Create an Intent to navigate to the SignupActivity
            val intent = Intent(this, MainActivity13::class.java).apply {
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



        val button5: Button = findViewById(R.id.button5)

        button5.setOnClickListener {
            val intent = Intent(this, MainActivity11::class.java).apply {
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

        val database = FirebaseDatabase.getInstance().reference
        val auth = FirebaseAuth.getInstance()
        val button6: Button = findViewById(R.id.button6) // Assuming you have a button with id button6 for joining the community

        button6.setOnClickListener {
            val currentUser = auth.currentUser
            currentUser?.let { user ->
                val userEmail = user.email
                userEmail?.let { email ->
                    val mentorName = intent.getStringExtra("name")
                    val picUri = intent.getStringExtra("picuri")

                    // Check if the community of the required mentor exists in the database
                    database.child("communities").orderByChild("mentorName").equalTo(mentorName)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    // Community of the mentor exists
                                    var communityKey: String? = null
                                    for (childSnapshot in snapshot.children) {
                                        communityKey = childSnapshot.key
                                        break
                                    }
                                    communityKey?.let { key ->
                                        // Check if the current user is already a member of that community
                                        database.child("communities").child(key).child("members")
                                            .orderByValue().equalTo(email)
                                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                                override fun onDataChange(memberSnapshot: DataSnapshot) {
                                                    if (!memberSnapshot.exists()) {
                                                        // User is not a member of the community, add them
                                                        database.child("communities").child(key)
                                                            .child("members").push().setValue(email)
                                                            .addOnCompleteListener { task ->
                                                                if (task.isSuccessful) {
                                                                    Toast.makeText(
                                                                        this@Main10Activity,
                                                                        "Joined community successfully!",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()

                                                                    val firebaseHelper = FirebaseHelper()
                                                                    val notification = notifications(useremail = userEmail, notificationdata = " Your joined the community  of  " + mentorName.toString() )
                                                                    firebaseHelper.addNotification(notification)





                                                                } else {
                                                                    Toast.makeText(
                                                                        this@Main10Activity,
                                                                        "Failed to join community. Please try again.",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                }
                                                            }
                                                    } else {
                                                        // User is already a member of the community
                                                        Toast.makeText(
                                                            this@Main10Activity,
                                                            "You are already part of this community.",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }

                                                override fun onCancelled(error: DatabaseError) {
                                                    // Handle error
                                                    Toast.makeText(
                                                        this@Main10Activity,
                                                        "Database error: ${error.message}",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            })
                                    }
                                } else {
                                    // Community of the mentor does not exist, create it
                                    val community = hashMapOf(
                                        "mentorName" to mentorName,
                                        "picUri" to picUri,
                                        "members" to hashMapOf<String, String>()
                                    )
                                    val newCommunityRef = database.child("communities").push()
                                    newCommunityRef.setValue(community)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                // Add the current user to the newly created community
                                                newCommunityRef.child("members").push().setValue(email)
                                                    .addOnCompleteListener { task ->
                                                        if (task.isSuccessful) {
                                                            Toast.makeText(
                                                                this@Main10Activity,
                                                                "Joined community successfully!",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        } else {
                                                            Toast.makeText(
                                                                this@Main10Activity,
                                                                "Failed to join community. Please try again.",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    }
                                            } else {
                                                Toast.makeText(
                                                    this@Main10Activity,
                                                    "Failed to create community. Please try again.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle error
                                Toast.makeText(
                                    this@Main10Activity,
                                    "Database error: ${error.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                } ?: run {
                    // Handle if userEmail is null
                    Toast.makeText(
                        this@Main10Activity,
                        "User email not found.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } ?: run {
                // Handle if currentUser is null
                Toast.makeText(
                    this@Main10Activity,
                    "User not logged in.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }




    }
}