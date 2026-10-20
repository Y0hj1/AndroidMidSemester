package io.github.dekabrsky.androidMidSemester.presentation.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import io.github.dekabrsky.androidMidSemester.R
import io.github.dekabrsky.androidMidSemester.domain.model.StatusFilter
import io.github.dekabrsky.androidMidSemester.domain.model.Team
import org.koin.androidx.compose.koinViewModel

private val StatusFilter.title: String
    get() = when (this) {
        StatusFilter.ALL -> "Все"
        StatusFilter.FINISHED -> "Сыгранные"
        StatusFilter.SCHEDULED -> "Предстоящие"
    }

@Composable
fun FiltersScreen(
    onBack: () -> Unit,
    onDone: () -> Unit,
    viewModel: FiltersViewModel = koinViewModel(),
) {
    val teams by viewModel.teams.collectAsStateWithLifecycle()
    val draft by viewModel.draft.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { FiltersTopBar(onBack = onBack, onReset = viewModel::reset) },
        bottomBar = {
            Button(
                onClick = { viewModel.apply(onDone) },
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(16.dp),
            ) {
                Text("Готово")
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            item { SectionTitle("Статус матча") }
            item {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    StatusFilter.entries.forEach { status ->
                        FilterChip(
                            selected = draft.status == status,
                            onClick = { viewModel.selectStatus(status) },
                            label = { Text(status.title) },
                        )
                    }
                }
            }
            item { SectionTitle("Команды России") }
            when (val current = teams) {
                TeamsUiState.Loading -> item {
                    Box(Modifier.fillMaxWidth().padding(24.dp), Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is TeamsUiState.Error -> item {
                    Text(
                        text = current.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp),
                    )
                }
                is TeamsUiState.Content -> items(current.teams, key = { it.id }) { team ->
                    TeamRow(
                        team = team,
                        checked = team.id in draft.teamIds,
                        onToggle = { viewModel.toggleTeam(team) },
                    )
                }
            }
        }
    }
}

@Composable
private fun FiltersTopBar(
    onBack: () -> Unit,
    onReset: () -> Unit,
) {
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
            text = "Фильтры",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
        )
        TextButton(onClick = onReset) {
            Text("Сбросить")
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
    )
}

@Composable
private fun TeamRow(
    team: Team,
    checked: Boolean,
    onToggle: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(value = checked, role = Role.Checkbox, onValueChange = { onToggle() })
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = team.logoUrl,
            contentDescription = team.name,
            modifier = Modifier.size(40.dp),
            placeholder = painterResource(R.drawable.ic_team_placeholder),
            error = painterResource(R.drawable.ic_team_placeholder),
        )
        Text(
            text = team.name,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
        )
        Checkbox(checked = checked, onCheckedChange = null)
    }
}
