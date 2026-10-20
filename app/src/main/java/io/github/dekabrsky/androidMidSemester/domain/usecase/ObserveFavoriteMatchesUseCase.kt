package io.github.dekabrsky.androidMidSemester.domain.usecase

import io.github.dekabrsky.androidMidSemester.domain.model.Match
import io.github.dekabrsky.androidMidSemester.domain.repository.FavoriteMatchRepository
import kotlinx.coroutines.flow.Flow

class ObserveFavoriteMatchesUseCase(private val repository: FavoriteMatchRepository) {
    operator fun invoke(): Flow<List<Match>> = repository.observeFavorites()
}
