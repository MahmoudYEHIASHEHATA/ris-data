package com.cassbana.risk.database

import android.app.Application
import androidx.room.Room
import com.cassbana.risk.RiskDataCollection
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.dsl.koinApplication
import org.koin.dsl.module

val databaseModuleRs = module {

    fun provideDatabase(application: Application): RSAppDatabase {
        return Room.databaseBuilder(
            application,
            RSAppDatabase::class.java,
            RSDatabaseConstants.databaseName
        )
            .build()
    }
    single { provideDatabase(androidApplication()) }
}
