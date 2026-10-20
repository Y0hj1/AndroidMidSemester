package io.github.dekabrsky.androidMidSemester.domain.repository

import io.github.dekabrsky.androidMidSemester.domain.model.Match
import kotlinx.coroutines.flow.Flow

interface FavoriteMatchRepository {
    fun observeFavorites(): Flow<List<Match>>
    fun observeIsFavorite(matchId: Long): Flow<Boolean>
    suspend fun setFavorite(match: Match, favorite: Boolean)
}
