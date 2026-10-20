package io.github.dekabrsky.androidMidSemester.data.cache

import io.github.dekabrsky.androidMidSemester.data.db.CacheDao
import io.github.dekabrsky.androidMidSemester.data.db.entity.CacheMetaEntity
import io.github.dekabrsky.androidMidSemester.data.mapper.toCachedEntity
import io.github.dekabrsky.androidMidSemester.data.mapper.toTeam
import io.github.dekabrsky.androidMidSemester.domain.model.Team
import java.time.Clock
import java.time.Duration
import java.time.Instant

private const val TEAMS_KEY = "teams"
private val TEAMS_TTL = Duration.ofDays(7)

class TeamsCache(
    private val dao: CacheDao,
    private val clock: Clock,
) {

    suspend fun get(): CachedData<List<Team>>? {
        val meta = dao.meta(TEAMS_KEY) ?: return null
        val teams = dao.teams().map { it.toTeam() }
        val age = Duration.between(Instant.ofEpochMilli(meta.loadedAt), clock.instant())
        return CachedData(teams, age < TEAMS_TTL)
    }

    suspend fun put(teams: List<Team>) {
        dao.replaceTeams(
            teams = teams.mapIndexed { index, team -> team.toCachedEntity(index) },
            meta = CacheMetaEntity(TEAMS_KEY, clock.millis()),
        )
    }
}
