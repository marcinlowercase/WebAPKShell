package com.webapk.shell

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import org.json.JSONObject

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            // 1. Read URL, Host Package, and Profile ID from assets/config.json
            val jsonStr = assets.open("config.json").bufferedReader().use { it.readText() }
            val jsonObj = JSONObject(jsonStr)

            val url = jsonObj.getString("url")
            val hostPackage = jsonObj.optString("host", "marcinlowercase.a")
            val profileId = jsonObj.optString("profileId", "") // <-- NEW

            // 2. Launch the Host Browser App via Intent!
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.putExtra("is_pwa", true)

            // Pass the Profile ID if it exists
            if (profileId.isNotEmpty()) {
                intent.putExtra("profileId", profileId)
            }

            intent.setPackage(hostPackage)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Close the shell immediately
        finish()
    }
}