package com.cassbana.risk

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext

class RSApp : Application() {

    override fun onCreate() {
        super.onCreate()
        com.cassbana.risk.RSApp.Companion.instance = this
        com.cassbana.risk.RSDomain.integrateWith(com.cassbana.risk.RSApp.Companion.instance)
    }

    companion object {
        lateinit var instance: com.cassbana.risk.RSApp
            private set
        lateinit var context: Context
            private set
    }
}
