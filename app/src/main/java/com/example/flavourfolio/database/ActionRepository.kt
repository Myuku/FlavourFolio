package com.example.flavourfolio.database

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class ActionRepository(private val actionDao: ActionDao) {

    @WorkerThread
    suspend fun retrieveIn(sid: Int): Action? {
        return actionDao.getInForStep(sid)
    }
    @WorkerThread
    suspend fun retrieveFor(sid: Int): Action? {
        return actionDao.getForForStep(sid)
    }
    @WorkerThread
    suspend fun retrieveUntil(sid: Int): Action? {
        return actionDao.getUntilForStep(sid)
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