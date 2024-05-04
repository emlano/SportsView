package com.github.emlano.sportsview.logic.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class League(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String,
    val sport: String,
    val alternateName: String
)