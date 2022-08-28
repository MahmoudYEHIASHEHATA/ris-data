package com.cassbana.risk.data.source.prefs

import com.cassbana.risk.RSConstants
import com.cassbana.risk.data.model.RSUser
import com.google.gson.Gson

class RSPreferencesDataSourceImp(private val prefs: Preferences) : RSPreferencesDataSource {

    override var rsUser: RSUser?
        get() = Gson().fromJson(prefs.load(com.cassbana.risk.RSConstants.USER, ""), RSUser::class.java)
        set(value) = prefs.save(com.cassbana.risk.RSConstants.USER, Gson().toJson(value))

    override var rsFcmToken: String
        get() = prefs.load(com.cassbana.risk.RSConstants.FCM_TOKEN, "")
        set(value) = prefs.save(com.cassbana.risk.RSConstants.FCM_TOKEN, value)

    override var rsLocationPermissionGranted: Boolean
        get() = prefs.load(com.cassbana.risk.RSConstants.HAS_GRANTED_LOCATION_PERMISSION, false)
        set(value) = prefs.save(com.cassbana.risk.RSConstants.HAS_GRANTED_LOCATION_PERMISSION, value)

    override var rsOtherPermissionsGranted: Boolean
        get() = prefs.load(com.cassbana.risk.RSConstants.HAS_GRANTED_OTHER_PERMISSIONS, false)
        set(value) = prefs.save(com.cassbana.risk.RSConstants.HAS_GRANTED_OTHER_PERMISSIONS, value)

    override var rsIsInsuranceDialogShown: Boolean
        get() = prefs.load(com.cassbana.risk.RSConstants.INSURANCE_DIALOG_SHOWN, false)
        set(value) = prefs.save(com.cassbana.risk.RSConstants.INSURANCE_DIALOG_SHOWN, value)
}
