package com.cassbana.antifraud

import android.app.Application
import android.content.Context
import android.util.Log
import com.cassbana.antifraud.database.databaseModuleRs
import com.cassbana.antifraud.workers.di.networkModuleRS
import com.cassbana.antifraud.workers.di.workersModuleRS
import com.cassbana.antifraud.workers.simInfo.RSSIMInfoCollectWorker
import org.koin.android.ext.koin.androidContext
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent

object RiskDataCollection {


    lateinit var internalKoinApplication: KoinApplication
    lateinit var application: Application

    fun init(application: Application,koinApplication: KoinApplication) {
        internalKoinApplication = koinApplication
        internalKoinApplication.apply {
            androidContext(application)
            modules(listOf(workersModuleRS, networkModuleRS, databaseModuleRs))
        }
    }

    fun scheduleSIMInfoCollectWorkManager(context: Context) =
        RSSIMInfoCollectWorker.start(context, 1)

    fun logInstallationSuccess() =
        Log.d("install anti fraud :", "installation data-collection success")


}

internal interface MySdkKoinComponent : KoinComponent {
    override fun getKoin(): Koin {
        return RiskDataCollection.internalKoinApplication.koin
    }

}