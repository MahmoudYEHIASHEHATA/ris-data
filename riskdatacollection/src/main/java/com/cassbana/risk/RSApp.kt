package com.cassbana.risk

import android.app.Application
import android.content.Context

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
