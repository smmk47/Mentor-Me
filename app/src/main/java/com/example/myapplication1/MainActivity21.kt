package com.example.myapplication1

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.myapplication1.R.drawable
import com.example.myapplication1.R.id
import com.example.myapplication1.R.layout
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class MainActivity21 : AppCompatActivity() {
    var img: Bitmap? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Adapter3
    private var reviewsList: MutableList<reviews> = mutableListOf()
    var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main21)

        email = intent.getStringExtra("email").toString()

        loadProfileImage()
        val edittextname = findViewById<TextView>(id.textViewname)
        val edittextcity = findViewById<TextView>(id.textViewcity)
        val circleImageView: CircleImageView = findViewById(id.textView12)
        val dp: ImageView = findViewById(id.view4)

        val button5: Button = findViewById(id.button5)
        button5.setOnClickListener {
            val intent = Intent(this, MainActivity23::class.java)
            startActivity(intent)
        }

        val updateimage: ImageButton = findViewById(id.imageButton4)

        var selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            img = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            uploadImageToServer()
        }

        updateimage.setOnClickListener {
            selectImage.launch("image/*")
        }

        val imageButton3: ImageButton = findViewById(id.imageButton3)
        imageButton3.setOnClickListener {
            val intent = Intent(this, MainActivity22::class.java)
            startActivity(intent)
        }


        recyclerView = findViewById(id.rrv)
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity21, LinearLayoutManager.HORIZONTAL, false)

        reviewsList = mutableListOf()
        adapter = Adapter3(reviewsList)
        recyclerView.adapter = adapter


        fetchDataFromApi()


    }

    private fun loadProfileImage() {
        val circleImageView: CircleImageView = findViewById(id.textView12)

        val url = "http://192.168.54.54/smd/getuserpic.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    val status = jsonResponse.getString("status")
                    if (status == "success") {
                        val picuri = jsonResponse.getString("picuri")
                        Glide.with(this /* context */)
                            .load(picuri)
                            .placeholder(drawable.profileicon)
                            .error(drawable.profileicon)
                            .into(findViewById(id.textView12))


                        Picasso.get().load(url).into(circleImageView)

                    } else {
                        // User not found
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Log.e("API Error", "Error occurred: ${error.message}")
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["email"] = email.toString()
                return params
            }
        }

        Volley.newRequestQueue(this).add(stringRequest)
    }

    private fun uploadImageToServer() {
        val url = "http://192.168.54.54/smd/updatepic.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    val status = jsonResponse.getString("status")
                    if (status == "success") {
                        // Picuri updated successfully
                        loadProfileImage() // Call loadProfileImage() to update the profile image
                    } else {
                        // Error updating picuri
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Log.e("API Error", "Error occurred: ${error.message}")
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["email"] = email.toString()
                params["picuri"] = bitmapToBase64(img!!)
                return params
            }
        }

        Volley.newRequestQueue(this).add(stringRequest)
    }


    fun bitmapToBase64(dp: Bitmap): String {
        val stream = ByteArrayOutputStream()
        dp.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT).toString()
    }

    private fun fetchDataFromApi() {
        val url = "http://192.168.0.102/smd/getreview.php"

        val queue = Volley.newRequestQueue(this)


        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val bookings = mutableListOf<bookedsessions>()
                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val rev = reviews(
                        mentorname    = jsonObject.getString("mentorname"),
                        useremail = jsonObject.getString("useremail"),
                        feedback = jsonObject.getString("feedback"),
                        rating = jsonObject.getString("rating"),
                    )
                    reviewsList.add(rev)

                }
                adapter.notifyDataSetChanged()
            },
            { error ->
                Log.e("MainActivity", "Error: ${error.toString()}")
            })

        queue.add(jsonArrayRequest)
    }




}