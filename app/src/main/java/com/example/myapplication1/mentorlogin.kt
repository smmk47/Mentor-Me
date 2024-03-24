package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class mentorlogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentorlogin)

        val mentorNameEditText = findViewById<EditText>(R.id.mentorname)
        val loginButton = findViewById<Button>(R.id.button12)

        loginButton.setOnClickListener {
            val mentorName = mentorNameEditText.text.toString().trim()

            fetchMentorFromDatabase(mentorName) { mentor ->
                if (mentor != null) {
                    val intent = Intent(this, mentorhome::class.java)
                    intent.putExtra("name", mentor.name)
                    intent.putExtra("designation", mentor.designation)
                    intent.putExtra("description", mentor.description)
                    intent.putExtra("sessionprice", mentor.price)
                    intent.putExtra("status", mentor.status)
                    startActivity(intent)
                    finish() // Optional: close the current activity
                } else {
                    Toast.makeText(this, "Mentor not found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Function to fetch mentor's information from the Firebase Realtime Database
    private fun fetchMentorFromDatabase(mentorName: String, callback: (Mentor?) -> Unit) {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("mentors")

        databaseReference.orderByChild("name").equalTo(mentorName).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {
                        val mentor = snapshot.getValue(Mentor::class.java)
                        callback(mentor)
                        return
                    }
                }
                callback(null) // Mentor not found
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                callback(null)
            }
        })
    }
}
