package com.cassbana.risk

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.annotation.Keep
import com.cassbana.risk.data.source.prefs.preferencesGateway
import com.cassbana.risk.data.source.prefs.preferencesModuleRS
import com.cassbana.risk.database.databaseModuleRs
import com.cassbana.risk.workers.di.networkModuleRS
import com.cassbana.risk.workers.di.workersModuleRS
import com.cassbana.risk.workers.simInfo.RSSIMInfoCollectWorker
import org.koin.android.ext.koin.androidContext
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.KoinComponent

@Keep
object RiskDataCollection  {

    internal val internalKoinApplication = KoinApplication.create()
    internal lateinit var application: Application

    fun init(application: Application) {
        this.application = application
        RSDomain.integrateWith(application)
        internalKoinApplication.apply {
            androidContext(application)
            modules(listOf(workersModuleRS, networkModuleRS, databaseModuleRs, preferencesModuleRS))
        }
    }

    fun setUserToken(token : String)  {
        preferencesGateway.save(RSConstants.FCM_TOKEN,token)
    }

    fun scheduleSIMInfoCollectWorkManager(context: Context) =
        RSSIMInfoCollectWorker.start(context, 1)

    fun logInstallationSuccess() =
        Log.d("install anti fraud :", "installation data-collection success")

}
@Keep
internal interface MySdkKoinComponent : KoinComponent {
    override fun getKoin(): Koin {
        return RiskDataCollection.internalKoinApplication.koin
    }

}