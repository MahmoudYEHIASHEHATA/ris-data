package com.cassbana.risk

import android.app.Application
import android.content.Context
import android.util.Log
import com.cassbana.risk.database.databaseModuleRs
import com.cassbana.risk.workers.di.networkModuleRS
import com.cassbana.risk.workers.di.workersModuleRS
import com.cassbana.risk.workers.simInfo.RSSIMInfoCollectWorker
import org.koin.android.ext.koin.androidContext
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent

object RiskDataCollection {


    lateinit var internalKoinApplication: KoinApplication
    lateinit var application: Application

    fun init(application: Application,koinApplication: KoinApplication) {
        com.cassbana.risk.RiskDataCollection.internalKoinApplication = koinApplication
        com.cassbana.risk.RiskDataCollection.internalKoinApplication.apply {
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
        return com.cassbana.risk.RiskDataCollection.internalKoinApplication.koin
    }

}