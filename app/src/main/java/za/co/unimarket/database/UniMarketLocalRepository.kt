package za.co.unimarket.database

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import za.co.unimarket.data.MockData
import za.co.unimarket.model.CategoryItem
import za.co.unimarket.model.MessageThread
import za.co.unimarket.model.NotificationItem
import za.co.unimarket.model.Product
import za.co.unimarket.network.ApiClient
import za.co.unimarket.network.SyncActionDto
import za.co.unimarket.network.UniMarketApiService

/**
 * Main Data Repository linking local Room DB storage with Retrofit REST Services & Offline Sync.
 */
class UniMarketLocalRepository(
    context: Context,
    private val apiService: UniMarketApiService = ApiClient.apiService
) {
    private val db = AppDatabase.getDatabase(context)
    private val userDao = db.userDao()
    private val categoryDao = db.categoryDao()
    private val productDao = db.productDao()
    private val messageDao = db.messageDao()
    private val notificationDao = db.notificationDao()
    private val savedSearchDao = db.savedSearchDao()
    private val syncActionDao = db.syncActionDao()

    // ----------------------------------------------------
    // User / Auth
    // ----------------------------------------------------
    suspend fun getCurrentUser(): UserEntity? = withContext(Dispatchers.IO) {
        userDao.getCurrentUser()
    }

    suspend fun saveUser(user: UserEntity) = withContext(Dispatchers.IO) {
        userDao.insertUser(user)
    }

    suspend fun logout() = withContext(Dispatchers.IO) {
        userDao.clearUser()
    }

    // ----------------------------------------------------
    // Categories
    // ----------------------------------------------------
    fun getCategoriesFlow(): Flow<List<CategoryItem>> {
        return categoryDao.getAllCategoriesFlow().map { entities ->
            if (entities.isEmpty()) {
                MockData.categories
            } else {
                entities.map { it.toDomain() }
            }
        }
    }

    suspend fun refreshCategories() = withContext(Dispatchers.IO) {
        runCatching {
            val response = apiService.getCategories()
            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                val dtos = response.body()!!
                val entities = dtos.map { dto ->
                    CategoryEntity(
                        id = dto.id,
                        name = dto.name,
                        itemCount = dto.itemCount,
                        iconRes = za.co.unimarket.R.drawable.ic_box,
                        bgColorRes = za.co.unimarket.R.color.category_blue_bg
                    )
                }
                categoryDao.insertCategories(entities)
            }
        }
    }

    // ----------------------------------------------------
    // Products & Listings
    // ----------------------------------------------------
    fun getProductsFlow(): Flow<List<Product>> {
        return productDao.getAllProductsFlow().map { entities ->
            if (entities.isEmpty()) {
                MockData.products
            } else {
                entities.map { it.toDomain() }
            }
        }
    }

    fun getWishlistedProductsFlow(): Flow<List<Product>> {
        return productDao.getWishlistedProductsFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun toggleWishlist(product: Product) = withContext(Dispatchers.IO) {
        val newWishlistState = !product.isWishlisted
        product.isWishlisted = newWishlistState
        
        // Update Room DB
        productDao.updateWishlistStatus(product.id, newWishlistState)
        
        // Enqueue offline sync action
        syncActionDao.insertSyncAction(
            SyncActionEntity(
                actionType = "WISHLIST",
                entityId = product.id,
                payloadJson = "{\"isWishlisted\": $newWishlistState}"
            )
        )
    }

    suspend fun addProduct(product: Product) = withContext(Dispatchers.IO) {
        val entity = ProductEntity.fromDomain(product, synced = false)
        productDao.insertProduct(entity)

        syncActionDao.insertSyncAction(
            SyncActionEntity(
                actionType = "CREATE_LISTING",
                entityId = product.id,
                payloadJson = "{\"title\": \"${product.title}\", \"price\": ${product.price}, \"category\": \"${product.category}\"}"
            )
        )
    }

    fun searchProductsFlow(query: String): Flow<List<Product>> {
        return productDao.searchProductsFlow(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    // ----------------------------------------------------
    // Messages & Communications
    // ----------------------------------------------------
    fun getMessagesFlow(): Flow<List<MessageThread>> {
        return messageDao.getAllMessagesFlow().map { entities ->
            if (entities.isEmpty()) {
                MockData.messages
            } else {
                entities.map { it.toDomain() }
            }
        }
    }

    suspend fun sendMessage(thread: MessageThread) = withContext(Dispatchers.IO) {
        messageDao.insertMessage(MessageEntity.fromDomain(thread))
        
        syncActionDao.insertSyncAction(
            SyncActionEntity(
                actionType = "SEND_MESSAGE",
                entityId = thread.id,
                payloadJson = "{\"content\": \"${thread.lastMessage}\"}"
            )
        )
    }

    // ----------------------------------------------------
    // Notifications
    // ----------------------------------------------------
    fun getNotificationsFlow(): Flow<List<NotificationItem>> {
        return notificationDao.getAllNotificationsFlow().map { entities ->
            if (entities.isEmpty()) {
                MockData.notifications
            } else {
                entities.map { it.toDomain() }
            }
        }
    }

    // ----------------------------------------------------
    // Saved Searches
    // ----------------------------------------------------
    suspend fun saveSearch(keyword: String, category: String? = null) = withContext(Dispatchers.IO) {
        savedSearchDao.insertSavedSearch(
            SavedSearchEntity(
                id = "ss_${System.currentTimeMillis()}",
                keyword = keyword,
                category = category
            )
        )
    }

    // ----------------------------------------------------
    // Offline Data Sync (/api/sync)
    // ----------------------------------------------------
    suspend fun performOfflineSync(): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            val pendingActions = syncActionDao.getPendingSyncActions()
            if (pendingActions.isEmpty()) return@runCatching true

            val dtos = pendingActions.map { action ->
                SyncActionDto(
                    actionType = action.actionType,
                    entityId = action.entityId,
                    payloadJson = action.payloadJson,
                    timestamp = action.timestamp
                )
            }

            val request = za.co.unimarket.network.SyncRequest(dtos, System.currentTimeMillis())
            val response = apiService.syncData(request)

            if (response.isSuccessful) {
                val actionIds = pendingActions.map { it.id }
                syncActionDao.deleteSyncActions(actionIds)
                true
            } else {
                false
            }
        }.getOrDefault(false)
    }
}
