package io.github.dekabrsky.androidMidSemester.data.mapper

import io.github.dekabrsky.androidMidSemester.data.db.entity.FavoriteMatchEntity
import io.github.dekabrsky.androidMidSemester.domain.model.Match
import io.github.dekabrsky.androidMidSemester.domain.model.MatchStatus
import java.time.LocalDateTime

fun Match.toFavoriteEntity(): FavoriteMatchEntity = FavoriteMatchEntity(
    id = id,
    home = home.toColumns(),
    away = away.toColumns(),
    homeScore = homeScore,
    awayScore = awayScore,
    status = status.name,
    kickoff = kickoff.toString(),
    league = league,
    round = round,
)

fun FavoriteMatchEntity.toMatch(): Match = Match(
    id = id,
    home = home.toTeam(),
    away = away.toTeam(),
    homeScore = homeScore,
    awayScore = awayScore,
    status = MatchStatus.valueOf(status),
    kickoff = LocalDateTime.parse(kickoff),
    league = league,
    round = round,
)
