package com.example.myapplication1




import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity3 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth // Declare FirebaseAuth variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        val sign: Button = findViewById(R.id.button2)

        sign.setOnClickListener {
            val intent = Intent(this, verifyphone::class.java)
            startActivity(intent)
        }

        val loginButton: Button = findViewById(R.id.button3)

        loginButton.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        val email = findViewById<EditText>(R.id.editTextText2)
        val password = findViewById<EditText>(R.id.editTextText9)
        val nameEditText = findViewById<EditText>(R.id.editTextText)
        val phoneEditText = findViewById<EditText>(R.id.editTextText6)
        val countryEditText = findViewById<EditText>(R.id.editTextText7)
        val cityEditText = findViewById<EditText>(R.id.editTextText8)




        val signupBtn = findViewById<Button>(R.id.button2)

        signupBtn.setOnClickListener {
            val userEmail = email.text.toString().trim()
            val userPass = password.text.toString().trim()
            val name = nameEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()
            val country = countryEditText.text.toString().trim()
            val city = cityEditText.text.toString().trim()


            if (userEmail.isEmpty() || userPass.isEmpty() || name.isEmpty() || phone.isEmpty() || country.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val signupUser = newuser(name, userEmail, phone, country,city, userPass ,"","")
                userSignUp(userEmail, userPass, signupUser)
            }



        }
    }

    private fun userSignUp(email: String, password: String, signupUser: newuser) {
        auth.createUserWithEmailAndPassword(email, password)
            //auth.currentUser.up
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    submitData(signupUser)
                    Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()
                } else {
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        Toast.makeText(this, "Email already in use", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Sign up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun submitData(signupUser: newuser) {
        Log.d("Firebase", "Submitting data to database: $signupUser")
        val database = FirebaseDatabase.getInstance().reference
        database.child("Users").child(signupUser.email.replace(".", ",")).setValue(signupUser)
            .addOnSuccessListener {
                Toast.makeText(this@MainActivity3, "User registered successfully", Toast.LENGTH_SHORT).show()
                Log.d("Firebase", "Data submitted successfully: $signupUser")

                // Fetch user data from Firebase Realtime Database
                val userRef = FirebaseDatabase.getInstance().getReference("Users").child(signupUser.email.replace(".", ","))
                userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val uname = snapshot.child("name").value.toString()
                        val intent = Intent(this@MainActivity3, homepage::class.java)
                        intent.putExtra("uname", uname) // Pass the username to homepage
                        startActivity(intent)
                        finish()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("Firebase", "Error fetching user data: ${error.message}")
                        // Finish activity if data fetch fails
                        finish()
                    }
                })

            }
            .addOnFailureListener {
                Toast.makeText(this@MainActivity3, "Failed to register user: ${it.message}", Toast.LENGTH_SHORT).show()
                Log.e("Firebase", "Failed to submit data: ${it.message}")
            }
    }

}
