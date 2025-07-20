package org.schabi.newpipe.util

import android.content.Context
import android.widget.Toast
import androidx.preference.PreferenceManager
import org.json.JSONException
import org.json.JSONObject

fun isStreamAllowed(context: Context?, uploaderUrl: String): Boolean {
    try {
        context?.let {
            val defaultSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context)
            val whitelistChannelIdsString: String = defaultSharedPreferences
                .getString("whitelist_channel_ids", "{\"whitelist_channel_ids\":[]}")!!
            val jsonObject = JSONObject(whitelistChannelIdsString)
            val whitelistChannelIdsArray = jsonObject
                .getJSONArray("whitelist_channel_ids")
            var i = 0
            val size = whitelistChannelIdsArray.length()
            while (i < size) {
                val whitelistChannelInfo = whitelistChannelIdsArray.getJSONObject(i)
                val whitelistChannelId = whitelistChannelInfo.getString("channel_id")
                if (uploaderUrl.contains(whitelistChannelId)) {
                    return true
                }
                i++
            }
            Toast.makeText(
                context,
                "You are not allowed to view this content.",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return false
    } catch (e: JSONException) {
        throw RuntimeException(e)
    }
}
