package com.cassbana.risk

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface ICoroutineScopeDispatchers {
    val IO: CoroutineDispatcher
    val Main: CoroutineDispatcher
}

class CoroutineScopeDispatchers(
        override val IO: CoroutineDispatcher = Dispatchers.IO,
        override val Main: CoroutineDispatcher = Dispatchers.Main
) : com.cassbana.risk.ICoroutineScopeDispatchers

