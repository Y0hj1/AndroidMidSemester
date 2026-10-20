package io.github.dekabrsky.androidMidSemester.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import io.github.dekabrsky.androidMidSemester.data.db.entity.CacheMetaEntity
import io.github.dekabrsky.androidMidSemester.data.db.entity.CachedMatchEntity
import io.github.dekabrsky.androidMidSemester.data.db.entity.CachedTeamEntity

@Dao
abstract class CacheDao {

    @Query("SELECT * FROM cache_meta WHERE cacheKey = :key")
    abstract suspend fun meta(key: String): CacheMetaEntity?

    @Query("SELECT * FROM cached_matches WHERE date = :date ORDER BY position")
    abstract suspend fun matchesOn(date: String): List<CachedMatchEntity>

    @Query("SELECT * FROM cached_matches WHERE id = :id LIMIT 1")
    abstract suspend fun match(id: Long): CachedMatchEntity?

    @Query("SELECT * FROM cached_teams ORDER BY position")
    abstract suspend fun teams(): List<CachedTeamEntity>

    @Transaction
    open suspend fun replaceMatchesOn(
        date: String,
        matches: List<CachedMatchEntity>,
        meta: CacheMetaEntity,
    ) {
        deleteMatchesOn(date)
        upsertMatches(matches)
        upsertMeta(meta)
    }

    @Transaction
    open suspend fun replaceTeams(teams: List<CachedTeamEntity>, meta: CacheMetaEntity) {
        deleteTeams()
        upsertTeams(teams)
        upsertMeta(meta)
    }

    @Query("DELETE FROM cached_matches WHERE date = :date")
    protected abstract suspend fun deleteMatchesOn(date: String)

    @Query("DELETE FROM cached_teams")
    protected abstract suspend fun deleteTeams()

    @Upsert
    protected abstract suspend fun upsertMatches(matches: List<CachedMatchEntity>)

    @Upsert
    protected abstract suspend fun upsertTeams(teams: List<CachedTeamEntity>)

    @Upsert
    protected abstract suspend fun upsertMeta(meta: CacheMetaEntity)
}
