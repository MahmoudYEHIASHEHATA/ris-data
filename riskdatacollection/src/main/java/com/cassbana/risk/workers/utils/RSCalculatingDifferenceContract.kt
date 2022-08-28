package com.cassbana.risk.workers.utils

import com.cassbana.risk.dto.RSDifferenceList


/**
 * Contact to calculating difference between two lists but by sending list in chunks.
 */
interface RSCalculatingDifferenceContract<T> {
    /**
     * Calculate the difference between chunks
     */
    fun calculateChunk(chunk1: List<T>, chunk2: List<T>)

    /**
     * @return final results after comparing chunks as inserted, updated and deleted lists
     */
    fun getFinalResult(): RSDifferenceList<T>
}