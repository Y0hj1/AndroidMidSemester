package io.github.dekabrsky.androidMidSemester.domain.model

enum class StatusFilter { ALL, FINISHED, SCHEDULED }

data class MatchFilters(
    val teamIds: Set<Long> = emptySet(),
    val status: StatusFilter = StatusFilter.ALL,
) {
    val isDefault: Boolean get() = this == MatchFilters()

    fun accepts(match: Match): Boolean {
        val byTeam = teamIds.isEmpty() || match.home.id in teamIds || match.away.id in teamIds
        val byStatus = when (status) {
            StatusFilter.ALL -> true
            StatusFilter.FINISHED -> match.status == MatchStatus.FINISHED
            StatusFilter.SCHEDULED -> match.status == MatchStatus.SCHEDULED
        }
        return byTeam && byStatus
    }
}
