package com.example.myapplication1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import android.content.pm.PackageManager
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication1.databinding.ActivityMain20Binding
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig

class MainActivity20 : AppCompatActivity() {
    private val appId = "0b42460d6f16463389fe44b67d8dd894"
    private val appCertificate = "a929d04269ed40c4970b9ba03f7fffa3"
    // private val expirationTimeInSeconds = 3600
    private val channelName = "SMDACVC"
    private var token: String? = "007eJxTYNj0qEHWtV1scnbpnumf2oVXW0yfk8V5Keb+fsOfYrwbJHkUGAySTIxMzAxSzNIMzUzMjI0tLNNSTUySzMxTLFJSLCxN5CR+pjYEMjKcvZHIyMgAgSA+O0Owr4ujc5gzAwMAXFEerQ=="
    private val uid = 0
    private var isJoined = false
    private var agoraEngine: RtcEngine? = null
    private val PERMISSION_REQ_ID = 22
    private val REQUESTED_PERMISSIONS = arrayOf(
        android.Manifest.permission.RECORD_AUDIO
    )

    private lateinit var binding: ActivityMain20Binding

    private val mRtcEventHandler: IRtcEngineEventHandler = object : IRtcEngineEventHandler() {
        override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
            isJoined = true
            showMessage("Joined Channel $channel")
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            showMessage("Remote user offline $uid $reason")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain20Binding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!checkSelfPermission()) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, PERMISSION_REQ_ID);
        }
        setupAudioSDKEngine()
    }

    override fun onDestroy() {
        super.onDestroy()
        agoraEngine?.leaveChannel()
        RtcEngine.destroy()
        agoraEngine = null
    }

    private fun checkSelfPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, REQUESTED_PERMISSIONS[0]) == PackageManager.PERMISSION_GRANTED
    }

    fun showMessage(message: String?) {
        runOnUiThread {
            Toast.makeText(
                applicationContext,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupAudioSDKEngine() {
        try {
            val config = RtcEngineConfig()
            config.mContext = baseContext
            config.mAppId = appId
            config.mEventHandler = mRtcEventHandler
            agoraEngine = RtcEngine.create(config)
            agoraEngine?.enableAudio()
        } catch (e: Exception) {
            showMessage(e.toString())
        }
    }

    fun joinChannel(view: View) {
        if (checkSelfPermission()) {
            agoraEngine?.joinChannel(token, channelName, null, uid)
            Toast.makeText(applicationContext, "  Waiting for other to join a channel", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(applicationContext, "Permissions for audio recording not granted", Toast.LENGTH_SHORT).show()
        }
    }

    fun leaveChannel(view: View) {
        if (!isJoined) {
            showMessage("Join a channel first")
        } else {
            agoraEngine?.leaveChannel()
            showMessage("You left the channel")
            isJoined = false
        }
    }
}