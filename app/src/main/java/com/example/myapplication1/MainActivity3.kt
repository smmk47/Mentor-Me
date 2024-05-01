package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class MainActivity3 : AppCompatActivity() {

    // API URL. Ensure this is the correct endpoint.
    private val apiUrl = "http://192.168.54.54/smd/insertuser.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val emailEditText: EditText = findViewById(R.id.editTextText2)
        val passwordEditText: EditText = findViewById(R.id.editTextText9)
        val nameEditText: EditText = findViewById(R.id.editTextText)
        val phoneEditText: EditText = findViewById(R.id.editTextText6)
        val countryEditText: EditText = findViewById(R.id.editTextText7)
        val cityEditText: EditText = findViewById(R.id.editTextText8)
        val signupBtn: Button = findViewById(R.id.button2)
        val loginButton: Button = findViewById(R.id.button3)

        signupBtn.setOnClickListener {
            val userEmail = emailEditText.text.toString().trim()
            val userPass = passwordEditText.text.toString().trim()
            val name = nameEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()
            val country = countryEditText.text.toString().trim()
            val city = cityEditText.text.toString().trim()

            if (userEmail.isEmpty() || userPass.isEmpty() || name.isEmpty() || phone.isEmpty() || country.isEmpty() || city.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val signupUser = newuser(name, userEmail, phone, country, city, userPass, "", "")
                userSignUp(emailEditText.text.toString(), passwordEditText.text.toString(), signupUser)
            }
        }

        loginButton.setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }
    }

    private fun userSignUp(email: String, password: String, signupUser: newuser) {
        submitData(signupUser)
    }

    private fun submitData(signupUser: newuser) {


        val name = signupUser.name
        val email =signupUser.email
        val phone = signupUser.phone
        val city = signupUser.city
        val country = signupUser.country
        val password = signupUser.password

        val url = "http://172.17.9.53/smda3/login.php" // Update the URL to your signup endpoint
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                // Handle successful response
                Log.d("API Response", response)
                // Parse the response JSON if necessary
                try {
                    val jsonResponse = JSONObject(response)
                    val status = jsonResponse.getString("status")
                    val message = jsonResponse.getString("message")
                    if (status == "success") {
                        // Handle successful registration, e.g., navigate to another activity
                    } else {
                        // Handle unsuccessful registration, e.g., display an error message
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    // Handle JSON parsing error
                }
            },
            Response.ErrorListener { error ->
                // Handle error
                Log.e("API Error", "Error occurred: ${error.message}")
                // Display an error message to the user
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                // Add user data to the params
                params["name"] = name
                params["email"] = email
                params["phone"] = phone
                params["city"] = city
                params["country"] = country
                params["password"] = password
                return params
            }
        }

// Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest)

    }
}

