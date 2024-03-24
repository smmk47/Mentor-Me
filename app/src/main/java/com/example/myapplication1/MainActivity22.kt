package com.example.myapplication1

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity22 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main22)

        auth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser

        // Reference to the Firebase Realtime Database
        val userRef = currentUser?.email?.replace(".", ",")?.let {
            FirebaseDatabase.getInstance().getReference("Users").child(it)
        }

        val edittextname = findViewById<EditText>(R.id.Edittextname)
        val edittextemail = findViewById<EditText>(R.id.Edittextemail)
        val edittextphone = findViewById<EditText>(R.id.editTextphone)
        val edittextcountry = findViewById<EditText>(R.id.editTextcountry)
        val edittextcity = findViewById<EditText>(R.id.Edittextcity)
        val update: Button = findViewById<Button>(R.id.button7)
        val circleImageView: CircleImageView = findViewById(R.id.textView12)

        update.setOnClickListener {
            val userEmail = edittextemail.text.toString().trim()
            val name = edittextname.text.toString().trim()
            val phone = edittextphone.text.toString().trim()
            val country = edittextcountry.text.toString().trim()
            val city = edittextcity.text.toString().trim()

            // Check if user is logged in
            val currentUser: FirebaseUser? = auth.currentUser
            if (currentUser != null) {
                // Construct the user key
                val userKey = currentUser.email?.replace(".", ",")

                // Get reference to the user node
                val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userKey ?: "")

                // Update user data in the database
                val updatedUser = User(name, userEmail, phone, country, city)
                userRef.setValue(updatedUser)
                    .addOnSuccessListener {
                        Toast.makeText(applicationContext, "User data updated successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firebase", "Error updating user data: ${e.message}")
                        Toast.makeText(applicationContext, "Failed to update user data", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        // Fetch user data from Firebase Realtime Database
        if (userRef != null) {
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userData = snapshot.getValue(User::class.java)
                    if (userData != null) {
                        // Set user information in EditText fields
                        edittextname.setText(userData.name)
                        edittextemail.setText(userData.email)
                        edittextphone.setText(userData.phone)
                        edittextcountry.setText(userData.country)
                        edittextcity.setText(userData.city)

                        Glide.with(this@MainActivity22 /* context */)
                            .load(userData.imageUrl)
                            .placeholder(R.drawable.profileicon) // Placeholder image while loading
                            .error(R.drawable.profileicon) // Error image if loading fails
                            .into(circleImageView)

                    } else {
                        // Handle if user data is null
                        Toast.makeText(applicationContext, "User data not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error while fetching data
                    Log.e("Firebase", "Error fetching user data: ${error.message}")
                    Toast.makeText(applicationContext, "Error fetching user data", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }





}

class User(
    var name: String = "",
    var email: String = "",
    var phone: String = "",
    var country: String = "",
    var city: String = "",
    val imageUrl: Any = "",
    val usertoken: Any = ""

)


