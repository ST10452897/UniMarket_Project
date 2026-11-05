package za.co.unimarket.database

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import za.co.unimarket.network.ApiClient
import za.co.unimarket.network.SyncActionDto
import za.co.unimarket.network.SyncRequest

class OfflineSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val db = AppDatabase.getDatabase(applicationContext)
        val dao = db.syncActionDao()

        val pending = dao.getPendingSyncActions()

        if (pending.isEmpty()) {
            Log.d(TAG, "No pending offline actions to sync.")
            return Result.success()
        }

        return try {
            // The request format matches the backend API.
            // Actions are only deleted after the server confirms them.
            val request = SyncRequest(
                actions = pending.map {
                    SyncActionDto(
                        actionType = it.actionType,
                        entityId = it.entityId,
                        payloadJson = it.payloadJson
                    )
                }
            )

            val response = ApiClient.apiService.syncData(request)

            if (response.isSuccessful) {
                dao.deleteSyncActions(pending.map { it.id })
                Log.d(TAG, "Successfully synced and cleared ${pending.size} offline actions.")
                Result.success()
            } else {
                Log.w(TAG, "Server returned HTTP ${response.code()}, retrying later.")
                Result.retry()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Offline sync failed due to network error, retrying.", e)
            Result.retry()
        }
    }

    companion object {
        private const val TAG = "OfflineSyncWorker"
        private const val UNIQUE_WORK_NAME = "UniMarketOfflineSyncWork"

        /**
         * Schedules a one-time background sync that executes automatically
         * as soon as the device has an active network connection.
         */
        fun requestOfflineSync(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val work = OneTimeWorkRequestBuilder<OfflineSyncWorker>()
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                UNIQUE_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                work
            )
        }
    }
}

/**
 * Top-level helper function matching the specification so callers can invoke
 * requestOfflineSync(context) directly after queuing an offline action in Room.
 */
fun requestOfflineSync(context: Context) {
    OfflineSyncWorker.requestOfflineSync(context)
}
