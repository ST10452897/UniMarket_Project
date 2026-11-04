package za.co.unimarket.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/** User DAO */
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getCurrentUser(): UserEntity?

    @Query("DELETE FROM users")
    suspend fun clearUser()
}

/** Category DAO */
@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Query("SELECT * FROM categories")
    fun getAllCategoriesFlow(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<CategoryEntity>
}

/** Product DAO */
@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Query("SELECT * FROM products ORDER BY id DESC")
    fun getAllProductsFlow(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products ORDER BY id DESC")
    suspend fun getAllProducts(): List<ProductEntity>

    @Query("SELECT * FROM products WHERE isWishlisted = 1")
    fun getWishlistedProductsFlow(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE isWishlisted = 1")
    suspend fun getWishlistedProducts(): List<ProductEntity>

    @Query("UPDATE products SET isWishlisted = :isWishlisted WHERE id = :productId")
    suspend fun updateWishlistStatus(productId: String, isWishlisted: Boolean)

    @Query("SELECT * FROM products WHERE title LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%'")
    fun searchProductsFlow(query: String): Flow<List<ProductEntity>>

    @Query("DELETE FROM products WHERE id = :productId")
    suspend fun deleteProduct(productId: String)
}

/** Message DAO */
@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Query("SELECT * FROM messages")
    fun getAllMessagesFlow(): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages")
    suspend fun getAllMessages(): List<MessageEntity>
}

/** Notification DAO */
@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    @Query("SELECT * FROM notifications")
    fun getAllNotificationsFlow(): Flow<List<NotificationEntity>>

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :notificationId")
    suspend fun markAsRead(notificationId: String)
}

/** Saved Search DAO */
@Dao
interface SavedSearchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedSearch(search: SavedSearchEntity)

    @Query("SELECT * FROM saved_searches ORDER BY createdAt DESC")
    fun getAllSavedSearchesFlow(): Flow<List<SavedSearchEntity>>
}

/** Offline Action Queue DAO */
@Dao
interface SyncActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncAction(action: SyncActionEntity)

    @Query("SELECT * FROM sync_actions ORDER BY timestamp ASC")
    suspend fun getPendingSyncActions(): List<SyncActionEntity>

    @Query("DELETE FROM sync_actions WHERE id IN (:ids)")
    suspend fun deleteSyncActions(ids: List<Long>)
}
