package com.webapk.shell

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import org.json.JSONObject
import androidx.core.net.toUri
import kotlin.system.exitProcess

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // THE GHOST KILLER: Check if we were woken up just to die!
        if (intent?.getBooleanExtra("kill_shell", false) == true) {
            finishAndRemoveTask()
            exitProcess(0)
            return
        }

        if (savedInstanceState == null) {
            try {
                val jsonStr = assets.open("config.json").bufferedReader().use { it.readText() }
                val jsonObj = JSONObject(jsonStr)

                val hostPackage = jsonObj.optString("host", "marcinlowercase.a")
                val activityName = "marcinlowercase.a.MainActivity"
                val isFullBrowser = jsonObj.optBoolean("isFullBrowser", false)

                val pwaName = jsonObj.optString("name", "")
                val pwaIconUrl = jsonObj.optString("iconUrl", "")

                val intent: Intent

                if (isFullBrowser) {
                    intent = Intent()
                    intent.setClassName(hostPackage, activityName)
                    intent.putExtra("is_cloned_browser", true)
                    intent.putExtra("pwa_name", pwaName)
                    intent.putExtra("pwa_icon_url", pwaIconUrl)
                } else {
                    val url = jsonObj.getString("url")
                    intent = Intent(Intent.ACTION_VIEW, url.toUri())
                    val profileId = jsonObj.optString("profileId", "")

                    intent.putExtra("is_pwa", true)
                    intent.putExtra("pwa_name", pwaName)
                    intent.putExtra("pwa_icon_url", pwaIconUrl)

                    if (profileId.isNotEmpty()) {
                        intent.putExtra("profileId", profileId)
                    }
                    intent.setPackage(hostPackage)
                }

                startActivityForResult(intent, 1)
            } catch (e: Exception) {
                e.printStackTrace()
                finish()
            }
        }
    }

    // Catch the kill missile if the app is already running
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.getBooleanExtra("kill_shell", false) == true) {
            finishAndRemoveTask()
            exitProcess(0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        finishAndRemoveTask()
        exitProcess(0)
    }
}