package com.cassbana.risk



object RSDomain {

    lateinit var application: com.cassbana.risk.RSApp private set


    fun integrateWith(application: com.cassbana.risk.RSApp) {
        com.cassbana.risk.RSDomain.application = application
    }
}