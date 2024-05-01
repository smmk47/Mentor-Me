
package com.example.myapplication1

import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging

class homepage<Query : Any> : AppCompatActivity(), Adapter1.OnItemClickListener  {

    private lateinit var screenshotObserver: ScreenshotObserver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        fetchMentors(this)

//        Handler().postDelayed({
//            // Intent to start the LoginPage activity
//            val loginIntent = Intent(this, searchhome::class.java)
//            startActivity(loginIntent)
//            finish() // Destroy this activity so the user can't return to it
//        }, 2500) // 2500 milliseconds is 2.5 seconds






        fun onItemClick(mentor: newmentor) {
            // Handle item click, start MainActivity12 with mentor information
            val intent = Intent(this, MainActivity12::class.java).apply {
                intent.putExtra("name", mentor.name)
                intent.putExtra("designation", mentor.designation)
                intent.putExtra("description", mentor.description)
                intent.putExtra("sessionprice", mentor.price)
                intent.putExtra("status", mentor.status)


            }
            startActivity(intent)
        }


        val userName = intent.getStringExtra("name")
        // Retrieve user information from extras
        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        val phone = intent.getStringExtra("phone")
        val country = intent.getStringExtra("country")
        val city = intent.getStringExtra("city")
        val picUri = intent.getStringExtra("picUri")
        val userToken = intent.getStringExtra("userToken")
        val password = intent.getStringExtra("password")




        // Display user's name in a TextView
        val textView22: TextView = findViewById(R.id.textView22)
        textView22.text = "$userName!"

        val button17: Button = findViewById(R.id.button17)

        button17.setOnClickListener {
            val myFirebaseMessagingService = MyFirebaseMessagingService()
            myFirebaseMessagingService.generateNotification(this,"mentor_me", "you added mentor successfully")


            intent.putExtra("name", name)
            intent.putExtra("city", city)
            intent.putExtra("country", country)
            intent.putExtra("email", email)
            intent.putExtra("password", password)

            //  val intent = Intent(this, homepage::class.java)
            //  startActivity(intent)
        }


        val button18: Button = findViewById(R.id.button18)

        button18.setOnClickListener {
            val intent = Intent(this, searchhome::class.java)
            intent.putExtra("name", name)
            intent.putExtra("city", city)
            intent.putExtra("country", country)
            intent.putExtra("email", email)
            intent.putExtra("password", password)
            startActivity(intent)
        }


        val button22: Button = findViewById(R.id.button22)

        button22.setOnClickListener {
            val intent = Intent(this, MainActivity12::class.java)

            intent.putExtra("name", name)
            intent.putExtra("city", city)
            intent.putExtra("country", country)
            intent.putExtra("email", email)
            intent.putExtra("password", password)

            startActivity(intent)
        }
        val button20: Button = findViewById(R.id.button20)
        button20.setOnClickListener {
            val intent = Intent(this, MainActivity14::class.java)

            intent.putExtra("name", name)
            intent.putExtra("city", city)
            intent.putExtra("country", country)
            intent.putExtra("email", email)
            intent.putExtra("password", password)

            startActivity(intent)
        }


        val button211: Button = findViewById(R.id.button211)
        button211.setOnClickListener {
            val intent = Intent(this, MainActivity21::class.java)

            intent.putExtra("name", name)
            intent.putExtra("city", city)
            intent.putExtra("country", country)
            intent.putExtra("email", email)
            intent.putExtra("password", password)

            startActivity(intent)
        }







        val button16: Button = findViewById(R.id.button16)
        button16.setOnClickListener {
            val intent = Intent(this, MainActivity24::class.java)
            startActivity(intent)
        }





        // val viewCardBottom1: View = findViewById(R.id.rv.viewCardBottom1)

        //viewCardBottom1.setOnClickListener {
        //    val intent = Intent(this, Main10Activity::class.java)
        //    startActivity(intent)
        // }


        getFCMToken()

    }


    fun getFCMToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    Log.i("MyToken", token)
                    // Update the FCM token for the current user in Firestore
                    Toast.makeText(applicationContext, "FCM token 11", Toast.LENGTH_SHORT).show()

                    updateFCMTokenInFirestore(token)
                } else {
                    Log.e("MyToken", "Fetching FCM token failed", task.exception)
                }
            }
    }



    private fun updateFCMTokenInFirestore(token: String?) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val userEmail = user.email
            userEmail?.let { email ->
                val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                val userQuery: com.google.firebase.database.Query = databaseReference.orderByChild("email").equalTo(email)

                userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (snapshot in dataSnapshot.children) {
                            val userKey = snapshot.key
                            userKey?.let {
                                val tokenMap = HashMap<String, Any>()
                                tokenMap["usertoken"] = token ?: ""
                                databaseReference.child(it).updateChildren(tokenMap)
                                    .addOnSuccessListener {
                                        Toast.makeText(applicationContext, "FCM token updated successfully", Toast.LENGTH_SHORT).show()
                                        Log.i("FCMTokenUpdate", "FCM token updated successfully in Realtime Database")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("FCMTokenUpdate", "Error updating FCM token in Realtime Database", e)
                                    }
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e("FCMTokenUpdate", "Database Error: ${databaseError.message}")
                    }
                })
            }
        }
    }


    override fun onItemClick(mentor: newmentor) {
        // Handle item click, start MainActivity12 with mentor information
        val intent = Intent(this, Main10Activity::class.java).apply {
            putExtra("name", mentor.name)
            putExtra("designation", mentor.designation)
            putExtra("description", mentor.description)
            putExtra("sessionprice", mentor.price)
            putExtra("status", mentor.status)
            putExtra("picuri", mentor.picuri)

        }
        startActivity(intent)
    }


    fun detectScreenshot(context: Context) {
        val mediaProjectionManager = context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

        val contentObserver = object : ContentObserver(null) {
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                Toast.makeText(context, "Screenshot captured!", Toast.LENGTH_SHORT).show()
            }
        }

        val contentResolver = context.contentResolver
        contentResolver.registerContentObserver(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, contentObserver)
    }



    private fun fetchMentors(context: Context) {
        val url = "http://192.168.0.102/smd/getmentors.php"
        val mentorsList = mutableListOf<newmentor>()

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                for (i in 0 until response.length()) {
                    val mentorObj = response.getJSONObject(i)
                    val mentor = newmentor(
                        mentorObj.getString("name"),
                        mentorObj.getString("description"),
                        mentorObj.getString("status"),
                        mentorObj.getString("designation"),
                        mentorObj.getString("sessionprice")
                    )
                    mentorsList.add(mentor)
                }


                val adapter=Adapter1(mentorsList,this@homepage)
                adapter.setOnItemClickListener(this@homepage)
                val rv = findViewById<RecyclerView>(R.id.rv)
                val layoutManager = LinearLayoutManager(this@homepage, LinearLayoutManager.HORIZONTAL, false)
                rv.layoutManager = layoutManager
                rv.adapter = adapter

            },
            { error ->
                error.printStackTrace()
                // Handle error response
            }
        )
        Volley.newRequestQueue(context).add(request)
    }




}






