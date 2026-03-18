package com.webapk.shell

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import org.json.JSONObject
import androidx.core.net.toUri

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Only launch the Browser if this is the very first time the task is created!
        // If the OS restores this background app from memory later, we don't want to duplicate the window.
        if (savedInstanceState == null) {
            try {
                val jsonStr = assets.open("config.json").bufferedReader().use { it.readText() }
                val jsonObj = JSONObject(jsonStr)

                val url = jsonObj.getString("url")
                val hostPackage = jsonObj.optString("host", "marcinlowercase.a")
                val profileId = jsonObj.optString("profileId", "")

                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                intent.putExtra("is_pwa", true)

                if (profileId.isNotEmpty()) {
                    intent.putExtra("profileId", profileId)
                }

                intent.setPackage(hostPackage)

                // Launch the Browser directly on top of this Shell App
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                finish() // Only close if something crashed
            }
        }

        // --- THE MULTI-WINDOW & RECENTS NAME FIX ---
        // Notice we DO NOT call finish() here!
        // By keeping this invisible Shell App alive at the bottom of the stack,
        // the Android OS natively considers the Shell App to be the "owner" of this window.
        // It will permanently use the PWA's true Name and Icon in the Recent Apps screen automatically!
    }
}