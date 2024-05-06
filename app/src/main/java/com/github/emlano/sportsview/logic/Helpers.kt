package com.github.emlano.sportsview.logic

import com.github.emlano.sportsview.logic.api.fetchAndStoreLeagues
import com.github.emlano.sportsview.logic.api.fetchTeamsFromLeague
import com.github.emlano.sportsview.logic.entity.League
import com.github.emlano.sportsview.logic.entity.Team
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

// To extract the relevant fields for teams from the json returned from the API
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

// To format a display-able String to be shown at the Search Clubs from Leagues page
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

// To create Team objects from the received JSON
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

// Created to get all teams in the SportsDB
// !! Does Not Work and Unused !!
suspend fun getAllTeams(): List<Team> {
    val teamList = mutableListOf<Team>()

    val leagues = JSONObject(fetchAndStoreLeagues()).getJSONArray("leagues")

    for (i in 0..<leagues.length()) {
        val league = leagues[i] as JSONObject
        val teams = fetchTeamsFromLeague(league.getString("strLeague"))

        teamList.addAll(parseJsonTeams(teams))
    }

    return teamList
}