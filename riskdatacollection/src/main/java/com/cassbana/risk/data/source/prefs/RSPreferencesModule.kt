package com.cassbana.risk.data.source.prefs

import android.content.Context
import com.cassbana.risk.RSDomain
import com.cassbana.risk.data.source.prefs.RSPreferences
import com.cassbana.risk.data.source.prefs.RSPreferencesDataSource
import com.cassbana.risk.data.source.prefs.RSPreferencesDataSourceImp
import org.koin.dsl.module

val preferencesModuleRS = module {
    single { RSPreferences(RSDomain.application.getSharedPreferences("PREFERENCES_NAME", Context.MODE_PRIVATE)) }
    single<RSPreferencesDataSource> { RSPreferencesDataSourceImp(get()) }
}