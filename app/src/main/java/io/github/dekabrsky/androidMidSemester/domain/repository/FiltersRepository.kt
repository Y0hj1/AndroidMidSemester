package io.github.dekabrsky.androidMidSemester.domain.repository

import io.github.dekabrsky.androidMidSemester.domain.model.MatchFilters
import kotlinx.coroutines.flow.Flow

interface FiltersRepository {
    val filters: Flow<MatchFilters>
    suspend fun save(filters: MatchFilters)
}
