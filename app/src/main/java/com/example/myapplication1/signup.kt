package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class SignupActivity : AppCompatActivity() {

  //  private lateinit var auth: FirebaseAuth // Declare FirebaseAuth variable
    val apiUrl = "http://172.17.10.38/smda2api/insertuser.php" // Change this to your API endpoint

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

      //  auth = FirebaseAuth.getInstance()

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
               // val signupUser = newuser(name, userEmail, phone, country, userPass)
               userSignUp(userEmail, userPass, newuser(name,userEmail,phone,country,city,userPass,"",""))
            }
        }
    }



    private fun userSignUp(email: String, password: String, signupUser: newuser) {

     submitData(signupUser)

        val intent = Intent(this, searchhome::class.java)
        startActivity(intent)
    }

    private fun submitData(signupUser: newuser) {
        // Form data

        // Data to be inserted
        val name = signupUser.name
        val email = signupUser.email
        val phone = signupUser.phone
        val country =signupUser.country
        val city = signupUser.city
        val password = signupUser.password

        val postData = "name=$name&email=$email&phone=$phone&country=$country&city=$city&password=$password"

        // Creating HTTP connection
        val url = URL(apiUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.doOutput = true

        // Sending data
        val outputStream = OutputStreamWriter(connection.outputStream)
        outputStream.write(postData)
        outputStream.flush()

        // Reading response
        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            println("Data inserted successfully.")
        } else {
            println("Failed to insert data. Response code: $responseCode")
        }

        // Closing resources
        outputStream.close()
        connection.disconnect()
    }
}
