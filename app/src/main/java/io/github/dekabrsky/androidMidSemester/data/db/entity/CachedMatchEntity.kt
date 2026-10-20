package io.github.dekabrsky.androidMidSemester.data.db.entity

import androidx.room.Embedded
import androidx.room.Entity

@Entity(tableName = "cached_matches", primaryKeys = ["date", "id"])
data class CachedMatchEntity(
    val date: String,
    val id: Long,
    val position: Int,
    @Embedded(prefix = "home_") val home: TeamColumns,
    @Embedded(prefix = "away_") val away: TeamColumns,
    val homeScore: Int?,
    val awayScore: Int?,
    val status: String,
    val kickoff: String,
    val league: String,
    val round: String,
)
