package com.example.myapplication1

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.telecom.Call
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.MediaController
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.util.*

class MainActivity15 : AppCompatActivity() {

    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var editTextMessage: EditText
    private lateinit var sendButton: Button
    private lateinit var fileButton: Button
    private lateinit var imageUpload: Button
    private lateinit var voicemessage: Button
    private lateinit var camerabutton: Button
    private lateinit var callbutton: Button
    private lateinit var videocallbutton: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var messagesReference: DatabaseReference
    private lateinit var storageRef: StorageReference

    private val IMAGE_PICK_CODE = 1000
    private val FILE_PICK_CODE = 1001
    private val CAMERA_REQUEST_CODE = 1002
    private val VIDEO_REQUEST_CODE = 1003
    private val messagesList = mutableListOf<message>()
    private var selectedMessagePosition: Int = -1

    private lateinit var audioRecorder: AudioRecorder

    private val offlineMessages = mutableListOf<message>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main15)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        messagesReference = database.reference.child("messages")
        storageRef = FirebaseStorage.getInstance().reference

        audioRecorder = AudioRecorder()

        // Initialize views
        val mentorNameTextView = findViewById<TextView>(R.id.textView26)

        messageRecyclerView = findViewById(R.id.pmrv)
        editTextMessage = findViewById(R.id.messagetext)
        sendButton = findViewById(R.id.sendbutton)
        fileButton = findViewById(R.id.attach_file)
        imageUpload = findViewById(R.id.attach_image)
        voicemessage = findViewById(R.id.recordvoice)
        camerabutton = findViewById(R.id.clickphoto)
        callbutton = findViewById(R.id.makeaudiocall)
        videocallbutton = findViewById(R.id.makevideocall)

        videocallbutton.setOnClickListener {
            val intent = Intent(this, MainActivity19::class.java)
            startActivity(intent)
        }

        callbutton.setOnClickListener {
            val intent = Intent(this, MainActivity20::class.java)
            intent.putExtra("source", "Chatopen")
            startActivity(intent)
        }



        val name = intent.getStringExtra("name")
        val textViewName = findViewById<TextView>(R.id.textView38)
        textViewName.text = name
        val picUri = intent.getStringExtra("picuri")
        val userid = intent.getStringExtra("useremail")
        // Setup RecyclerView and Adapter
        messageAdapter = MessageAdapter(messagesList, object : MessageAdapter.OnMessageClickListener {
            override fun onMessageClick(position: Int) {
                selectedMessagePosition = position
                val message = messagesList[position]
                if (message.audioUrl != null) {
                    // Play audio
                    playAudioFromFirebase(message.audioUrl)
                } else {
                    // Handle other message types
                    showEditDialog(message)
                }
            }

            override fun onMessageLongClick(position: Int) {
                selectedMessagePosition = position
                showDeleteConfirmationDialog()
            }
        })

        messageRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity15)
            adapter = messageAdapter
            addItemDecoration(ItemOffsetDecoration(16)) // Adding item decoration with 16dp offset
        }

        // Send message button click listener
        sendButton.setOnClickListener {
            val messageText = editTextMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                if (name != null) {
                    sendMessage(messageText , name)
                }
            }
        }

        // File button click listener
        fileButton.setOnClickListener {
            pickFileFromDevice()
        }

        // Image upload button click listener
        imageUpload.setOnClickListener {
            pickImageFromGallery()
        }

        // Camera button click listener
        camerabutton.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openCamera()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_REQUEST_CODE
                )
            }
        }

        // Voice message button click listener
        voicemessage.setOnClickListener {
            if (audioRecorder.isRecording) {
                audioRecorder.stopRecording { audioUri ->
                    if (audioUri != null) {
                        uploadAudioToFirebase(audioUri)
                    } else {
                        // Handle audio recording failure
                        Toast.makeText(this, "Failed to record audio", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                audioRecorder.startRecording()
                Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show()
            }
        }

        // Listen for new messages
        messagesReference.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(message::class.java)
                if (message != null) {
                    if (message != null && (message.userId == userid && message.recivername == name)) {
                        if (message != null) {
                            messagesList.add(message)
                        }
                        messageAdapter.notifyItemInserted(messagesList.size - 1)
                        scrollToBottom()
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val updatedMessage = snapshot.getValue(message::class.java)
                if (updatedMessage != null) {
                    val existingMessage = messagesList.find { it.userId == updatedMessage.userId }
                    if (existingMessage != null) {
                        val index = messagesList.indexOf(existingMessage)
                        messagesList[index] = updatedMessage
                        messageAdapter.notifyItemChanged(index)
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val removedMessage = snapshot.getValue(message::class.java)
                if (removedMessage != null) {
                    val existingMessage = messagesList.find { it.userId == removedMessage.userId }
                    if (existingMessage != null) {
                        val index = messagesList.indexOf(existingMessage)
                        messagesList.removeAt(index)
                        messageAdapter.notifyItemRemoved(index)
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })

        // Check for internet connectivity and sync offline messages
        if (isNetworkAvailable()) {
            syncOfflineMessages()
        } else {
            Toast.makeText(this, "No internet connection. Offline mode.", Toast.LENGTH_SHORT).show()
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



        val button211: Button = findViewById(R.id.button211)
        button211.setOnClickListener {
            val intent = Intent(this, MainActivity21::class.java)
            startActivity(intent)
        }


    }

    private fun scrollToBottom() {
        messageRecyclerView.scrollToPosition(messageAdapter.itemCount - 1)
    }

    private fun sendMessage(messageText: String,name:String) {
        //val userId = auth.currentUser?.uid
        val userid = intent.getStringExtra("useremail")

        if (userid != null) {
            val message = message(userid, messageText, System.currentTimeMillis(), null, null, null,null,null,name)
            messagesReference.push().setValue(message)
                .addOnSuccessListener {
                    editTextMessage.text.clear()
                }
                .addOnFailureListener {
                    // Handle message sending failure
                    Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show()
                    // Add message to offline list
                    offlineMessages.add(message)
                }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun pickFileFromDevice() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, FILE_PICK_CODE)
    }

    private fun openCamera() {
        val options = arrayOf("Take Photo", "Record Video")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose an option")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
                }
                1 -> {
                    val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                    startActivityForResult(takeVideoIntent, VIDEO_REQUEST_CODE)
                }
            }
        }
        builder.show()
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    private fun recordVideo() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(intent, VIDEO_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGE_PICK_CODE -> {
                    // Handle image pick
                    data?.data?.let { imageUri ->
                        uploadImageToFirebase(imageUri)
                    }
                }
                FILE_PICK_CODE -> {
                    // Handle file pick
                    data?.data?.let { fileUri ->
                        uploadFileToFirebase(fileUri)
                    }
                }
                CAMERA_REQUEST_CODE -> {
                    // Handle image capture
                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    imageBitmap?.let {
                        val imageUri = getImageUriFromBitmap(it)
                        uploadImageToFirebase(imageUri)
                    }
                }
                VIDEO_REQUEST_CODE -> {
                    // Handle video capture
                    data?.data?.let { videoUri ->
                        uploadVideoToFirebase(videoUri)
                    }
                }
            }
        }
    }
    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            contentResolver,
            bitmap,
            "Image Title",
            null
        )
        return Uri.parse(path)
    }

    private fun uploadImageToFirebase(imageUri: Uri) {

        val userid = intent.getStringExtra("useremail")

        if (userid != null) {
            val imageRef = storageRef.child("images/${UUID.randomUUID()}")
            val uploadTask = imageRef.putFile(imageUri)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val name = intent.getStringExtra("name")
                    val userid = intent.getStringExtra("useremail")

                    val message = message(userid.toString(), "", System.currentTimeMillis(), null, downloadUri.toString(), null,null,null,name)
                    messagesReference.push().setValue(message)
                } else {
                    // Handle unsuccessful upload
                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uploadFileToFirebase(fileUri: Uri) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val fileRef = storageRef.child("files/${UUID.randomUUID()}")
            val uploadTask = fileRef.putFile(fileUri)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                fileRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val name = intent.getStringExtra("name")
                    val userid = intent.getStringExtra("useremail")

                    val message = message(userid.toString(), "", System.currentTimeMillis(), null, null, downloadUri.toString(),null,null,name)
                    messagesReference.push().setValue(message)
                } else {
                    // Handle unsuccessful upload
                    Toast.makeText(this, "Failed to upload file", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uploadVideoToFirebase(videoUri: Uri) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val videoRef = storageRef.child("videos/${UUID.randomUUID()}")
            val uploadTask = videoRef.putFile(videoUri)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                videoRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val name = intent.getStringExtra("name")
                    val userid = intent.getStringExtra("useremail")

                    val message = message(userid.toString(), "", System.currentTimeMillis(), null, null, downloadUri.toString(),null,null,name)
                    messagesReference.push().setValue(message)
                } else {
                    // Handle unsuccessful upload
                    Toast.makeText(this, "Failed to upload video", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun uploadAudioToFirebase(audioUri: Uri) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val audioRef = storageRef.child("audio/${UUID.randomUUID()}")
            val uploadTask = audioRef.putFile(audioUri)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                audioRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result

                    val name = intent.getStringExtra("name")
                    val userid = intent.getStringExtra("useremail")

                    val message = message(userid.toString(), "", System.currentTimeMillis(), downloadUri.toString(), null, null,null,null,name)
                    messagesReference.push().setValue(message)
                    Log.d("AudioUpload", "Audio uploaded successfully: $downloadUri")
                } else {
                    // Handle unsuccessful upload
                    Toast.makeText(this, "Failed to upload audio", Toast.LENGTH_SHORT).show()
                    Log.e("AudioUpload", "Failed to upload audio")
                }
            }
        }
    }

    private fun playAudioFromFirebase(audioUrl: String) {
        Log.d("AudioPlayback", "Starting audio playback for: $audioUrl")

        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(audioUrl)

        mediaPlayer.setOnPreparedListener { mp ->
            Log.d("AudioPlayback", "Audio prepared. Starting playback.")
            mp.start()
        }

        mediaPlayer.setOnCompletionListener { mp ->
            Log.d("AudioPlayback", "Audio playback completed.")
            mp.release()
        }

        mediaPlayer.setOnErrorListener { mp, what, extra ->
            Log.e("AudioPlayback", "MediaPlayer error: what=$what, extra=$extra")
            false
        }

        try {
            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            Log.e("AudioPlayback", "Failed to prepare MediaPlayer: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun playVideoFromFirebase(videoUrl: String) {
        Log.d("VideoPlayback", "Starting video playback for: $videoUrl")

        val videoUri = Uri.parse(videoUrl)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(messageRecyclerView)

        val videoView = VideoView(this)
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(videoUri)
        videoView.requestFocus()

        val dialog = AlertDialog.Builder(this)
            .setView(videoView)
            .setPositiveButton("Close") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()

        videoView.setOnPreparedListener { mp ->
            Log.d("VideoPlayback", "Video prepared. Starting playback.")
            mp.start()
        }

        videoView.setOnCompletionListener { mp ->
            Log.d("VideoPlayback", "Video playback completed.")
            mp.release()
        }

        videoView.setOnErrorListener { _, what, extra ->
            Log.e("VideoPlayback", "MediaPlayer error: what=$what, extra=$extra")
            false
        }
    }


    private fun showEditDialog(message: message) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit Message")

        val input = EditText(this)
        input.setText(message.messageText)
        builder.setView(input)

        builder.setPositiveButton("Save") { dialog, which ->
            val editedMessage = input.text.toString().trim()
            if (editedMessage.isNotEmpty()) {
                updateMessageInFirebase(message, editedMessage)
            } else {
                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

        builder.show()
    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Message")
            .setMessage("Are you sure you want to delete this message?")
            .setPositiveButton("Delete") { dialog, which ->
                val message = messagesList[selectedMessagePosition]
                deleteMessageFromFirebase(message)
                selectedMessagePosition = -1
            }
            .setNegativeButton("Cancel") { dialog, which ->
                selectedMessagePosition = -1
                dialog.cancel()
            }
            .show()
    }

    private fun updateMessageInFirebase(oldMessage: message, newMessageText: String) {
        val messageRef = messagesReference.child(oldMessage.userId ?: "")
        val updatedMessage = oldMessage.copy(messageText = newMessageText)
        messageRef.setValue(updatedMessage)
            .addOnSuccessListener {
                // Update local list and notify adapter
                val index = messagesList.indexOf(oldMessage)
                if (index != -1) {
                    messagesList[index] = updatedMessage
                    messageAdapter.notifyItemChanged(index)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update message", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteMessageFromFirebase(message: message) {
        val messageRef = messagesReference.child(message.userId ?: "")
        messageRef.removeValue()
            .addOnSuccessListener {
                // Remove from local list and notify adapter
                val index = messagesList.indexOf(message)
                if (index != -1) {
                    messagesList.removeAt(index)
                    messageAdapter.notifyItemRemoved(index)
                } else {
                    // Message not found in the list
                    // Handle this case if needed
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to delete message", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        audioRecorder.cancelRecording()
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            return networkCapabilities != null &&
                    (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }

    private fun syncOfflineMessages() {
        for (message in offlineMessages) {
            sendMessage(message.messageText,"")
        }
        offlineMessages.clear()
    }
}


//package com.example.myapplication1
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.widget.Button
//import android.widget.TextView
//import android.widget.Toast
//
//class MainActivity15 : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main15)
//
//
//        val name = intent.getStringExtra("name")
//        val picUri = intent.getStringExtra("picuri")
//        val emails = intent.getStringExtra("useremail")
//
//        val textViewName = findViewById<TextView>(R.id.textView38)
//        textViewName.text = name
//
//
//
//        val clickPhotoButton = findViewById<Button>(R.id.clickphoto)
//
//        // Set OnClickListener for the button
//        clickPhotoButton.setOnClickListener {
//            Toast.makeText(applicationContext, "Button Clicked", Toast.LENGTH_SHORT).show()
//
//            // Create an Intent to navigate to MainActivity17
//            val intent = Intent(this, MainActivity17::class.java)
//            startActivity(intent)
//        }
//
//        val Videocall = findViewById<Button>(R.id.makevideocall)
//
//        // Set OnClickListener for the button
//        Videocall.setOnClickListener {
//            Toast.makeText(applicationContext, "Button Clicked", Toast.LENGTH_SHORT).show()
//
//            // Create an Intent to navigate to MainActivity17
//            val intent = Intent(this, MainActivity19::class.java)
//            startActivity(intent)
//        }
//
//        val Audiocall = findViewById<Button>(R.id.makeaudiocall)
//
//        // Set OnClickListener for the button
//        Audiocall.setOnClickListener {
//            Toast.makeText(applicationContext, "Button Clicked", Toast.LENGTH_SHORT).show()
//
//            // Create an Intent to navigate to MainActivity17
//            val intent = Intent(this, MainActivity20::class.java)
//            startActivity(intent)
//        }
//
//    }
//
//
//}