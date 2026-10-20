package io.github.dekabrsky.androidMidSemester.data.mapper

import io.github.dekabrsky.androidMidSemester.data.db.entity.CachedMatchEntity
import io.github.dekabrsky.androidMidSemester.data.db.entity.CachedTeamEntity
import io.github.dekabrsky.androidMidSemester.data.db.entity.TeamColumns
import io.github.dekabrsky.androidMidSemester.domain.model.Match
import io.github.dekabrsky.androidMidSemester.domain.model.MatchStatus
import io.github.dekabrsky.androidMidSemester.domain.model.Team
import java.time.LocalDate
import java.time.LocalDateTime

fun Team.toColumns(): TeamColumns = TeamColumns(id = id, name = name, logoUrl = logoUrl)

fun TeamColumns.toTeam(): Team = Team(id = id, name = name, logoUrl = logoUrl)

fun Match.toCachedEntity(date: LocalDate, position: Int): CachedMatchEntity = CachedMatchEntity(
    date = date.toString(),
    id = id,
    position = position,
    home = home.toColumns(),
    away = away.toColumns(),
    homeScore = homeScore,
    awayScore = awayScore,
    status = status.name,
    kickoff = kickoff.toString(),
    league = league,
    round = round,
)

fun CachedMatchEntity.toMatch(): Match = Match(
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

fun Team.toCachedEntity(position: Int): CachedTeamEntity =
    CachedTeamEntity(id = id, name = name, logoUrl = logoUrl, position = position)

fun CachedTeamEntity.toTeam(): Team = Team(id = id, name = name, logoUrl = logoUrl)
