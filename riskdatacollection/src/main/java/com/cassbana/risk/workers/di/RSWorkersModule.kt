package com.cassbana.risk.workers.di



import com.cassbana.risk.CoroutineScopeDispatchers
import com.cassbana.risk.ICoroutineScopeDispatchers
import com.cassbana.risk.workers.simInfo.data.mapper.RSSIMInformationMapper
import com.cassbana.risk.workers.utils.RSUniqueIDGeneratorUseCase
import com.cassbana.risk.workers.utils.RSUniqueIDGeneratorWrapper
import com.cassbana.risk.workers.utils.UniqueIDGeneratorWrapperImpl
import com.cassbana.risk.data.*
import com.cassbana.risk.database.RSAppDatabase
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val workersModuleRS = module {
    single<ICoroutineScopeDispatchers> { CoroutineScopeDispatchers() }

    factory { get<RSAppDatabase>().simInformationDAO() }

    factory { get<Retrofit>(named(FRAUD_RETROFIT_RS)).create(RSFraudApis::class.java) }

    factory <RSFraudRemoteDataSource> { RSFraudRemoteDataSourceImpl(get(), get()) }
    factory <RSFraudLocalDataSource> { RSFraudLocalDataSourceImpl() }
    factory { RSFraudRepository(get(),get()) }


    factory { RSSIMInformationMapper() }

    factory <RSUniqueIDGeneratorWrapper>{ UniqueIDGeneratorWrapperImpl(get()) }
    factory { RSUniqueIDGeneratorUseCase(get()) }
}