package io.github.dekabrsky.androidMidSemester.data.repository

import io.github.dekabrsky.androidMidSemester.data.api.EspnApi
import io.github.dekabrsky.androidMidSemester.data.cache.MatchesCache
import io.github.dekabrsky.androidMidSemester.data.cache.loadWithCache
import io.github.dekabrsky.androidMidSemester.data.mapper.toMatch
import io.github.dekabrsky.androidMidSemester.domain.model.Match
import io.github.dekabrsky.androidMidSemester.domain.repository.MatchRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EspnMatchRepositoryImpl(
    private val api: EspnApi,
    private val cache: MatchesCache,
) : MatchRepository {

    override suspend fun matchesOn(date: LocalDate): List<Match> =
        loadWithCache(
            cached = cache.get(date),
            load = {
                val response = api.scoreboard(date.format(DateTimeFormatter.BASIC_ISO_DATE))
                val league = response.leagues.firstOrNull()?.name.orEmpty()
                response.events.map { it.toMatch(league) }
            },
            save = { cache.put(date, it) },
        )

    override suspend fun match(id: Long): Match =
        cache.findMatch(id)
            ?: api.summary(id).toMatch(id)
            ?: throw NoSuchElementException("Матч $id не найден")
}
