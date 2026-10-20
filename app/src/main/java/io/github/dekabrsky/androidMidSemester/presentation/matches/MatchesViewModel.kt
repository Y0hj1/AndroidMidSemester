package io.github.dekabrsky.androidMidSemester.presentation.matches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.dekabrsky.androidMidSemester.domain.model.Match
import io.github.dekabrsky.androidMidSemester.domain.model.MatchFilters
import io.github.dekabrsky.androidMidSemester.domain.usecase.GetMatchesUseCase
import io.github.dekabrsky.androidMidSemester.domain.usecase.ObserveFiltersUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

sealed interface MatchesUiState {
    data object Loading : MatchesUiState
    data class Content(
        val date: LocalDate,
        val matches: List<Match>,
        val filtered: Boolean,
    ) : MatchesUiState
    data class Error(val message: String) : MatchesUiState
}

class MatchesViewModel(
    private val getMatches: GetMatchesUseCase,
    observeFilters: ObserveFiltersUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<MatchesUiState>(MatchesUiState.Loading)
    val state: StateFlow<MatchesUiState> = _state.asStateFlow()

    private var currentDate = LocalDate.now()
    private var filters = MatchFilters()
    private var loadJob: Job? = null

    init {
        // первая эмиссия — сохранённые фильтры, дальше — каждое «Готово» на экране фильтров
        viewModelScope.launch {
            observeFilters().collect { newFilters ->
                filters = newFilters
                load(currentDate)
            }
        }
    }

    fun previousDay() {
        load(currentDate.minusDays(1))
    }

    fun nextDay() {
        load(currentDate.plusDays(1))
    }

    private fun load(date: LocalDate) {
        currentDate = date
        loadJob?.cancel()
        _state.value = MatchesUiState.Loading
        loadJob = viewModelScope.launch {
            runCatching { getMatches(date) }
                .onSuccess { matches ->
                    _state.value = MatchesUiState.Content(
                        date = date,
                        matches = matches.filter(filters::accepts),
                        filtered = !filters.isDefault,
                    )
                }
                .onFailure { e ->
                    if (e is CancellationException) throw e
                    _state.value = MatchesUiState.Error(
                        e.message ?: "Не удалось загрузить матчи"
                    )
                }
        }
    }
}
