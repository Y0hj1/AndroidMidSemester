package io.github.dekabrsky.androidMidSemester.data.repository

import io.github.dekabrsky.androidMidSemester.data.db.FavoriteMatchDao
import io.github.dekabrsky.androidMidSemester.data.mapper.toFavoriteEntity
import io.github.dekabrsky.androidMidSemester.data.mapper.toMatch
import io.github.dekabrsky.androidMidSemester.domain.model.Match
import io.github.dekabrsky.androidMidSemester.domain.repository.FavoriteMatchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteMatchRepositoryImpl(
    private val dao: FavoriteMatchDao,
) : FavoriteMatchRepository {

    override fun observeFavorites(): Flow<List<Match>> =
        dao.observeAll().map { list -> list.map { it.toMatch() } }

    override fun observeIsFavorite(matchId: Long): Flow<Boolean> = dao.observeExists(matchId)

    override suspend fun setFavorite(match: Match, favorite: Boolean) {
        if (favorite) dao.upsert(match.toFavoriteEntity()) else dao.delete(match.id)
    }
}
