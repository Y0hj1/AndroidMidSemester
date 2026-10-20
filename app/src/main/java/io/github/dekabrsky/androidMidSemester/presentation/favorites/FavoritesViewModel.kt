package io.github.dekabrsky.androidMidSemester.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.dekabrsky.androidMidSemester.domain.model.Match
import io.github.dekabrsky.androidMidSemester.domain.usecase.ObserveFavoriteMatchesUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class FavoritesViewModel(
    observeFavorites: ObserveFavoriteMatchesUseCase,
) : ViewModel() {

    /** null — данные из базы ещё не пришли */
    val favorites: StateFlow<List<Match>?> = observeFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
