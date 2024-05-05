package com.github.emlano.sportsview.logic.api

import com.github.emlano.sportsview.logic.parseAndGetReleventFields
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
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