package io.github.dekabrsky.androidMidSemester.data.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import io.github.dekabrsky.androidMidSemester.domain.model.MatchFilters
import io.github.dekabrsky.androidMidSemester.domain.model.StatusFilter
import io.github.dekabrsky.androidMidSemester.domain.repository.FiltersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

private val TEAM_IDS_KEY = stringSetPreferencesKey("filter_team_ids")
private val STATUS_KEY = stringPreferencesKey("filter_status")

class FiltersRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : FiltersRepository {

    override val filters: Flow<MatchFilters> = dataStore.data
        .map { prefs ->
            MatchFilters(
                teamIds = prefs[TEAM_IDS_KEY].orEmpty().mapNotNull { it.toLongOrNull() }.toSet(),
                status = prefs[STATUS_KEY]
                    ?.let { name -> StatusFilter.entries.firstOrNull { it.name == name } }
                    ?: StatusFilter.ALL,
            )
        }
        .distinctUntilChanged()

    override suspend fun save(filters: MatchFilters) {
        dataStore.edit { prefs ->
            prefs[TEAM_IDS_KEY] = filters.teamIds.map { it.toString() }.toSet()
            prefs[STATUS_KEY] = filters.status.name
        }
    }
}
