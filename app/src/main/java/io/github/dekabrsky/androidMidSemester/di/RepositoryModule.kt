package io.github.dekabrsky.androidMidSemester.di

import io.github.dekabrsky.androidMidSemester.data.cache.MatchesCache
import io.github.dekabrsky.androidMidSemester.data.cache.TeamsCache
import io.github.dekabrsky.androidMidSemester.data.repository.MatchRepositoryImpl
import io.github.dekabrsky.androidMidSemester.data.repository.TeamRepositoryImpl
import io.github.dekabrsky.androidMidSemester.domain.repository.MatchRepository
import io.github.dekabrsky.androidMidSemester.domain.repository.TeamRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { MatchesCache(get(), get()) }
    single { TeamsCache(get(), get()) }
    single<MatchRepository> { MatchRepositoryImpl(get(), get()) }
    single<TeamRepository> { TeamRepositoryImpl(get(), get()) }
}
