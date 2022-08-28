package com.cassbana.risk.data

interface RSFraudLocalDataSource {

    suspend fun deleteContacts ()
    suspend fun deleteBluetooth ()
    suspend fun deleteLocation ()
    suspend fun deleteAppHistory ()

}