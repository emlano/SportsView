package com.github.emlano.sportsview.logic.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.github.emlano.sportsview.logic.entity.League

@Dao
interface LeagueDAO {
    @Query("SELECT * FROM League")
    suspend fun getAllLeagues(): List<League>

    @Query("SELECT * FROM League WHERE id = :id")
    suspend fun getLeague(id: Int): League

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLeague(league: League)

    @Update
    suspend fun updateLeague(league: League)

    @Delete
    suspend fun deleteLeague(league: League): Int

    @Query("DELETE FROM League WHERE id = :id")
    suspend fun deleteLeagueById(id: Int)
}