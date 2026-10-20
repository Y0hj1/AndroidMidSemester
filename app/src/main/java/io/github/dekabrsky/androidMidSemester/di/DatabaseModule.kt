package io.github.dekabrsky.androidMidSemester.di

import androidx.room.Room
import io.github.dekabrsky.androidMidSemester.data.db.CacheDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.time.Clock

val databaseModule = module {
    single<Clock> { Clock.systemDefaultZone() }
    single {
        // кэш можно безболезненно пересоздать при смене схемы
        Room.databaseBuilder(androidContext(), CacheDatabase::class.java, "cache.db")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
    single { get<CacheDatabase>().cacheDao() }
}
