package com.cassbana.risk.data.source.prefs

import com.cassbana.risk.data.model.RSUser


interface RSPreferencesDataSource {

    var rsUser: RSUser?

    var rsFcmToken: String

    var rsLocationPermissionGranted: Boolean

    var rsOtherPermissionsGranted: Boolean

    var rsIsInsuranceDialogShown: Boolean
}
