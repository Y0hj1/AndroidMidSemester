package io.github.dekabrsky.androidMidSemester.domain.usecase

import io.github.dekabrsky.androidMidSemester.domain.model.MatchFilters
import io.github.dekabrsky.androidMidSemester.domain.repository.FiltersRepository
import kotlinx.coroutines.flow.Flow

class ObserveFiltersUseCase(private val repository: FiltersRepository) {
    operator fun invoke(): Flow<MatchFilters> = repository.filters
}
