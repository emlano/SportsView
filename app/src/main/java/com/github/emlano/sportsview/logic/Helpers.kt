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
