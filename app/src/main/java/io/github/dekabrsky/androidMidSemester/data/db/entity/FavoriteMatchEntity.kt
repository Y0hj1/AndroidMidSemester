package io.github.dekabrsky.androidMidSemester.data.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_matches")
data class FavoriteMatchEntity(
    @PrimaryKey val id: Long,
    @Embedded(prefix = "home_") val home: TeamColumns,
    @Embedded(prefix = "away_") val away: TeamColumns,
    val homeScore: Int?,
    val awayScore: Int?,
    val status: String,
    val kickoff: String,
    val league: String,
    val round: String,
)
