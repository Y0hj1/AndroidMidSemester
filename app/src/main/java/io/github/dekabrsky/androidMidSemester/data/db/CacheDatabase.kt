package io.github.dekabrsky.androidMidSemester.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.dekabrsky.androidMidSemester.data.db.entity.CacheMetaEntity
import io.github.dekabrsky.androidMidSemester.data.db.entity.CachedMatchEntity
import io.github.dekabrsky.androidMidSemester.data.db.entity.CachedTeamEntity

@Database(
    entities = [CachedMatchEntity::class, CachedTeamEntity::class, CacheMetaEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class CacheDatabase : RoomDatabase() {
    abstract fun cacheDao(): CacheDao
}
