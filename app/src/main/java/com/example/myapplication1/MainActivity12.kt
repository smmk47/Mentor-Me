package com.example.myapplication1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso


data class Mentor(
    var name: String = "",
    var designation: String = "",
    var description: String = "",
    var price: String = "",
    var status: String = "",
    var Picuri: String = ""
)

class MainActivity12 : AppCompatActivity() {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val PICK_IMAGE_REQUEST = 1

    private lateinit var selectImageButton: Button
    private lateinit var uploadButton: Button
    private lateinit var editTextName: EditText
    private lateinit var editTextDesignation: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var editTextPrice: EditText
    private lateinit var editTextStatus: EditText
    private var imageUri: Uri? = null
    private lateinit var storageReference: StorageReference
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main12)

        selectImageButton = findViewById(R.id.button)
        uploadButton = findViewById(R.id.button23)
        editTextName = findViewById(R.id.editTextText1)
        editTextDesignation = findViewById(R.id.editTextText4)
        editTextDescription = findViewById(R.id.editTextText2)
        editTextPrice = findViewById(R.id.editTextText5)
        editTextStatus = findViewById(R.id.editTextText3)
        imageView = findViewById(R.id.imageview)

       // storageReference = FirebaseStorage.getInstance().reference

        selectImageButton.setOnClickListener { openGallery() }

        val name = editTextName.text.toString()
        val designation = editTextDesignation.text.toString()
        val description = editTextDescription.text.toString()
        val price = editTextPrice.text.toString()
        val status = editTextStatus.text.toString()
        val sessionPrice = editTextPrice.text.toString()


        uploadButton.setOnClickListener {
            val x = imageUri

            uploadImage()
            insertMentor(this, name, description, status, designation, sessionPrice  )


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

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            imageUri = data.data
            Picasso.get().load(imageUri).into(imageView)

        }
    }

    private fun uploadImage() {

    }

    private fun insertMentor(context: Context, name: String, description: String, status: String, designation: String, sessionPrice: String) {
        // URL of your PHP API endpoint
        val apiurl = "http://192.168.0.102/smd/insertmentor.php"

        val stringRequest = object : StringRequest(
            Method.POST, apiurl,
            Response.Listener { response ->
                Log.d("API Response", response)
            },
            Response.ErrorListener { error ->
                Log.e("API Error", "Error occurred: ${error.message}")
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                val name1 = editTextName.text.toString()
                val designation1 = editTextDesignation.text.toString()
                val description1 = editTextDescription.text.toString()
                val price1 = editTextPrice.text.toString()
                val status1 = editTextStatus.text.toString()
                val sessionPrice1 = editTextPrice.text.toString()


                params["name"] = name1
                params["description"] = description1
                params["status"] = status1
                params["designation"] = designation1
                params["sessionprice"] = sessionPrice1
                params["imgurl"] = imageUri.toString()


                Log.d("name",name)
                Log.d("email",designation)
                Log.d("phone",description)
                Log.d("country",status)
                Log.d("password",sessionPrice)
                Log.d("imageurl", imageUri.toString())

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


}
