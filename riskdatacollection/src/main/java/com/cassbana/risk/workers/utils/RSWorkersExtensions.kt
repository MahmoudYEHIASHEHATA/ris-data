package com.cassbana.risk.workers.utils

import com.cassbana.risk.data.model.*

fun String.toTrueOrFalse(): Boolean = this == "1"


fun <ID, T> createSyncListRequest(
    syncListData: SyncListData<ID, T>
): SyncListRequest<ID, T> {
    return SyncListRequest<ID, T>(
        data = syncListData
    )
}

fun <ID, T> createSyncListRequestWithTotal(
    syncListDataWithTotal: SyncListDataWithTotal<ID, T>
): SyncListRequestWithTotal<ID, T> {
    return SyncListRequestWithTotal<ID, T>(
        data = syncListDataWithTotal
    )
}

fun <T> createSyncObjectRequest(
    objectRequest: T
): RSSyncObjectRequest<T> {
    return RSSyncObjectRequest<T>(
        data = objectRequest
    )
}