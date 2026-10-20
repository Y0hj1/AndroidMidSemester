package io.github.dekabrsky.androidMidSemester.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.dekabrsky.androidMidSemester.domain.model.Match
import io.github.dekabrsky.androidMidSemester.domain.usecase.ObserveIsFavoriteMatchUseCase
import io.github.dekabrsky.androidMidSemester.domain.usecase.SetMatchFavoriteUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MatchDetailsViewModel(
    private val match: Match,
    observeIsFavorite: ObserveIsFavoriteMatchUseCase,
    private val setFavorite: SetMatchFavoriteUseCase,
) : ViewModel() {

    val isFavorite: StateFlow<Boolean> = observeIsFavorite(match.id)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    fun toggleFavorite() {
        viewModelScope.launch {
            setFavorite(match, !isFavorite.value)
        }
    }
}
