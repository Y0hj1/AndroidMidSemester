package io.github.dekabrsky.androidMidSemester.data.cache

import io.github.dekabrsky.androidMidSemester.data.db.CacheDao
import io.github.dekabrsky.androidMidSemester.data.db.entity.CacheMetaEntity
import io.github.dekabrsky.androidMidSemester.data.mapper.toCachedEntity
import io.github.dekabrsky.androidMidSemester.data.mapper.toMatch
import io.github.dekabrsky.androidMidSemester.domain.model.Match
import io.github.dekabrsky.androidMidSemester.domain.model.MatchStatus
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.LocalDate

private val MATCHES_TTL = Duration.ofMinutes(10)

class MatchesCache(
    private val dao: CacheDao,
    private val clock: Clock,
) {

    suspend fun get(date: LocalDate): CachedData<List<Match>>? {
        val meta = dao.meta(key(date)) ?: return null
        val matches = dao.matchesOn(date.toString()).map { it.toMatch() }
        return CachedData(matches, isFresh(date, matches, Instant.ofEpochMilli(meta.loadedAt)))
    }

    suspend fun put(date: LocalDate, matches: List<Match>) {
        dao.replaceMatchesOn(
            date = date.toString(),
            matches = matches.mapIndexed { index, match -> match.toCachedEntity(date, index) },
            meta = CacheMetaEntity(key(date), clock.millis()),
        )
    }

    suspend fun findMatch(id: Long): Match? = dao.match(id)?.toMatch()

    /**
     * Прошедший день, где все матчи завершены, уже не изменится — кэш бессрочный.
     * Сегодняшние и будущие матчи (и незавершённые прошлые) перезапрашиваем раз в MATCHES_TTL.
     */
    private fun isFresh(date: LocalDate, matches: List<Match>, loadedAt: Instant): Boolean {
        val dayClosed = date.isBefore(LocalDate.now(clock)) &&
            matches.all { it.status == MatchStatus.FINISHED }
        return dayClosed || Duration.between(loadedAt, clock.instant()) < MATCHES_TTL
    }

    private fun key(date: LocalDate) = "matches:$date"
}
