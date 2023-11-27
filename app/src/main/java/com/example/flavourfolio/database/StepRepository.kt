package com.example.flavourfolio.database

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class StepRepository(private val stepDao: StepDao) {
    @WorkerThread
    suspend fun retrieveSteps(rid: Int): Flow<List<Step>>? {
        if (stepDao.isRecipeExist(rid)) {
            return stepDao.getStepsForRecipe(rid)
        }
        return null
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
    suspend fun length(rid: Int): Int {
        return stepDao.length(rid)
    }


    @WorkerThread
    suspend fun deleteStep(sid: Int) {
        stepDao.delete(sid)
    }
}