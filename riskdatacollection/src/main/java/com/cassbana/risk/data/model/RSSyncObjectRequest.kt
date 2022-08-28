package com.cassbana.risk.data.model

import androidx.annotation.Keep
import com.cassbana.risk.utils.RSUtils

@Keep
data class RSSyncObjectRequest<T> (
    val deviceId: String = RSUtils.getDeviceID(),
    val data: T
)