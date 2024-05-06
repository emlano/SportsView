package com.github.emlano.sportsview.logic

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.github.emlano.sportsview.logic.entity.League
import com.github.emlano.sportsview.logic.entity.Team
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.net.URL

fun parseJsonLeagues(json: String): List<League> {
    val jsonRoot = JSONObject(json)
    val jsonArray = jsonRoot.getJSONArray("leagues")
    val leagueList = mutableListOf<League>()

    for (i in 0..<jsonArray.length()) {
        val league = jsonArray.get(i) as JSONObject

        val leagueId = league.getInt("idLeague")
        val leagueName = league.getString("strLeague")
        val leagueSport = league.getString("strSport")
        val leagueAltName = league.getString("strLeagueAlternate")

        val leagueObj = League(leagueId, leagueName, leagueSport, leagueAltName)
        leagueList.add(leagueObj)
    }

    return leagueList.filter { it.sport == "Soccer" }.toList()
}

fun parseAndGetReleventFields(root: JSONObject): String {
    if (root.getString("teams") == "null") return "null"
    val jsonArray = root.getJSONArray("teams")
    val outputJSONArray = JSONArray()
    val fieldList = listOf(
        "idTeam", "strTeam", "strTeamShort", "strAlternate", "intFormedYear",
        "strLeague", "idLeague", "strStadium", "strKeywords", "strStadiumThumb",
        "strStadiumLocation", "intStadiumCapacity", "strWebsite", "strTeamJersey", "strTeamLogo"
    )

    for (i in 0..<jsonArray.length()) {
        val jsonObj = JSONObject()
        val indexObj = jsonArray.get(i) as JSONObject

        for (j in fieldList) {
            jsonObj.put(j, indexObj.getString(j))
        }

        outputJSONArray.put(jsonObj)
    }

    return outputJSONArray.toString(2)
}

fun parseJsonToOutputString(jsonStr: String): String {
    if (jsonStr == "null") return "No clubs returned of such league!"
    val jsonArr = JSONArray(jsonStr)

    val sb = StringBuilder()
    for (i in 0..<jsonArr.length()) {
        val j = jsonArr.get(i) as JSONObject
        sb.append(j.toString(2))
        sb.append("\n")
    }

    return sb.toString().replace("""[\{\}]""".toRegex(), "")
}

fun parseJsonTeams(json: String): List<Team> {
    if (json == "null" || json.isEmpty()) return emptyList()
    val jsonArr = JSONArray(json)
    val teamList = mutableListOf<Team>()

    for (i in 0..<jsonArr.length()) {
        val jsonObj = jsonArr.get(i) as JSONObject
        val team = Team(
            id = jsonObj.getInt("idTeam"),
            name = jsonObj.getString("strTeam"),
            shortName = jsonObj.getString("strTeamShort"),
            altName = jsonObj.getString("strAlternate"),
            formedYear = jsonObj.getInt("intFormedYear"),
            leagueId = jsonObj.getInt("idLeague"),
            leagueName = jsonObj.getString("strLeague"),
            stadiumCapacity = jsonObj.getInt("intStadiumCapacity"),
            stadiumLocation = jsonObj.getString("strStadiumLocation"),
            stadiumName = jsonObj.getString("strStadium"),
            stadiumThumb = jsonObj.getString("strStadiumThumb"),
            stringKeywords = jsonObj.getString("strKeywords"),
            teamJersey = jsonObj.getString("strTeamJersey"),
            teamLogo = jsonObj.getString("strTeamLogo"),
            website = jsonObj.getString("strWebsite")
        )

        teamList.add(team)
    }

    return teamList
}

data object ImgList {
    private val imgList = mutableMapOf<Int, ImageBitmap>()

    fun addImage(key: Int, image: ImageBitmap) {
        imgList[key] = image
    }

    fun getImage(index: Int): ImageBitmap {
        return imgList[index]!!
    }

    fun hasImage(index: Int): Boolean {
        return imgList.containsKey(index)
    }

    fun destroySelf() {
        imgList.clear()
    }
}