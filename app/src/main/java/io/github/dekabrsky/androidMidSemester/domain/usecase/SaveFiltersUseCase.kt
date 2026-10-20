package io.github.dekabrsky.androidMidSemester.domain.usecase

import io.github.dekabrsky.androidMidSemester.domain.model.MatchFilters
import io.github.dekabrsky.androidMidSemester.domain.repository.FiltersRepository

class SaveFiltersUseCase(private val repository: FiltersRepository) {
    suspend operator fun invoke(filters: MatchFilters) = repository.save(filters)
}
