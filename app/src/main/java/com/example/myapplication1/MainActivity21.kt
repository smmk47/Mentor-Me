package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView


class MainActivity21 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var userRef: DatabaseReference? = null // Declare userRef as a class-level variable

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Adapter3
    private var reviewsList: MutableList<reviews> = mutableListOf() // Initialize reviewsList


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main21)

        auth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser

        // Reference to the Firebase Realtime Database
        userRef = currentUser?.email?.replace(".", ",")?.let {
            FirebaseDatabase.getInstance().getReference("Users").child(it)
        }

        val edittextname = findViewById<TextView>(R.id.textViewname)
        val edittextcity = findViewById<TextView>(R.id.textViewcity)
        val circleImageView: CircleImageView = findViewById(R.id.textView12)
        val iview: ImageView = findViewById(R.id.view4)


        val button5: Button = findViewById(R.id.button5)
        button5.setOnClickListener {
            val intent = Intent(this, MainActivity23::class.java)
            startActivity(intent)
        }

        val updateimage: ImageButton = findViewById(R.id.imageButton4)
        updateimage.setOnClickListener {
            // Open the image selection intent
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        val imageButton3: ImageButton = findViewById(R.id.imageButton3)
        imageButton3.setOnClickListener {
            val intent = Intent(this, MainActivity22::class.java)
            startActivity(intent)
        }

        if (userRef != null) {
            userRef!!.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userData = snapshot.getValue(User::class.java)
                    if (userData != null) {
                        // Set user information in EditText fields
                        edittextname.text = userData.name
                        edittextcity.text = userData.city

                        Glide.with(this@MainActivity21 /* context */)
                            .load(userData.imageUrl)
                            .placeholder(R.drawable.profileicon) // Placeholder image while loading
                            .error(R.drawable.profileicon) // Error image if loading fails
                            .into(circleImageView)

                        Glide.with(this@MainActivity21 /* context */)
                            .load(userData.imageUrl)
                            .placeholder(R.drawable.rec2) // Placeholder image while loading
                            .error(R.drawable.square) // Error image if loading fails
                            .into(findViewById<ImageView>(R.id.view4))



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


        recyclerView = findViewById(R.id.rrv)
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity21, LinearLayoutManager.HORIZONTAL, false)

        reviewsList = mutableListOf()
        adapter = Adapter3(reviewsList)
        recyclerView.adapter = adapter

        // Fetch reviews of the current user from Firebase Realtime Database
        fetchReviewsForCurrentUser()
        // Load user image from Firebase Storage and display it in the circular image view


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


        val button211: Button = findViewById(R.id.button21)
        button211.setOnClickListener {
            val intent = Intent(this, MainActivity21::class.java)
            startActivity(intent)
        }

    }

    // Define a constant for image pick request
    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }

    // Override onActivityResult to handle the result of the image selection
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            // Get the image URI from the data intent
            val imageUri = data?.data

            // Upload the image to Firebase Storage
            if (imageUri != null) {
                val currentUserEmail = auth.currentUser?.email?.replace(".", ",")
                val storageRef = FirebaseStorage.getInstance().reference.child("profile_images/$currentUserEmail.jpg")

                storageRef.putFile(imageUri)
                    .addOnSuccessListener { taskSnapshot ->
                        // Image uploaded successfully, get the download URL
                        storageRef.downloadUrl.addOnSuccessListener { uri ->
                            // Update the user's image URL in the database
                            userRef?.child("imageUrl")?.setValue(uri.toString())
                                ?.addOnSuccessListener {
                                    Toast.makeText(this, "Image updated successfully", Toast.LENGTH_SHORT).show()

                                    Glide.with(this)
                                        .load(uri)
                                        .placeholder(R.drawable.pro) // Placeholder image while loading
                                        .error(R.drawable.profileicon) // Error image if loading fails
                                        .into(findViewById<CircleImageView>(R.id.textView12))


                                    Glide.with(this)
                                        .load(uri)
                                        .placeholder(R.drawable.rec2) // Placeholder image while loading
                                        .error(R.drawable.square) // Error image if loading fails
                                        .into(findViewById<ImageView>(R.id.view4))


                                }
                                ?.addOnFailureListener { e ->
                                    Log.e("Firebase", "Error updating image URL: ${e.message}")
                                    Toast.makeText(this, "Error updating image", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firebase", "Error uploading image: ${e.message}")
                        Toast.makeText(this, "Error uploading image", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun fetchReviewsForCurrentUser() {
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

        // Get a reference to the Firebase database
        val database = FirebaseDatabase.getInstance()
        val reviewsRef = database.getReference("reviews")

        // Query the reviews for the current user's email
        val query: Query = reviewsRef.orderByChild("useremail").equalTo(currentUserEmail)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                reviewsList.clear() // Clear the list before adding new data
                for (snapshot in dataSnapshot.children) {
                    // Convert dataSnapshot to Review object
                    val review = snapshot.getValue(reviews::class.java)
                    review?.let {
                        reviewsList.add(it)
                    }
                }
                // Notify adapter that data set has changed
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

}
