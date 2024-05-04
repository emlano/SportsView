package com.github.emlano.sportsview.logic.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

suspend fun fetchAndStoreLeagues(): String {
    val urlString = "https://www.thesportsdb.com/api/v1/json/3/all_leagues.php"
    val url = URL(urlString)
    val jsonStr = StringBuilder()

    withContext(Dispatchers.IO) {
        val con = url.openConnection() as HttpURLConnection
        val reader = BufferedReader(InputStreamReader(con.inputStream))
        var line: String? = reader.readLine()

        while (line != null) {
            jsonStr.append(line)
            line = reader.readLine()
        }
    }

    return jsonStr.toString()
}