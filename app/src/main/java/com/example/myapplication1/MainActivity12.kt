package com.example.myapplication1

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream




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

        storageReference = FirebaseStorage.getInstance().reference

        selectImageButton.setOnClickListener { openGallery() }

        uploadButton.setOnClickListener { uploadImage() }


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
        imageUri?.let { uri ->
            val fileReference = storageReference.child("images/${System.currentTimeMillis()}.jpg")
            imageView.isDrawingCacheEnabled = true
            imageView.buildDrawingCache()
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val uploadTask = fileReference.putBytes(data)
            uploadTask.addOnSuccessListener {
                // Image uploaded successfully
                Toast.makeText(this@MainActivity12, "Upload successful", Toast.LENGTH_LONG).show()

                // Get the download URL of the uploaded image
                fileReference.downloadUrl.addOnSuccessListener { imageUrl ->
                    val name = editTextName.text.toString()
                    val designation = editTextDesignation.text.toString()
                    val description = editTextDescription.text.toString()
                    val price = editTextPrice.text.toString()
                    val status = editTextStatus.text.toString()

                    // Create a Mentor object
                    val mentor = Mentor(name, designation, description, price, status, imageUrl.toString())

                    // Save mentor data to Firebase Database
                    val database = FirebaseDatabase.getInstance()
                    val myRef = database.getReference("mentors")
                    myRef.push().setValue(mentor)


                    val currentUser = auth.currentUser
                    val userEmail = currentUser?.email
                    val firebaseHelper = FirebaseHelper()
                    val notification = notifications(useremail = userEmail.toString(), notificationdata = " Your have added a  mentor    " + name.toString() )
                    firebaseHelper.addNotification(notification)

                    val myFirebaseMessagingService = MyFirebaseMessagingService()
                    myFirebaseMessagingService.generateNotification(this,"mentor_me", " Your have added a  mentor    " + name.toString())


                }.addOnFailureListener {
                    Toast.makeText(this@MainActivity12, "Failed to get image URL", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this@MainActivity12, "Upload failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } ?: run {
            Toast.makeText(this@MainActivity12, "No file selected", Toast.LENGTH_SHORT).show()
        }
    }
}
