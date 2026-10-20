package io.github.dekabrsky.androidMidSemester.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import io.github.dekabrsky.androidMidSemester.data.db.FavoritesDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val storageModule = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create {
            androidContext().preferencesDataStoreFile("settings")
        }
    }
    single {
        Room.databaseBuilder(androidContext(), FavoritesDatabase::class.java, "favorites.db")
            .build()
    }
    single { get<FavoritesDatabase>().favoriteMatchDao() }
}
