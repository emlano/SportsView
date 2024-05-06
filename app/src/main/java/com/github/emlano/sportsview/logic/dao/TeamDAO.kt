package com.github.emlano.sportsview.logic.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.github.emlano.sportsview.logic.entity.Team

@Dao
interface TeamDAO {
    @Query("SELECT * FROM Team")
    suspend fun getAllTeams(): List<Team>

    @Query("SELECT * FROM Team WHERE id = :id")
    suspend fun getTeam(id: Int): Team

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTeam(team: Team)

    @Update
    suspend fun updateTeam(team: Team)

    @Delete
    suspend fun deleteTeam(team: Team)

    @Query("DELETE FROM TEAM WHERE id = :id")
    suspend fun deleteTeamById(id: Int)
}