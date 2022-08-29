package com.cassbana.risk



object RSDomain {

    lateinit var application: RSApp private set


    fun integrateWith(application: RSApp) {
        RSDomain.application = application
    }
}