package io.github.dekabrsky.androidMidSemester.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_teams")
data class CachedTeamEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val logoUrl: String?,
    val position: Int,
)
