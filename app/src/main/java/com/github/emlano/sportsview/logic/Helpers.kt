package com.github.emlano.sportsview.logic

import com.github.emlano.sportsview.logic.entity.League
import org.json.JSONArray
import org.json.JSONObject

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
    if (root.getString("teams") == "null") return "No clubs of such league!"
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
    val jsonArr = JSONArray(jsonStr)

    val sb = StringBuilder()
    for (i in 0..<jsonArr.length()) {
        val j = jsonArr.get(i) as JSONObject
        sb.append(j.toString(2))
        sb.append("\n")
    }

    return sb.toString().replace("""[\{\}]""".toRegex(), "")
}