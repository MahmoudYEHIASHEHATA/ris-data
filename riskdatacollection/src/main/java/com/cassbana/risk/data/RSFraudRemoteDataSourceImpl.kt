package com.cassbana.risk.data


import com.cassbana.risk.ICoroutineScopeDispatchers
import com.cassbana.risk.data.model.SyncListRequest
import com.cassbana.risk.workers.simInfo.data.remote.RSSIMInfoModel
import kotlinx.coroutines.withContext

class RSFraudRemoteDataSourceImpl(
    private val fraudApis: RSFraudApis,
    private val coroutineScopeDispatchers: com.cassbana.risk.ICoroutineScopeDispatchers
) : RSFraudRemoteDataSource {

    override suspend fun submitSIMInformation(simInfoRequest: SyncListRequest<String, RSSIMInfoModel>): Any {
        return withContext(coroutineScopeDispatchers.IO) {
            try {

                fraudApis.submitSIMInformation(simInfoRequest)
            } catch (e: Exception) {
                throw e
            }
        }
    }


}
