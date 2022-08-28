package com.cassbana.risk.data

import com.cassbana.risk.workers.simInfo.data.remote.RSSIMInfoModel
import com.cassbana.risk.data.model.SyncListRequest

interface RSFraudRemoteDataSource {

    suspend fun submitSIMInformation(simInfoRequest : SyncListRequest<String, RSSIMInfoModel>) : Any

}
