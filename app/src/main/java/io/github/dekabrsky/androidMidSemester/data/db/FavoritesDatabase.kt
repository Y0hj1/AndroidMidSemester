package io.github.dekabrsky.androidMidSemester.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.dekabrsky.androidMidSemester.data.db.entity.FavoriteMatchEntity

/** Пользовательские данные — отдельно от кэша, чтобы не терять их при пересоздании кэша. */
@Database(entities = [FavoriteMatchEntity::class], version = 1, exportSchema = false)
abstract class FavoritesDatabase : RoomDatabase() {
    abstract fun favoriteMatchDao(): FavoriteMatchDao
}
