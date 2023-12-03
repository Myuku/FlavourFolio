package com.example.flavourfolio.database

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class StepRepository(private val stepDao: StepDao) {

    val allSteps: Flow<List<Step>> = stepDao.getAllSteps()

    @WorkerThread
    fun retrieveSteps(rid: Int): Flow<List<Step>> {
        return stepDao.getStepsForRecipe(rid)
    }

    @WorkerThread
    suspend fun insert(step: Step): Long {
        return stepDao.insert(step)
    }

    @WorkerThread
    suspend fun update(step: Step) {
        stepDao.update(step)
    }

    @WorkerThread
    suspend fun length(rid: Int): Int {
        return stepDao.length(rid)
    }


    @WorkerThread
    suspend fun deleteStep(sid: Int) {
        stepDao.delete(sid)
    }
}