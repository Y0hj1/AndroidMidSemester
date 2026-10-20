package io.github.dekabrsky.androidMidSemester.di

import io.github.dekabrsky.androidMidSemester.domain.usecase.GetMatchUseCase
import io.github.dekabrsky.androidMidSemester.domain.usecase.GetMatchesUseCase
import io.github.dekabrsky.androidMidSemester.domain.usecase.GetRussianTeamsUseCase
import io.github.dekabrsky.androidMidSemester.domain.usecase.ObserveFavoriteMatchesUseCase
import io.github.dekabrsky.androidMidSemester.domain.usecase.ObserveFiltersUseCase
import io.github.dekabrsky.androidMidSemester.domain.usecase.ObserveIsFavoriteMatchUseCase
import io.github.dekabrsky.androidMidSemester.domain.usecase.SaveFiltersUseCase
import io.github.dekabrsky.androidMidSemester.domain.usecase.SetMatchFavoriteUseCase
import io.github.dekabrsky.androidMidSemester.presentation.details.MatchDetailsViewModel
import io.github.dekabrsky.androidMidSemester.presentation.favorites.FavoritesViewModel
import io.github.dekabrsky.androidMidSemester.presentation.filters.FiltersViewModel
import io.github.dekabrsky.androidMidSemester.presentation.matches.MatchesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    single { GetMatchesUseCase(get()) }
    single { GetMatchUseCase(get()) }
    single { GetRussianTeamsUseCase(get()) }
    single { ObserveFiltersUseCase(get()) }
    single { SaveFiltersUseCase(get()) }
    single { ObserveFavoriteMatchesUseCase(get()) }
    single { ObserveIsFavoriteMatchUseCase(get()) }
    single { SetMatchFavoriteUseCase(get()) }
    viewModelOf(::MatchesViewModel)
    viewModelOf(::FiltersViewModel)
    viewModelOf(::FavoritesViewModel)
    viewModel { params -> MatchDetailsViewModel(params.get(), get(), get()) }
}
