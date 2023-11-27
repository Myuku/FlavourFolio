package com.example.flavourfolio.database

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class ActionRepository(private val actionDao: ActionDao) {

    @WorkerThread
    fun retrieveActions(sid: Int): Flow<List<Action>> {
        return actionDao.getDetailsForStep(sid)
    }

    @WorkerThread
    suspend fun insert(action: Action): Int {
        return if (actionDao.isStepExist(action.sid)) {
            actionDao.insert(action)
            0
        } else {
            1
        }
    }

    @WorkerThread
    suspend fun deleteAction(aid: Int) {
        actionDao.delete(aid)
    }
}