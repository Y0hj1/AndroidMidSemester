package io.github.dekabrsky.androidMidSemester.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Когда последний раз загружали данные по ключу (например, "matches:2026-10-20"). */
@Entity(tableName = "cache_meta")
data class CacheMetaEntity(
    @PrimaryKey val cacheKey: String,
    val loadedAt: Long,
)
