package com.example.myapplication1

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.myapplication1.R.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat

class MainActivity13 : AppCompatActivity() {
    private var currentUser: String = ""

    private lateinit var mentorEmail: String

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main13)

        // Declare val variables for buttons
        var buttont1: Button = findViewById(R.id.button1)
        var buttont2: Button = findViewById(R.id.button2)
        var buttont3: Button = findViewById(R.id.button3)
        var buttont4: Button = findViewById(R.id.button4)
        var buttont5: Button = findViewById(R.id.button5)

        var selectedTime: String = ""

        val name = intent.getStringExtra("name")
        val designation = intent.getStringExtra("designation")
        val description = intent.getStringExtra("description")
        val price = intent.getStringExtra("sessionprice")
        val status = intent.getStringExtra("status")
        val picuri = intent.getStringExtra("picuri")
        val circleImageView: CircleImageView = findViewById(R.id.profile)


        //val storedEmail = sharedPref.getString("email", "")


        // Find TextViews in your layout
        val textViewName = findViewById<TextView>(R.id.textView38)
        //val textViewDesignation = findViewById<TextView>(R.id.textView39)
        //val textViewDescription = findViewById<TextView>(R.id.textView41)
        val textViewPrice = findViewById<TextView>(R.id.textView46)
        //val textViewStatus = findViewById<TextView>(R.id.textViewStatus)

        // Set values to TextViews
        textViewName.text = name
        //textViewDesignation.text = designation
        //textViewDescription.text = description
        textViewPrice.text = price
        //textViewStatus.text = status

        if (picuri != null) {

            Glide.with(this@MainActivity13 /* context */)
                .load(picuri)
                .placeholder(R.drawable.profileicon) // Placeholder image while loading
                .error(R.drawable.profileicon) // Error image if loading fails
                .into(circleImageView)

        } else {
            // Handle if user data is null
            Toast.makeText(applicationContext, "image not found", Toast.LENGTH_SHORT).show()
        }


        val selectedDate = Calendar.getInstance()
        val year = 0
        val month = 0
        val dayOfMonth = 0
        selectedDate.set(year, month, dayOfMonth)

        // Format the selected date as a string
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        val dateString = dateFormat.format(selectedDate.time)


        val Button7: Button = findViewById(id.button7)

        // Set OnClickListener to handle the button click event
        Button7.setOnClickListener {
            // Create an Intent to navigate to the SignupActivity
            val intent = Intent(this, homepage::class.java)

            // Start the SignupActivity
            startActivity(intent)
        }


        val Button5: ImageButton = findViewById(id.msgbut)

        // Set OnClickListener to handle the button click event
        Button5.setOnClickListener {
            // Create an Intent to navigate to the SignupActivity
            val intent = Intent(this, MainActivity15::class.java)

            // Start the SignupActivity
            startActivity(intent)
        }


        val Button4: ImageButton = findViewById(id.vidcall)

        // Set OnClickListener to handle the button click event
        Button4.setOnClickListener {
            // Create an Intent to navigate to the SignupActivity
            val intent = Intent(this, MainActivity19::class.java)

            // Start the SignupActivity
            startActivity(intent)
        }

        val firebaseHelper = FirebaseHelper()
        val notification = notifications(useremail = currentUser, notificationdata = "Your Session booked successfully with  "  )
        firebaseHelper.addNotification(notification)


        val myFirebaseMessagingService = MyFirebaseMessagingService()
        myFirebaseMessagingService.generateNotification(this,"mentor_me", "Your Session booked successfully with  "   )



        val Button3: ImageButton = findViewById(id.audcall)

        // Set OnClickListener to handle the button click event
        Button3.setOnClickListener {
            // Create an Intent to navigate to the SignupActivity
            val intent = Intent(this, MainActivity20::class.java)

            // Start the SignupActivity
            startActivity(intent)
        }



        buttont1.setOnClickListener {
            // Update the selectedTime variable when button1 is clicked
            selectedTime = "10:00 AM"
        }

        buttont2.setOnClickListener {
            // Update the selectedTime variable when button2 is clicked
            selectedTime = "11:00 AM"
        }

        buttont3.setOnClickListener {
            // Update the selectedTime variable when button3 is clicked
            selectedTime = "12:00 PM"
        }

        buttont4.setOnClickListener {
            // Update the selectedTime variable when button4 is clicked
            selectedTime = "1:00 PM"
        }

        buttont5.setOnClickListener {
            // Update the selectedTime variable when button5 is clicked
            selectedTime = "2:00 PM"
        }



        val booksession: Button = findViewById<Button>(R.id.button7)
        booksession.setOnClickListener {
            // Retrieve data from the intent first
            val name = intent.getStringExtra("name") ?: ""
            val designation = intent.getStringExtra("designation") ?: ""
            val description = intent.getStringExtra("description") // If you need to use this
            val price = intent.getStringExtra("sessionprice") // If you need to use this
            val status = intent.getStringExtra("status") // If you need to use this
            val picuri = intent.getStringExtra("picuri")


            val datetime: String = dateFormat.toString() + selectedTime.toString()


            // Define the URL of your API endpoint
            val url = "http://192.168.0.102/smd/insertbooking.php"


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


                    val name = intent.getStringExtra("name") ?: ""
                    val designation = intent.getStringExtra("designation") ?: ""
                    val description = intent.getStringExtra("description") // If you need to use this
                    val price = intent.getStringExtra("sessionprice") // If you need to use this
                    val status = intent.getStringExtra("status") // If you need to use this
                    val picuri = intent.getStringExtra("picuri")



                    params["useremail"] = name
                    params["mentorname"] = name
                    params["mentordesignation"] = designation
                    params["datetime"] = datetime.toString()
                    params["picuri"] = picuri.toString()


                    Log.d("name",name)
                    Log.d("email",designation)
                    Log.d("phone",designation)
                    Log.d("country",datetime.toString())


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



        val currentUser = auth.currentUser
        val userEmail = currentUser?.email ?: ""
        val mentorName = intent.getStringExtra("name") ?: ""

        val msgButton: ImageButton = findViewById<ImageButton>(R.id.msgbut)

        msgButton.setOnClickListener {
            // Check if the data already exists for this mentor
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()

            val mentorRef = database.getReference("allmessages").child(mentorName)
            mentorRef.orderByChild("emails").equalTo(userEmail).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Data already exists, navigate to MainActivity16
                        val intent = Intent(this@MainActivity13, MainActivity16::class.java)
                        startActivity(intent)
                    } else {
                        // Data doesn't exist, create new entry
                        picuri?.let { uri ->
                            val newMessage = allmessages(userEmail, mentorName, uri, "")
                            mentorRef.push().setValue(newMessage)
                                .addOnSuccessListener {
                                    // Data successfully added, navigate to MainActivity14
                                    val intent = Intent(this@MainActivity13, MainActivity14::class.java)
                                    startActivity(intent)
                                }
                                .addOnFailureListener { e ->
                                    // Handle failure to add data
                                    Toast.makeText(this@MainActivity13, "Failed to add data: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        } ?: run {
                            Toast.makeText(this@MainActivity13, "Image URI is null", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                    Toast.makeText(this@MainActivity13, "Database error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }





    }
}






