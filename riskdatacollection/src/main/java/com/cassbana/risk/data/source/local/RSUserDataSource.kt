package com.cassbana.risk.data.source.local



import com.cassbana.risk.RSConstants.USER
import com.cassbana.risk.data.model.RSUser
import com.cassbana.risk.data.model.RSUserType
import com.cassbana.risk.data.source.prefs.preferencesGateway

import com.google.gson.Gson

object RSUserDataSource {

    fun saveUser(RSUser: RSUser?) {
        preferencesGateway.save(USER, Gson().toJson(RSUser))
    }

    fun getUser(): RSUser? {
        return Gson().fromJson(
            preferencesGateway.load(USER, ""), RSUser::class.java
        )
    }

    fun getToken(): String {
        return if (hasUser()) "Bearer " + getUser()?.token else ""
    }

    fun hasUser(): Boolean {
        val RSUser = Gson().fromJson(
            preferencesGateway.load(USER, ""),
            RSUser::class.java
        )

        if (RSUser?.token != null) {
            return RSUser.token.toString().isNotEmpty()
        }

        return false
    }

    fun isAgent(): Boolean {
        val RSUser = Gson().fromJson(
            preferencesGateway.load(USER, ""),
            RSUser::class.java
        )

        if (RSUser?.type != null) {
            return RSUser.type == RSUserType.AGENT
        }
        return false
    }


    fun isMerchant(): Boolean {
        val RSUser = Gson().fromJson(
            preferencesGateway.load(USER, ""),
            RSUser::class.java
        )

        if (RSUser?.type != null) {
            return RSUser.type == RSUserType.MERCHANT
        }
        return false
    }


    fun isClient(): Boolean {
        val RSUser = Gson().fromJson(
            preferencesGateway.load(USER, ""),
            RSUser::class.java
        )

        if (RSUser?.type != null) {
            return RSUser.type == RSUserType.CLIENT
        }
        return false
    }
}