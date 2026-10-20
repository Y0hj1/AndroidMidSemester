package io.github.dekabrsky.androidMidSemester.presentation.filters

import io.github.dekabrsky.androidMidSemester.domain.model.MatchFilters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Общий для списка и фильтров флаг «фильтры не по умолчанию». Живёт только в памяти. */
class FiltersBadgeCache {

    private val _showBadge = MutableStateFlow(false)
    val showBadge: StateFlow<Boolean> = _showBadge.asStateFlow()

    fun update(filters: MatchFilters) {
        _showBadge.value = !filters.isDefault
    }
}
