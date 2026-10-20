package io.github.dekabrsky.androidMidSemester.presentation.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.dekabrsky.androidMidSemester.domain.model.MatchFilters
import io.github.dekabrsky.androidMidSemester.domain.model.StatusFilter
import io.github.dekabrsky.androidMidSemester.domain.model.Team
import io.github.dekabrsky.androidMidSemester.domain.usecase.GetRussianTeamsUseCase
import io.github.dekabrsky.androidMidSemester.domain.usecase.ObserveFiltersUseCase
import io.github.dekabrsky.androidMidSemester.domain.usecase.SaveFiltersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface TeamsUiState {
    data object Loading : TeamsUiState
    data class Content(val teams: List<Team>) : TeamsUiState
    data class Error(val message: String) : TeamsUiState
}

class FiltersViewModel(
    private val getRussianTeams: GetRussianTeamsUseCase,
    private val observeFilters: ObserveFiltersUseCase,
    private val saveFilters: SaveFiltersUseCase,
) : ViewModel() {

    private val _teams = MutableStateFlow<TeamsUiState>(TeamsUiState.Loading)
    val teams: StateFlow<TeamsUiState> = _teams.asStateFlow()

    // черновик: в DataStore попадает только по «Готово»
    private val _draft = MutableStateFlow(MatchFilters())
    val draft: StateFlow<MatchFilters> = _draft.asStateFlow()

    init {
        viewModelScope.launch {
            _draft.value = observeFilters().first()
        }
        viewModelScope.launch {
            runCatching { getRussianTeams() }
                .onSuccess { _teams.value = TeamsUiState.Content(it) }
                .onFailure { e ->
                    _teams.value = TeamsUiState.Error(e.message ?: "Не удалось загрузить команды")
                }
        }
    }

    fun toggleTeam(team: Team) {
        _draft.update { filters ->
            val ids = filters.teamIds
            filters.copy(teamIds = if (team.id in ids) ids - team.id else ids + team.id)
        }
    }

    fun selectStatus(status: StatusFilter) {
        _draft.update { it.copy(status = status) }
    }

    fun reset() {
        _draft.value = MatchFilters()
    }

    fun apply(onApplied: () -> Unit) {
        viewModelScope.launch {
            saveFilters(_draft.value)
            onApplied()
        }
    }
}
