package za.co.unimarket.database

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
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
import java.util.concurrent.TimeUnit

/**
 * Member 3 — Offline Synchronisation Worker
 * Reads queued offline actions from Room (SyncActionDao) and synchronises them
 * with the ASP.NET Core / Firestore backend when network connectivity is restored.
 * Deletes local queued actions only after the server confirms receipt.
 */
class OfflineSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val db = AppDatabase.getDatabase(applicationContext)
        val dao = db.syncActionDao()

        val pending = try {
            dao.getPendingSyncActions()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to read pending sync actions from Room", e)
            return Result.failure()
        }

        if (pending.isEmpty()) {
            Log.d(TAG, "Offline queue is empty. Nothing to synchronise.")
            return Result.success()
        }

        Log.d(TAG, "Attempting to synchronise ${pending.size} pending offline action(s)...")

        return try {
            val request = SyncRequest(
                actions = pending.map { entity ->
                    SyncActionDto(
                        actionType = entity.actionType,
                        entityId = entity.entityId,
                        payloadJson = entity.payloadJson
                    )
                }
            )

            val response = ApiClient.apiService.syncData(request)

            if (response.isSuccessful) {
                val syncedIds = pending.map { it.id }
                dao.deleteSyncActions(syncedIds)
                Log.d(TAG, "Successfully synchronised and cleared ${syncedIds.size} offline action(s).")
                Result.success()
            } else {
                Log.w(TAG, "Backend returned HTTP ${response.code()} during sync. Scheduling retry.")
                Result.retry()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Network exception during offline sync. Will retry when online.", e)
            Result.retry()
        }
    }

    companion object {
        private const val TAG = "OfflineSyncWorker"
        private const val UNIQUE_WORK_NAME = "UniMarketOfflineSyncWork"

        /**
         * Enqueues a one-time background sync work request that executes
         * automatically as soon as the device has an active internet connection.
         */
        fun requestOfflineSync(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<OfflineSyncWorker>()
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    15L,
                    TimeUnit.SECONDS
                )
                .build()

            WorkManager.getInstance(context.applicationContext).enqueueUniqueWork(
                UNIQUE_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
            Log.d(TAG, "OfflineSyncWorker scheduled waiting for network connection.")
        }
    }
}
