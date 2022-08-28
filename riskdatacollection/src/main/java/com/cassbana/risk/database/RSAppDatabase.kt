package com.cassbana.risk.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cassbana.risk.workers.simInfo.data.local.RSSIMInfoDao
import com.cassbana.risk.workers.simInfo.data.remote.RSSIMInfoModel


@Database(
    entities = [
        RSSIMInfoModel::class,
    ],
    autoMigrations = [],
    version = RSDatabaseConstants.databaseVersion,
    // Enable export database schema to allow $[androidx.room.AutoMigration] in the next database versions
    exportSchema = true
)
@TypeConverters(RSContactPhoneConverter::class)
abstract class RSAppDatabase : RoomDatabase() {

    abstract fun simInformationDAO(): RSSIMInfoDao
}