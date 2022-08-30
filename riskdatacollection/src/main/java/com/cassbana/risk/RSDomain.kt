package com.cassbana.risk

import android.app.Application


object RSDomain {

    lateinit var application: Application private set


    fun integrateWith(application: Application) {
        RSDomain.application = application
    }
}