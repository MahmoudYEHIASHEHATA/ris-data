package com.cassbana.risk.data

import com.cassbana.risk.workers.simInfo.data.remote.RSSIMInfoModel
import com.cassbana.risk.workers.utils.createSyncListRequest
import com.cassbana.risk.data.model.SyncListData

class RSFraudRepository(
    private val fraudRemoteDataSource: RSFraudRemoteDataSource,
    private val fraudLocalDataSource: RSFraudLocalDataSource
) {

    suspend fun submitSIMInformation(simInfoRequest: SyncListData<String, RSSIMInfoModel>): Any {
        return fraudRemoteDataSource.submitSIMInformation(
            createSyncListRequest(simInfoRequest)
        )
    }
}
