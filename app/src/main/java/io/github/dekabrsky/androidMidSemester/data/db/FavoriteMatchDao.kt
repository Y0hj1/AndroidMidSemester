package io.github.dekabrsky.androidMidSemester.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import io.github.dekabrsky.androidMidSemester.data.db.entity.FavoriteMatchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMatchDao {

    @Query("SELECT * FROM favorite_matches ORDER BY kickoff DESC")
    fun observeAll(): Flow<List<FavoriteMatchEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_matches WHERE id = :id)")
    fun observeExists(id: Long): Flow<Boolean>

    @Upsert
    suspend fun upsert(match: FavoriteMatchEntity)

    @Query("DELETE FROM favorite_matches WHERE id = :id")
    suspend fun delete(id: Long)
}
