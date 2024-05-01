package com.example.myapplication1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class searchhome : AppCompatActivity() {
   // private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchhome)


//        Handler().postDelayed({
//            // Intent to start the LoginPage activity
//            val loginIntent = Intent(this, searchresult::class.java)
//            startActivity(loginIntent)
//            finish() // Destroy this activity so the user can't return to it
//        }, 2500) // 2500 milliseconds is 2.5 seconds





      //  auth = FirebaseAuth.getInstance()
       // val currentUser: FirebaseUser? = auth.currentUser

        val searchBar :TextView= findViewById(R.id.searchbar)
        val search: TextView = findViewById(R.id.searchbutton)
        search.setOnClickListener {
            val intent = Intent(this, MainActivity9::class.java)

            // Get the text from the search bar
            val searchText = searchBar.text.toString()

          //  auth = FirebaseAuth.getInstance()

         //   val currentUserEmail = auth.currentUser?.email?.replace(".", ",")

            // Assuming you have a variable to store the current logged-in user
            // This function should return the current user

            // Put the data into the intent
            intent.putExtra("searchText", searchText)
            intent.putExtra("currentUser", "currentUserEmail")

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


        val button211: Button = findViewById(R.id.button21)
        button211.setOnClickListener {
            val intent = Intent(this, MainActivity21::class.java)
            startActivity(intent)
        }

    }
}