package com.github.emlano.sportsview.logic

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.emlano.sportsview.logic.dao.LeagueDAO
import com.github.emlano.sportsview.logic.dao.TeamDAO
import com.github.emlano.sportsview.logic.entity.League
import com.github.emlano.sportsview.logic.entity.Team

@Database(entities = [League::class, Team::class], version = 2, autoMigrations = [ AutoMigration (from = 1, to = 2) ])
abstract class SportsDatabase: RoomDatabase() {
    abstract fun leagueDao(): LeagueDAO
    abstract fun teamDao(): TeamDAO

    companion object {
        @Volatile
        private var INSTANCE: SportsDatabase? = null

        fun getInstance(context: Context): SportsDatabase {
            synchronized(this) {
                var db = INSTANCE

                if (db == null) {
                    db = Room.databaseBuilder(context.applicationContext, SportsDatabase::class.java, "sportsviewDb").build()

                    INSTANCE = db
                }

                return db
            }
        }
    }
}