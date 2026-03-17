package com.webapk.app0000

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import org.json.JSONObject
import androidx.core.net.toUri

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            // 1. Read URL and Host Package from assets/config.json
            val jsonStr = assets.open("config.json").bufferedReader().use { it.readText() }
            val jsonObj = JSONObject(jsonStr)

            val url = jsonObj.getString("url")
            // Provide a fallback just in case, but it will use the dynamic one
            val hostPackage = jsonObj.optString("host", "marcinlowercase.a")

            // 2. Launch the Host Browser App via Intent!
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            intent.putExtra("is_pwa", true)
            intent.setPackage(hostPackage) // <--- DYNAMIC PACKAGE NAME HERE
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Close the shell immediately
        finish()
    }
}