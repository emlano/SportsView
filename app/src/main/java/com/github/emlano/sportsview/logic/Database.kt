package com.github.emlano.sportsview.logic

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.emlano.sportsview.logic.dao.LeagueDAO
import com.github.emlano.sportsview.logic.entity.League

@Database(entities = [League::class], version = 1)
abstract class Database: RoomDatabase() {
    abstract fun leagueDao(): LeagueDAO
}