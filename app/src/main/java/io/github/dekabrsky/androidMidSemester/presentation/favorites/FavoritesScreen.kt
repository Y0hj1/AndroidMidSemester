package io.github.dekabrsky.androidMidSemester.presentation.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.dekabrsky.androidMidSemester.R
import io.github.dekabrsky.androidMidSemester.domain.model.Match
import io.github.dekabrsky.androidMidSemester.presentation.matches.MatchListItem
import io.github.dekabrsky.androidMidSemester.presentation.ui.common.LoadingState
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter
import java.util.Locale

private val DAY_FORMAT = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.forLanguageTag("ru"))

@Composable
fun FavoritesScreen(
    onBack: () -> Unit,
    onMatchClick: (Match) -> Unit,
    viewModel: FavoritesViewModel = koinViewModel(),
) {
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { FavoritesTopBar(onBack = onBack) },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            val matches = favorites
            when {
                matches == null -> LoadingState()
                matches.isEmpty() -> Text(
                    text = "В избранном пока пусто.\nДобавить матч можно звёздочкой в деталях.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                )
                else -> LazyColumn {
                    matches.groupBy { it.kickoff.toLocalDate() }.forEach { (day, dayMatches) ->
                        item(key = day.toString()) {
                            Text(
                                text = day.format(DAY_FORMAT),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 4.dp),
                            )
                        }
                        items(dayMatches, key = { it.id }) { match ->
                            MatchListItem(match = match, onClick = onMatchClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoritesTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBack) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_left),
                contentDescription = "Назад",
            )
        }
        Text(
            text = "Избранное",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}
