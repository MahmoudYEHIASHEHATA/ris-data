package com.cassbana.antifraud

import android.app.Application
import android.content.Context
import com.cassbana.antifraud.workers.di.workersModuleRS
import org.koin.android.ext.koin.androidContext

class RSApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        RSDomain.integrateWith(instance)
    }

    companion object {
        lateinit var instance: RSApp
            private set
        lateinit var context: Context
            private set
    }
}
