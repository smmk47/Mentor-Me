package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.jsoup.Jsoup

class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val loginButton: Button = findViewById(R.id.loginButton)
        val emailEditText: EditText = findViewById(R.id.email)
        val passwordEditText: EditText = findViewById(R.id.password)

        val signupButton: TextView = findViewById(R.id.textView3)


        signupButton.setOnClickListener{
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

           // val email = etEmail.text.toString()
           // val password = etPassword.text.toString()
                       //http://192.168.54.54/smda3/login.php
            val url = "http://192.168.0.102/smd/login.php"
            val stringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { response ->
                    // Handle successful response
                    Log.d("API Response", response)
                    try {
                        val htmlResponse = response.trim()
                        val parser = Jsoup.parse(htmlResponse)
                        val elements = parser.select("p")
                        val name = elements[0].text()
                        val email = elements[1].text()
                        val phone = elements[2].text()
                        val country = elements[3].text()
                        val city = elements[4].text()
                        val password = elements[5].text()
                        val picuri = elements[6].text()
                        val usertoken = elements[7].text()
                        // Use the parsed data
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, homepage::class.java)
                        intent.putExtra("name", name)
                        intent.putExtra("email", email)
                        intent.putExtra("phone", phone)
                        intent.putExtra("country", country)
                        intent.putExtra("city", city)
                        intent.putExtra("password", password)
                        intent.putExtra("picuri", picuri)
                        intent.putExtra("usertoken", usertoken)
                        startActivity(intent)
                        finish()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // Handle HTML parsing error
                        Toast.makeText(this, "HTML parsing error", Toast.LENGTH_SHORT).show()
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
                    // Add email and password to the params
                    params["email"] = email
                    params["password"] = password
                    return params
                }
            }

// Add the request to the RequestQueue
            Volley.newRequestQueue(this).add(stringRequest)






        }



    }


}
