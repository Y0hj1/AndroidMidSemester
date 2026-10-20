package io.github.dekabrsky.androidMidSemester.domain.usecase

import io.github.dekabrsky.androidMidSemester.domain.repository.FavoriteMatchRepository
import kotlinx.coroutines.flow.Flow

class ObserveIsFavoriteMatchUseCase(private val repository: FavoriteMatchRepository) {
    operator fun invoke(matchId: Long): Flow<Boolean> = repository.observeIsFavorite(matchId)
}
