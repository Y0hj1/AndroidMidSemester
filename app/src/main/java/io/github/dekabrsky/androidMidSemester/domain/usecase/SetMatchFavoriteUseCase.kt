package io.github.dekabrsky.androidMidSemester.domain.usecase

import io.github.dekabrsky.androidMidSemester.domain.model.Match
import io.github.dekabrsky.androidMidSemester.domain.repository.FavoriteMatchRepository

class SetMatchFavoriteUseCase(private val repository: FavoriteMatchRepository) {
    suspend operator fun invoke(match: Match, favorite: Boolean) =
        repository.setFavorite(match, favorite)
}
