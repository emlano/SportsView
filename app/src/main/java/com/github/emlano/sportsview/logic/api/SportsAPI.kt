package com.github.emlano.sportsview.logic.api

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Picture
import android.provider.MediaStore.Images
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.github.emlano.sportsview.logic.parseAndGetReleventFields
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

suspend fun fetchAndStoreLeagues(): String {
    val urlString = "https://www.thesportsdb.com/api/v1/json/3/all_leagues.php"
    return fetchDataFromAPI(urlString)
}

suspend fun fetchTeamsFromLeague(league: String): String {
    val urlString = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=" + league.replace(" ", "_")
    val jsonStr = fetchDataFromAPI(urlString)

    return parseAndGetReleventFields(JSONObject(jsonStr))
}

suspend fun fetchDataFromAPI(urlString: String): String {
    val url = URL(urlString)
    val jsonStr = StringBuilder()

    withContext(Dispatchers.IO) {
        val con = url.openConnection()
        val reader = BufferedReader(InputStreamReader(con.getInputStream()))
        var line: String? = reader.readLine()

        while (line != null) {
            jsonStr.append(line)
            line = reader.readLine()
        }
    }
    return jsonStr.toString()
}

fun fetchImgFromUrl(urlStr: String): ImageBitmap {
    var bitmap: Bitmap? = null
    val url = URL(urlStr)

    val con = url.openConnection()
    val bitstream = BufferedInputStream(con.getInputStream())

    bitmap = BitmapFactory.decodeStream(bitstream)
    return bitmap.asImageBitmap()
}