package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity2 : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth // Declare FirebaseAuth variable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)





        auth = FirebaseAuth.getInstance()

        // Find the button by its ID
        val loginButton: Button = findViewById(R.id.loginButton)

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)


        loginButton.setOnClickListener {
            val userEmail = email.text.toString().trim()
            val userPass = password.text.toString().trim()

            if (userEmail.isEmpty() || userPass.isEmpty())
            {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            }
            else
            {
                userSignIn(userEmail, userPass)
            }
        }


        val forgotPasswordTextView: TextView = findViewById(R.id.textView66)
        forgotPasswordTextView.setOnClickListener {
            val intent = Intent(this, forgotpassword::class.java)
            startActivity(intent)
        }


        val signupPasswordTextView: TextView = findViewById(R.id.textView3)
        signupPasswordTextView.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }




    }

    private fun userSignIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    Toast.makeText(this, "Sign in successful", Toast.LENGTH_SHORT).show()

                    // Fetch user data from Firebase Realtime Database
                    val userRef =  user?.email?.replace(".", ",")
                            ?.let { FirebaseDatabase.getInstance().getReference("Users").child(it) }
                    userRef?.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val uname = snapshot.child("name").value.toString()
                            val intent = Intent(this@MainActivity2, homepage::class.java)
                            intent.putExtra("uname", uname) // Pass the username to homepage
                            startActivity(intent)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("Firebase", "Error fetching user data: ${error.message}")
                        }
                    })
                } else {
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }



}