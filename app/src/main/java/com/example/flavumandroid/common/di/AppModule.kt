package com.example.flavumandroid.common.di

import android.app.Application
import androidx.room.Room
import com.example.flavumandroid.home.domain.SlotsDao
import com.example.flavumandroid.home.domain.SlotsRepo
import com.example.flavumandroid.home.domain.SlotsRoomDatabase
import com.example.flavumandroid.home.viewmodel.HomeViewModel
import com.example.flavumandroid.home.viewmodel.SharedViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Dagger Hilt module providing dependencies for the application
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Provides a Singleton instance of SlotsRepo with a dependency on SlotsDao
    @Provides
    @Singleton
    fun provideSlotsRepo(dao: SlotsDao): SlotsRepo {
        return SlotsRepo(dao)
    }

    // Provides a Singleton instance of HomeViewModel with a dependency on SlotsRepo
    @Provides
    @Singleton
    fun provideHomeViewModel(repo: SlotsRepo): HomeViewModel {
        return HomeViewModel(repo)
    }

    // Provides a Singleton instance of SharedViewModel
    @Provides
    @Singleton
    fun provideSharedViewModel(): SharedViewModel {
        return SharedViewModel()
    }

    // Provides a Singleton instance of SlotsDao with a dependency on SlotsRoomDatabase
    @Provides
    @Singleton
    fun provideSlotsDao(database: SlotsRoomDatabase): SlotsDao {
        return database.getDao()
    }

    // Provides a Singleton instance of SlotsRoomDatabase with a dependency on the Application
    @Provides
    @Singleton
    fun provideSlotsRoomDatabase(application: Application): SlotsRoomDatabase {
        return Room.databaseBuilder(
            application,
            SlotsRoomDatabase::class.java,
            "slots"
        ).fallbackToDestructiveMigration().build()
    }
}
