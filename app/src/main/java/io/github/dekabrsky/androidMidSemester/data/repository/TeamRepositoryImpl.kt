package io.github.dekabrsky.androidMidSemester.data.repository

import io.github.dekabrsky.androidMidSemester.data.api.SStatsApi
import io.github.dekabrsky.androidMidSemester.data.api.loadAllPages
import io.github.dekabrsky.androidMidSemester.data.cache.TeamsCache
import io.github.dekabrsky.androidMidSemester.data.cache.loadWithCache
import io.github.dekabrsky.androidMidSemester.data.mapper.toTeam
import io.github.dekabrsky.androidMidSemester.domain.model.Team
import io.github.dekabrsky.androidMidSemester.domain.repository.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private const val COUNTRY = "Russia"

class TeamRepositoryImpl(
    private val api: SStatsApi,
    private val cache: TeamsCache,
) : TeamRepository {

    private val _selectedTeamIds = MutableStateFlow<Set<Long>>(emptySet())
    override val selectedTeamIds: StateFlow<Set<Long>> = _selectedTeamIds.asStateFlow()

    override suspend fun russianTeams(): List<Team> =
        loadWithCache(
            cached = cache.get(),
            load = {
                loadAllPages { offset, limit ->
                    api.teams(COUNTRY, offset, limit).data
                }
                    .filter { it.country == null || it.country.name.equals(COUNTRY, ignoreCase = true) }
                    .distinctBy { it.id }
                    .map { it.toTeam() }
                    .sortedBy { it.name }
            },
            save = { cache.put(it) },
        )

    override fun toggleTeam(id: Long) {
        _selectedTeamIds.update { ids -> if (id in ids) ids - id else ids + id }
    }
}
