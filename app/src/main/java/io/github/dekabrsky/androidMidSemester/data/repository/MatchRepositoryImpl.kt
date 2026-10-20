package io.github.dekabrsky.androidMidSemester.data.repository

import io.github.dekabrsky.androidMidSemester.data.api.SStatsApi
import io.github.dekabrsky.androidMidSemester.data.api.loadAllPages
import io.github.dekabrsky.androidMidSemester.data.cache.MatchesCache
import io.github.dekabrsky.androidMidSemester.data.cache.loadWithCache
import io.github.dekabrsky.androidMidSemester.data.mapper.toMatch
import io.github.dekabrsky.androidMidSemester.domain.model.Match
import io.github.dekabrsky.androidMidSemester.domain.repository.MatchRepository
import java.time.LocalDate

class MatchRepositoryImpl(
    private val api: SStatsApi,
    private val cache: MatchesCache,
) : MatchRepository {

    override suspend fun matchesOn(date: LocalDate): List<Match> =
        loadWithCache(
            cached = cache.get(date),
            load = {
                loadAllPages { offset, limit ->
                    api.gamesOn(date.toString(), offset, limit).data
                }
                    .distinctBy { it.id }
                    .map { it.toMatch() }
            },
            save = { cache.put(date, it) },
        )

    override suspend fun match(id: Long): Match =
        cache.findMatch(id)
            ?: api.game(id).data?.toMatch()
            ?: throw NoSuchElementException("Матч $id не найден")
}
