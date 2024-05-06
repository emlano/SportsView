package com.github.emlano.sportsview.logic.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Team(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String,
    val shortName: String,
    val altName: String,
    val formedYear: Int,
    val leagueName: String,
    val leagueId: Int,
    val stadiumName: String,
    val stringKeywords: String,
    val stadiumThumb: String,
    val stadiumLocation: String,
    val stadiumCapacity: Int,
    val website: String,
    val teamJersey: String,
    val teamLogo: String
)