package com.example.flavourfolio.database

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class StepRepository(private val stepDao: StepDao) {
    @WorkerThread
    fun retrieveSteps(rid: Int): Flow<List<Step>> {
        return stepDao.getStepsForRecipe(rid)
    }

    @WorkerThread
    suspend fun insert(step: Step): Int {
        return if (stepDao.isRecipeExist(step.rid)) {
            stepDao.insert(step)
            0
        } else {
            1
        }
    }

    @WorkerThread
    suspend fun deleteStep(sid: Int) {
        stepDao.delete(sid)
    }
}