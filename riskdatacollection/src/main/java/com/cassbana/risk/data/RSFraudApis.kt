package com.cassbana.risk.data

import com.cassbana.risk.data.model.SyncListRequest
import com.cassbana.risk.workers.simInfo.data.remote.RSSIMInfoModel
import retrofit2.http.Body
import retrofit2.http.POST


interface RSFraudApis {



    @POST("sim-cards")
    suspend fun submitSIMInformation(
        @Body simInformationList: SyncListRequest<String, RSSIMInfoModel>
    ): Any


}